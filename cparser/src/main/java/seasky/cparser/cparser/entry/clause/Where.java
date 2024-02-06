package seasky.cparser.cparser.entry.clause;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import seasky.cparser.cparser.entry.constrain.Constrain;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.tool.Counter;

@Getter @Setter
@AllArgsConstructor
public class Where {
    @NonNull List<Constrain> constrains;
    
    public static Where from(String string) throws CypherParseException
    {
        return from(string, Counter.build());
    }
    
    public static Where from(String string, Counter counter) throws CypherParseException
    {
        int flag = 0; //flag = 0 means nothing or waiting for a constraint, 1 means waiting for an AND
        List<Constrain> constrains = new ArrayList<Constrain>();
        
        while (counter.get() < string.length())
        {
            char c = string.charAt(counter.get());
            if (Character.isSpaceChar(c)) {
                ;
            }
            else {
                Keyword keyword = Keyword.from(string, counter, false);
                if (keyword == Keyword.AND)
                {
                    if (flag == 1) {
                        counter.inc(3);
                        flag = 0;
                        continue;
                    }
                    else
                        throw new CypherParseException("unexcepted keyword \'and\' at index : " + counter.get());
                } else if (keyword == Keyword.OTHERS){
                    if (flag == 0) {
                        constrains.add(Constrain.from(string, counter));
                        flag = 1;
                        continue;
                    }
                    else
                        throw new CypherParseException("Unexpected word " + keyword.getString() + " at index " + counter.get());
                } else {
                    break;
                }
            }
            counter.inc();
        }
        if (constrains.size() == 0 || flag != 1)
        {
            throw new CypherParseException("Where and AND clause should followed by constrain, but got a " + string.charAt(counter.get()));
        }
        return new Where(constrains);
    }
    
    public static Where from(List<Constrain> constrains)
    {
        return new Where(constrains);
    }
    
    public String toString(boolean rmdb)
    {
        return String.join(" and ", constrains.stream().map((c)->c.toString(rmdb)).toList());
    }
    
    public String toString()
    {
        return toString(false);
    }
}
