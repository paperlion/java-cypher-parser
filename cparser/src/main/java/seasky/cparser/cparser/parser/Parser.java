package seasky.cparser.cparser.parser;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seasky.cparser.cparser.entry.clause.Keyword;
import seasky.cparser.cparser.entry.clause.Match;
import seasky.cparser.cparser.entry.clause.Merge;
import seasky.cparser.cparser.entry.clause.Return;
import seasky.cparser.cparser.entry.clause.Where;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.tool.Counter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Parser {
    List<Match> matches;
    List<Merge> merges;
    Where where;
    Return returns;
    
    public static Parser from(String string) throws CypherParseException
    {
        List<Match> matches = new ArrayList<>();
        List<Merge> merges = new ArrayList<>();
        Where wheres = null;
        Return returns = null;
        Counter counter = Counter.build();
        while (counter.get() < string.length())
        {
            char c = string.charAt(counter.get());
            if (Character.isSpaceChar(c)) 
            {
                ;
            }
            else {
                Keyword keyword = Keyword.from(string, counter, true);
                if (keyword == Keyword.MATCH)
                {
                    matches.add(Match.from(string, counter));
                    continue;
                } 
                else if (keyword == Keyword.MERGE)
                {
                    merges.add(Merge.from(string, counter));
                    continue;
                } 
                else if (keyword == Keyword.WHERE)
                {
                    if (wheres == null) 
                    {
                        wheres = Where.from(string, counter);
                        continue;
                    }
                    else
                    {
                        throw new CypherParseException("There should be only one where clause, but found second at index " + counter.get());
                    }
                } 
                else if (keyword == Keyword.RETURN)
                {
                    if (returns == null) 
                    {
                        returns = Return.from(string, counter);
                        continue;
                    }
                    else
                    {
                        throw new CypherParseException("There should be only one return clause, but found second at index " + counter.get());
                    }
                } 
                else if (c == ';')
                {
                    counter.inc();
                    break;
                }
                else {
                    throw new CypherParseException("Unexpected word " + keyword.getString() + " at index " + counter.get());
                }
            }
            counter.inc();
        }
        return new Parser(matches, merges, wheres, returns);
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Match m : matches)
        {
            sb.append("Match ").append(m.toString()).append(" ");
        }
        for (Merge m : merges)
        {
            sb.append("Merge ").append(m.toString()).append(" ");
        }
        if (where != null && where.getConstrains().size() > 0)
        {
            sb.append("Where ").append(where.toString()).append(" ");
        }
        if (returns != null && returns.getReturns().size() > 0)
        {
            sb.append("Return ").append(returns.toString()).append(" ");
        }
        return sb.toString();
    }
    
    public void addMacth(Match match)
    {
    	if (matches == null)
    	{
    		matches = new ArrayList<>();
    	}
    	matches.add(match);
    }
    
    public void addMerge(Merge merge)
    {
    	if (merges == null)
    	{
    		merges = new ArrayList<>();
    	}
    	merges.add(merge);
    }

}
