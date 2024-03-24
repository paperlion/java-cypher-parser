package seasky.cparser.cparser.entry;

import java.util.ArrayList;
import java.util.List;

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
public class Requirement {
    final static public String pattern = "^([\\w\\s`]+):(.*)$";
    @NonNull Property prop;
    @NonNull Scaler scaler;
    
    public String toString() {
        return prop.toString() + ":" + scaler.toString();
    }
    public static Requirement from(Property prop, Scaler scaler)
    {
        return new Requirement(prop, scaler);
    }
    
    public static Requirement from(String string) throws CypherParseException {
        return from(string, Counter.build());
    }
    
    public static Requirement from(String string, Counter counter) throws CypherParseException{
        int flag = 0; // 0 means waiting for the property name and 1 means waiting for the :, and 2 means waiting for the scaler
        
        Property property = null;
        Scaler scaler = null;
        while (counter.get() < string.length()) {
            char c = string.charAt(counter.get());
            if (Character.isSpaceChar(c)) {
                counter.inc();
                continue;
            }
            else if (flag == 0) {
                property = Property.from(string, counter);
                flag = 1;
                continue;
            }
            else if (flag == 1) {
                if (c != ':') {
                    throw new CypherParseException("Unexceted character " + c + " at index" + counter.get() +", should be a :");
                }
                flag = 2;
            }
            else if (flag == 2) {
                scaler = Scaler.from(string, counter);
                break;
            }
            counter.inc();
        }
        if (property != null && scaler != null)
            return new Requirement(property, scaler);
        else {
            throw new CypherParseException("Syntex error : should expect a variable and a property but failed");
        }
    }
    
    static List<Requirement> getRequirements(String string) throws CypherParseException{
        return getRequirements(string, Counter.build());
    }
    
    public static List<Requirement> getRequirements(String string, Counter counter) throws CypherParseException{      
        List<Requirement> requirements = new ArrayList<>();
        int flag = 0; // 0 means started, 1 means waiting for a requirement and 2 means waiting for the ","
        while (counter.get() < string.length()) {
            char c = string.charAt(counter.get());
            if (Character.isSpaceChar(c)) {
                ;
            }
            else if (c == '}') {
                break;
            }
            else if (flag == 0 || flag == 1) {
                requirements.add(Requirement.from(string, counter));
                flag = 2;
                continue;
            }
            else if (flag == 2) {
                if (c != ',')  {
                    throw new CypherParseException("Unexceted character " + c + " at index " + counter.get() +", should be a , or }");
                }
                flag = 1;
            }
            counter.inc();
        }
        return requirements;
    }
}