package seasky.cparser.cparser.entry.clause;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import seasky.cparser.cparser.entry.VarProper;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.tool.Counter;

@Getter @Setter
@AllArgsConstructor
public class Return {
    @NonNull List<VarProper> returns;
    
    public static Return from(String string) throws CypherParseException
    {
        return from(string, Counter.build());
    }
    
    public static Return from(String string, Counter counter) throws CypherParseException
    {
        int flag = 0; //flag = 0 means nothing and waiting for a varproper, 1 means waiting for a ,
        List<VarProper> returns = new ArrayList<VarProper>();
        
        while (counter.get() < string.length())
        {
            char c = string.charAt(counter.get());
            if (Character.isSpaceChar(c)) {
                ;
            }
            else if (flag == 1)
            {
                if (c == ',')
                {
                    flag = 0;
                } else {
                    break;
                }
            } else if (flag == 0){
                VarProper var = VarProper.from(string, counter);
                returns.add(var);
                flag = 1;
                continue;
                
            }
            counter.inc();
        }
        if (returns.size() == 0 || flag != 1)
        {
            throw new CypherParseException("RETURN clause should followed by varProper, but got a " + string.charAt(counter.get()));
        }
        return new Return(returns);
    }
    
    public static Return from(List<VarProper> returns)
    {
        return new Return(returns);
    }
    
    public String toString(boolean rmdb)
    {
        return String.join(", ", returns.stream().map((v) -> v.toString(rmdb)).toList());
    }
    
    public String toString()
    {
        return toString(false);
    }
}
