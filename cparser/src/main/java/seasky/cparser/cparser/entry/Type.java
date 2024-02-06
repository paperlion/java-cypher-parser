package seasky.cparser.cparser.entry;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.tool.Counter;

@Getter @Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Type {
//    final static public String pattern = "^:\\s*([a-zA-Z]\\w*)$";
    String value;
    	
    public String toString() {
        return ":" + value;
    }
    
    static public Type from(String string) throws CypherParseException {
        return from(string, Counter.build());
    }
    
    static public Type from(String string, Counter counter) throws CypherParseException {
        int flag = 0; // 0 means initial and wait for :, 1 means wait for the type name, 2 means finished
        int currStart = counter.get();
        while (counter.get() < string.length()) {
            char c = string.charAt(counter.get());
            if (currStart == counter.get()) {
                if (c != ':') {
                    throw new CypherParseException("A type should be started with :, but start with " + c);
                } else {
                    flag = 1; 
                }
            } else if (flag == 1) {
                if (Character.isSpaceChar(c)) {
                    ;
                } else if (Character.isAlphabetic(c) || c == '_') {
                    flag = 2;
                    currStart = counter.get();
                } else {
                    throw new CypherParseException("A type name should be started with a letter or _, but start with " + c);
                }
            } else if (flag == 2 && !Character.isAlphabetic(c) && c != '_' && !Character.isDigit(c)) {
                // the type is end
                String value = string.substring(currStart, counter.get());
                Type type = new Type(value);
                return type;
            }
            counter.inc();
        }
        String value = string.substring(currStart, counter.get());
        Type type = new Type(value);
        return type;
    }
    
    static public List<Type> getTypes(String string) throws CypherParseException {
        return getTypes(string, Counter.build());
    }
    
    static public List<Type> getTypes(String string, Counter counter) throws CypherParseException{
        List<Type> types = new ArrayList<>();
        while (counter.get() < string.length()) {
            char c = string.charAt(counter.get());
            if (Character.isSpaceChar(c)) {
                ;
            }
            else if (c == ':') {
                types.add(Type.from(string, counter));
                continue;
            } else {
                break;
            }
            counter.inc();
        } 
        return types;
    }
}
