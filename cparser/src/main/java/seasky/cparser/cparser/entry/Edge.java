package seasky.cparser.cparser.entry;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.tool.Counter;	

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Edge {
    final static public String pattern = "^-\\[([\\w\\s]*)((?:[:\\w\\s]*))(\\*)?\\s*(?:\\{(.*)\\})?\\s*\\]->$";
    
    Variable variable;
    List<Type> types;
    List<Property> properties;
    
    int lenMin;
    int lenMax;
    
    Node to;
    
    public String toString() {
        return "[" + (variable != null ? variable.toString() : "")
                + (types != null ? types.stream().map(Type::toString).reduce("", String::concat) : "")
                + getLengthExpression(lenMin, lenMax)
                + (properties != null && properties.size() > 0? "{" + String.join(",", properties.stream().map(Property::toString).toList()) + "}" : "")
                + "]";
    }
    
    public static String getLengthExpression(int lenMin, int lenMax)
    {
        if (lenMin == 1 && lenMax == 1)
        {
            return "";
        }
        if (lenMin == 1 && lenMax == Integer.MAX_VALUE)
        {
            return "*";
        }
        if (lenMin == 1)
        {
            return "*" + ".." + lenMax;
        }
        if (lenMax == Integer.MAX_VALUE)
        {
            return "*" + lenMin + "..";
        }
        return "*" + lenMin + ".." + lenMax;
    }
    
    public static Edge from(String string) throws CypherParseException {
       
        return from(string, Counter.build());
    }
    
    public static Edge from(String string, Counter counter) throws CypherParseException{
        int flag = 0; // 0 means waiting the variable name and 1 means waiting for the types, 
        // and 2 means waiting for the length, 3 means waiting for requirements and 4 means finished
        int currStart = counter.get();
        
        Variable variable = null;
        List<Type> types = null;
        List<Property> requirements = null;
        
        int lenMin = 1;
        int lenMax = 1;
        
        while (counter.get() < string.length()) 
        {
            char c = string.charAt(counter.get());
            if (Character.isSpaceChar(c)) {
                ;
            } 
            else if (currStart == counter.get())
            {
                if (c != '[')
                    throw new CypherParseException("Edge should start with the [ character, but start with " + c);
            }
            else if (c == ']') 
            {
                counter.inc();
                return new Edge(variable, types, requirements, lenMin, lenMax, null);
            }
            else if (c == '{' && flag < 4) 
            {
                requirements = Property.getRequirements(string, counter.inc());
                flag = 4;
                continue;
            } 
            else if (c == '}' && flag == 4) 
            {
                ;
            }
            else if (c == '*' && flag < 3)
            {
                flag = 3;
                String pattern = "^\\*(?:(\\d*)..(\\d*))?";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(string.substring(counter.get()));
                if (m.find())
                {
                    lenMin = m.group(1) != null ? Integer.valueOf(m.group(1)) : 1;
                    lenMax = m.group(2) != null ? Integer.valueOf(m.group(2)) : Integer.MAX_VALUE;
                    counter.inc(m.end());
                    continue;
                }
                
            }
            else if (c == ':' && flag < 2) 
            {
                types = Type.getTypes(string, counter);
                flag = 2;
                continue;
            }
            else if (flag == 0) 
            {
                variable = Variable.from(string, counter);
                flag = 1;
                continue;
            }
            counter.inc();
        }
        throw new CypherParseException("Cannot found right parathesis ) for the node");
    }
    
    
    public static Edge from() throws CypherParseException {
        return new Edge();
    }
    
}
