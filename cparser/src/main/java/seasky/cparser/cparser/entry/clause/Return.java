package seasky.cparser.cparser.entry.clause;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import seasky.cparser.cparser.entry.Node;
import seasky.cparser.cparser.entry.VarProper;
import seasky.cparser.cparser.exception.CypherIndexOutofBoundException;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.tool.Counter;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class Return extends Clause{
	@NonNull
	List<VarProper> varPropers;

	public static Return from(String string) throws CypherParseException {
		return from(string, Counter.build());
	}

	public static Return from(String string, Counter counter) throws CypherParseException {
		int flag = 0; // flag = 0 means nothing and waiting for a varProper or variable, 1 means waiting for a ,
		List<VarProper> returns = new ArrayList<VarProper>();

		while (counter.get() < string.length()) {
			char c = string.charAt(counter.get());
			if (Character.isSpaceChar(c)) {
				;
			} else if (flag == 1) {
				if (c == ',') {
					flag = 0;
				} else {
					break;
				}
			} else if (flag == 0) {
				VarProper var = VarProper.from(string, counter);
				returns.add(var);
				flag = 1;
				continue;

			}
			counter.inc();
		}
		if (returns.size() == 0 || flag != 1) {
			throw new CypherParseException(
					"RETURN clause should followed by varProper, but got a " + string.charAt(counter.get()));
		}
		return new Return(returns);
	}

	public static Return from(List<VarProper> returns) {
		return new Return(returns);
	}

	public String toString() {
		if (varPropers.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("RETURN ");
        sb.append(String.join(", ", varPropers.stream().map((v) -> v.toString()).collect(Collectors.toList())));
		return sb.toString();
	}
	
	public Keyword type()
    {
    	return Keyword.RETURN;
    }
	
	public int size()
	{
		return varPropers.size();
	}
	
	/***
	 * Get the varProper by index, starting from 0.
	 * 
	 * @param index
	 * @return
	 * @throws CypherIndexOutofBoundException
	 */
	public VarProper getNodeByIndex(int index) throws CypherIndexOutofBoundException {
		if (index >= size() || index < 0) {
			throw new CypherIndexOutofBoundException("Index " + index + " out of bound " + size());
		}
		return varPropers.get(index);
	}
}
