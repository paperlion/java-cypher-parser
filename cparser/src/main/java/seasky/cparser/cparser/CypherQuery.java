package seasky.cparser.cparser;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seasky.cparser.cparser.entry.clause.Match;
import seasky.cparser.cparser.entry.clause.Return;
import seasky.cparser.cparser.entry.clause.Where;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CypherQuery {
    List<Match> matches;
    List<Match> merges;
    Where wheres;
    Return returns;
    
    public static CypherQuery from(List<Match> matches, List<Match> merges, Where wheres, Return returns)
    {
        return new CypherQuery(matches, merges, wheres, returns);
    }	
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if (matches != null)
        for (Match m : matches)
        {
            sb.append("Match ").append(m.toString()).append(" ");
        }
        if (merges != null)
        for (Match m : merges)
        {
            sb.append("Merge ").append(m.toString()).append(" ");
        }
        if (wheres != null && wheres.getConstrains().size() > 0)
        {
            sb.append("Where ").append(wheres.toString()).append(" ");
        }
        if (returns != null && returns.getReturns().size() > 0)
        {
            sb.append("Return ").append(returns.toString()).append(" ");
        }
        return sb.toString();
    }
}
