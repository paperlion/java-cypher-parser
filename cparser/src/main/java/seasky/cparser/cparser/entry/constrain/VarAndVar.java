package seasky.cparser.cparser.entry.constrain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import seasky.cparser.cparser.entry.VarProper;

@Getter @Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class VarAndVar extends Constrain{
	@NonNull VarProper first;
	@NonNull Relation relation;
	@NonNull VarProper second;
    
    public String toString() {
        return first.toString() + relation.toString() + second.toString();
    }
    
    public VarAndVar reverse()
    {
        return new VarAndVar(second, relation.reverse(), first);
    }
}
