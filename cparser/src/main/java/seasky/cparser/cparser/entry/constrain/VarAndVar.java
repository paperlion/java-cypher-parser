package seasky.cparser.cparser.entry.constrain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import seasky.cparser.cparser.entry.VarProper;

@Getter @Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class VarAndVar extends Constrain{
    VarProper first;
    Relation relation;
    VarProper second;
    
    public String toString(boolean rmdbs) {
        return first.toString(rmdbs) + relation.toString() + second.toString(rmdbs);
    }
    
    public VarAndVar reverse()
    {
        return new VarAndVar(second, relation.reverse(), first);
    }
}
