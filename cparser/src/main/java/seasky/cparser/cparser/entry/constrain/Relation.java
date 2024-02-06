package seasky.cparser.cparser.entry.constrain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import seasky.cparser.cparser.tool.Counter;

@AllArgsConstructor
@Getter
public enum Relation {
    LarEq(">="),
    LesEq("<="),
    Equal("="),
    NoEqual("<>"),
    Larger(">"),
    Less("<"),
    IsNotNull("IS NOT NULL");
    
    private String string;
    
    public String toString() {
        
        return string;
    }
    
    public static Relation from(String string) {
        return from(string, Counter.build());
    }
    
    public static Relation from(String string, Counter counter) {
        int currStart = counter.get();
        for (Relation relation : Relation.values()) {
            if (currStart + relation.string.length() <= string.length() 
                    && relation.string.equals(string.substring(currStart, currStart + relation.string.length()))) {
                counter.inc(relation.string.length());
                return relation;
            }
        }
        return null;
    }
    
    public Relation reverse() {
        if (this == Larger) {
            return Less;
        } else if (this == Less) {
            return Larger;
        } else if (this == LarEq) {
            return LesEq;
        } else if (this == LesEq) {
            return LarEq;
        } else {
            return this;
        }
    }
}
