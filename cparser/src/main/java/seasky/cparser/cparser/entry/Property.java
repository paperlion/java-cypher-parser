package seasky.cparser.cparser.entry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class Property {  
//    final static public String pattern = "^(?:(\\w+)|`([\\w\\s]+)`)$";
    
	@NonNull String value;
    
    public static Property from(String string) throws CypherParseException {
        return from(string, Counter.build());
    }
    
    public static Property from(String string, Counter counter) throws CypherParseException{
        
        boolean quoted = false;
        int currStart = counter.get();
        
        while (counter.get() < string.length()) {
            char c = string.charAt(counter.get());
            if (quoted && c != '`') {
                counter.inc();
                continue;
            }
            if (currStart == counter.get()) {
                if (c == '`')
                    quoted = true;
                else if (!Character.isAlphabetic(c) && c != '_') {
                    throw new CypherParseException("A property name should start with a alpha or a _ or quote `, but start with " + c);
                }
            } else if (c == '`' && string.charAt(counter.get() - 1) != '\\') {
                if (!quoted) {
                    throw new CypherParseException("quote character ` should not appear in the middle of property name");
                }
                quoted = false;
                String value = string.substring(currStart + 1, counter.inc().get() - 1);
                Property property = new Property(value);
                return property;
            } else if (!Character.isAlphabetic(c) && c != '_' && !Character.isDigit(c)) {
                // the property is end
                String value = string.substring(currStart, counter.get());
                Property property = new Property(value);
                return property;
            }
            counter.inc();
        }
        if (quoted) {
            throw new CypherParseException("Unclosed quote ` at " + currStart);
        }
        String value = string.substring(currStart, counter.get());
        Property property = new Property(value);
        return property;
    }
    
    public String toString() {
        boolean quoted = false;
        
        Pattern r = Pattern.compile("^[a-zA-Z_]\\w*$");
        Matcher m = r.matcher(value);
        if (!m.find()) quoted = true;
        
        if (quoted) {
            return "`" + value + "`";
        }	
        return value;
    }
}
