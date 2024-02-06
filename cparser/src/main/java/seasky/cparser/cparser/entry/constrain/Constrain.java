package seasky.cparser.cparser.entry.constrain;

import seasky.cparser.cparser.entry.Scaler;
import seasky.cparser.cparser.entry.VarProper;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.tool.Counter;

public abstract class Constrain {
    final public static String pattern = "(\\w+\\.[\\w\\s`]+)([><]=?|=|IS NOT NULL)([\\w\\s\"\'\\.+-]+)";
    public abstract String toString(boolean b);
    public String toString() {
        return toString(false);
    };
    
    public static Constrain from(String string) throws CypherParseException {
        return from(string, Counter.build());
    }
    
    public static Constrain from(String string, Counter counter) throws CypherParseException {
        int flag = 0; // 0 means waiting for the left part and 1 means waiting for symbol, and 2 means waiting for the right part, 3 means finished
        VarProper first = null;
        Relation relation = null;
        while (counter.get() < string.length()) {
            char c = string.charAt(counter.get());
            if (Character.isSpaceChar(c)) {
                ;
            } else if (flag == 0) {
                first = VarProper.from(string, counter);
                flag = 1;
                continue;
            } else if (flag == 1) {
                relation = Relation.from(string, counter);
                flag = 2;
                continue;
            } else if (flag == 2) {
                if (relation == Relation.IsNotNull) {
                    return new VarAndScaler(first, relation, null);
                }
                else if (Character.isAlphabetic(c) || c == '_') {
                    VarProper property = VarProper.from(string, counter);
                    return new VarAndVar(first, relation, property);
                }
                else {
                    Scaler scaler = Scaler.from(string, counter);
                    return new VarAndScaler(first, relation, scaler);
                }
            }
            counter.inc();
        }
        if (relation == Relation.IsNotNull)
            return new VarAndScaler(first, relation, null);
        else {
            throw new CypherParseException("Syntex error : should expect a constraint but failed");
        }
    }
}
