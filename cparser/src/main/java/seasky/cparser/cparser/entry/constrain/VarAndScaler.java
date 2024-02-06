package seasky.cparser.cparser.entry.constrain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import seasky.cparser.cparser.entry.Scaler;
import seasky.cparser.cparser.entry.VarProper;

@Getter @Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class VarAndScaler extends Constrain {
    VarProper first;
    Relation relation;
    Scaler second;
    
    @Override
    public String toString(boolean rmdbs) {   
        if (relation != Relation.IsNotNull) {
            return first.toString(rmdbs) + relation.toString() + second.toString();
        } else {
            return first.toString(rmdbs) + " " + relation.toString();
        }
    }  
    
}
