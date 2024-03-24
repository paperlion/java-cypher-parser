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
public class CType {
//    final static public String pattern = "^:\\s*([a-zA-Z]\\w*)$";
	@NonNull String value;
    	
    public String toString() {
        return ":" + value;
    }
    
    static public CType from(String string) throws CypherParseException {
        return from(string, Counter.build());
    }
    
    static public CType from(String string, Counter counter) throws CypherParseException {
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
                CType type = new CType(value);
                return type;
            }
            counter.inc();
        }
        String value = string.substring(currStart, counter.get());
        CType type = new CType(value);
        return type;
    }
    
    static public List<CType> getTypes(String string) throws CypherParseException {
        return getTypes(string, Counter.build());
    }
    
    static public List<CType> getTypes(String string, Counter counter) throws CypherParseException{
        List<CType> types = new ArrayList<>();
        while (counter.get() < string.length()) {
            char c = string.charAt(counter.get());
            if (Character.isSpaceChar(c)) {
                ;
            }
            else if (c == ':') {
                types.add(CType.from(string, counter));
                continue;
            } else {
                break;
            }
            counter.inc();
        } 
        return types;
    }
}
