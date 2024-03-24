package seasky.cparser.cparser.entry.constrain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import seasky.cparser.cparser.entry.Scaler;
import seasky.cparser.cparser.entry.VarProper;

@Getter @Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class VarAndScaler extends Constrain {
	@NonNull VarProper first;
	@NonNull Relation relation;
	Scaler second;
    
    @Override
    public String toString() {   
        if (relation != Relation.IsNotNull) {
            return first.toString() + relation.toString() + second.toString();
        } else {
            return first.toString() + " " + relation.toString();
        }
    }  
    
}
