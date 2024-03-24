package seasky.cparser.cparser.entry;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.tool.Counter;

@Getter @Setter
@AllArgsConstructor
@EqualsAndHashCode
public class VarProper {
//    final static public String pattern = "^(\\w+)\\.(.*)$";
	@NonNull Variable var;
	@NonNull Property prop;
    
    public String toString() {
        return var.toString() + "." + prop.toString();
    }
    
    public static VarProper from(String string) throws CypherParseException {
        return from(string, Counter.build());
    }
    
    public static VarProper from(String string, Counter counter) throws CypherParseException{
        
        int flag = 0; // 0 means the variable name and 1 means waiting for the dot, and 2 means waiting for the property name
        
        Variable variable = null;
        Property property = null;
        while (counter.get() < string.length()) {
            char c = string.charAt(counter.get());
            if (Character.isSpaceChar(c)) {
                ;
            }
            else if (flag == 0) {
                variable = Variable.from(string, counter);
                flag = 1;
                continue;
            }
            else if (flag == 1) {
                if (c != '.') {
                    throw new CypherParseException("Unexceted character " + c + " at index " + counter.get() +", should be .");
                }
                flag = 2;
            }
            else if (flag == 2) {
                property = Property.from(string, counter);
                break;
            }
            counter.inc();
        }
        if (variable != null && property != null)
            return new VarProper(variable, property);
        else {
            throw new CypherParseException("Syntex error : should expect a variable and a property but failed");
        }
    }
    
    public static VarProper from(Variable var, Property prop)
    {
        return new VarProper(var, prop);
    }
}
