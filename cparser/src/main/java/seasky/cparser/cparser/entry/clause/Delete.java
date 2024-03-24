package seasky.cparser.cparser.entry.clause;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import seasky.cparser.cparser.entry.Variable;
import seasky.cparser.cparser.exception.CypherIndexOutofBoundException;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.tool.Counter;

@Getter
@Setter
@AllArgsConstructor
public class Delete extends Clause {
	@NonNull
	List<Variable> vars;

	public static Delete from(String string) throws CypherParseException {
		return from(string, Counter.build());
	}

	public static Delete from(String string, Counter counter) throws CypherParseException {
		int flag = 0; // flag = 0 means nothing and waiting for a variable, 1 means waiting for a ,
		List<Variable> vars = new ArrayList<Variable>();

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
				Variable var = Variable.from(string, counter);
				vars.add(var);
				flag = 1;
				continue;

			}
			counter.inc();
		}
		if (vars.size() == 0 || flag != 1) {
			throw new CypherParseException(
					"DELETE clause should followed by variables, but got a " + string.charAt(counter.get()));
		}
		return new Delete(vars);
	}

	public String toString() {
		if (vars.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append(String.join(", ", vars.stream().map((v) -> v.toString()).toList()));
		return sb.toString();
	}
	
	public Keyword type()
    {
    	return Keyword.DELETE;
    }

	public int size() {
		return vars.size();
	}

	/***
	 * Get the variable by index, starting from 0.
	 * 
	 * @param index
	 * @return
	 * @throws CypherIndexOutofBoundException
	 */
	public Variable getVariableByIndex(int index) throws CypherIndexOutofBoundException {
		if (index >= size() || index < 0) {
			throw new CypherIndexOutofBoundException("Index " + index + " out of bound " + size());
		}
		return vars.get(index);
	}
}
