package seasky.cparser.cparser.entry;

import java.util.ArrayList;
import java.util.List;

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
public class Node {
    
//    final public static String pattern = "^\\(([\\w\\s]*)((?:[:\\w\\s]*))(?:\\{(.*)\\})?\\s*\\)$";
    
    Variable variable;
    List<Type> types;
    List<Property> properties;
    
    List<Edge> to;
    
    public String toString() {
        return "(" + (variable != null ? variable.toString() : "")
                + (types != null ? types.stream().map(Type::toString).reduce("", String::concat) : "")
                + (properties != null  && properties.size() > 0 ? "{" + String.join(",", properties.stream().map(Property::toString).toList()) + "}" : "")
                + ")";
    }
    
    
    public static Node from(String string) throws CypherParseException {
        return from(string, Counter.build());
    }

    public static Node from(String string, Counter counter) throws CypherParseException{
        int flag = 0; // 0 means waiting the variable name and 1 means waiting for the types, and 2 means waiting for the requirements, 3 means finished
        int currStart = counter.get();
        
        Variable variable = null;
        List<Type> types = null;
        List<Property> requirements = null;
        
        while (counter.get() < string.length()) {
            char c = string.charAt(counter.get());
            if (Character.isSpaceChar(c)) {
                ;
            } else if (currStart == counter.get()){
                if (c != '(')
                    throw new CypherParseException("Node should start with the ( character");
            }
            else if (c == ')') {
                counter.inc();
                return new Node(variable, types, requirements, new ArrayList<>());
            }
            else if (c == '{' && flag < 3) {
                requirements = Property.getRequirements(string, counter.inc());
                flag = 3;
                continue;
            } else if (c == '}' && flag == 3) {
                ;
            }
            else if (c == ':' && flag < 2) {
                types = Type.getTypes(string, counter);
                flag = 2;
                continue;
            }
            else if (flag == 0) {
                variable = Variable.from(string, counter);
                flag = 1;
                continue;
            }
            counter.inc();
        }
        throw new CypherParseException("Cannot found right parathesis ) for the node");
    }

}
