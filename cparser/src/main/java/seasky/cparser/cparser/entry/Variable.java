package seasky.cparser.cparser.entry;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.tool.Counter;

@Getter @Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Variable {
//    final static public String pattern = "^[a-zA-Z_]\\w*$";
    
    String value;
    
    public String toString() {
        return value;
    }
    
    static public Variable from(String value) throws CypherParseException {
        return from(value, Counter.build());
    }
    
    public static Variable from(String string, Counter counter) throws CypherParseException{
        
        int currStart = counter.get();
        
        while (counter.get() < string.length()) {
            char c = string.charAt(counter.get());
            if (!Character.isAlphabetic(c) && c != '_' && currStart == counter.get()) {
                throw new CypherParseException("A value name should start with a alpha or a _, but start with " + c);
            }
            else if (!Character.isAlphabetic(c) && c != '_' && !Character.isDigit(c)) {
                // the value is end
                String value = string.substring(currStart, counter.get());
                Variable variable = new Variable(value);
                return variable;
            }
            counter.inc();
        }
        String value = string.substring(currStart, counter.get());
        Variable variable = new Variable(value);
        return variable;
    }
}
