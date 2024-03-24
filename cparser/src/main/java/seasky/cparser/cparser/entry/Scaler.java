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
public class Scaler {
    public final static String integerPattern = "([+-]?[0-9]+)";
    public final static String doublePattern = "([+-]?(?:[0-9]*[.])?[0-9]+)";
    public final static String stringPattern = "'(.*)'|\"(.*)\"";
    public final static String pattern = "^(?:" + integerPattern + "|" + doublePattern + "|" + stringPattern + ")$";
    
    @NonNull String value;
    
    enum Type { 
        INTEGER,
        DOUBLE,
        STRING
    }
    
    @NonNull Type type;
    
    public Scaler(String string) throws CypherParseException {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(string);
        if (!m.find()) 
            throw new CypherParseException(string + " is not a valid Scaler");
        else {
            if (m.group(1) != null) {
                value = m.group(1);
                type = Type.INTEGER;
            } else if (m.group(2) != null) {
                value = m.group(2);
                type = Type.DOUBLE;
            } else {
                value = m.group(3)!= null ? m.group(3) : m.group(4);
                type = Type.STRING;
            }
        }
    }
    
    public String toString() {
        if (type == Type.STRING) {
            return "\'" + value + "\'";
        }
        return value;
    }
    
    public static Scaler fromString(String string)
    {
        return new Scaler(string, Type.STRING);
    }
    
    public static Scaler fromInteger(int i)
    {
        return new Scaler(String.valueOf(i), Type.INTEGER);
    }
    
    public static Scaler fromDouble(double d)
    {
        return new Scaler(String.valueOf(d), Type.DOUBLE);
    }
    
    public static Scaler from(String string) throws CypherParseException {
        return from(string, Counter.build());
    }
    
    public static Scaler from(String string, Counter counter) throws CypherParseException{
        
        boolean quoted = false;
        char quote = 0;
        int currStart = counter.get();
        
        while (counter.get() < string.length()) {
            char c = string.charAt(counter.get());
            if (quoted && c != quote) {
                counter.inc();
                continue;
            }
            if ((c == '"' || c=='\'') && currStart == counter.get()) {
                quoted = true;
                quote = c;
            } else if (c == quote && string.charAt(counter.get() - 1) != '\\') {
                if (!quoted) {
                    throw new CypherParseException("quote character " + c + " should not appear in the middle of scaler name");
                }
                quoted = false;
                String value = string.substring(currStart, counter.inc().get());
                Scaler scaler = new Scaler(value);
                return scaler;
            } 
            else if (Character.isSpaceChar(c) || c == ',' || c == '}') {
                // the scaler is end
                String value = string.substring(currStart, counter.get());
                Scaler scaler = new Scaler(value);
                return scaler;
            }
            counter.inc();
        }
        if (quoted) {
            throw new CypherParseException("Unclosed quote \" at " + currStart);
        }
        String value = string.substring(currStart, counter.get());
        Scaler scaler = new Scaler(value);
        return scaler;
    }

}
