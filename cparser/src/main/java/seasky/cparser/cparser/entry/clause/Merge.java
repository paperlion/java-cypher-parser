package seasky.cparser.cparser.entry.clause;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import seasky.cparser.cparser.entry.Edge;
import seasky.cparser.cparser.entry.Node;
import seasky.cparser.cparser.exception.CypherIndexOutofBoundException;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.tool.Counter;

@Getter
@Setter
@AllArgsConstructor
public class Merge extends Clause {
	@NonNull
	List<Edge> edgeList;

	public static Merge from(String string) throws CypherParseException {
		return from(string, Counter.build());
	}

	public static Merge from(String string, Counter counter) throws CypherParseException {
		int flag = 0; // flag = 0 means nothing and waiting for a node, 1 means waiting for a edge

		int nodeCount = 0;

		List<Edge> edgeList = new ArrayList<Edge>();
		// add the header empty edge
		edgeList.add(Edge.from());

		while (counter.get() < string.length()) {
			char c = string.charAt(counter.get());
			if (Character.isSpaceChar(c)) {
				;
			} else if (c == '(') {
				if (flag == 0) {
					edgeList.get(nodeCount++).setTo(Node.from(string, counter));
					flag = 1;
					continue;
				} else {
					throw new CypherParseException(
							"unexcepted left parathesis \'" + c + "\' at index : " + counter.get());
				}
			} else if (c == '[') {
				if (flag == 1) {
					edgeList.add(Edge.from(string, counter));
					flag = 0;
					continue;
				} else {
					throw new CypherParseException("unexcepted symbol \'" + c + "\' at index : " + counter.get());
				}
			} else if (c == '-' || c == '>') {
				;
			} else {
				break;
			}
			counter.inc();
		}
		if (flag != 1) {
			throw new CypherParseException(
					"unfinished merge clause, expecting a node but got " + string.charAt(counter.get()));
		}
		return new Merge(edgeList);
	}

	public String toString() {
		if (edgeList.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("MERGE ");
		for (int i = 0; i < edgeList.size(); i++) {
			if (i != 0) {
				sb.append('-').append(edgeList.get(i).toString()).append("->");

			}
			sb.append(edgeList.get(i).getTo().toString());
		}
		return sb.toString();
	}
	
	public Keyword type()
    {
    	return Keyword.MERGE;
    }

	public int size() {
		return edgeList.size();
	}

	/***
	 * Get the edge by index, starting from 1. Index 0 will return the first void
	 * edge.
	 * 
	 * @param index
	 * @return
	 * @throws CypherIndexOutofBoundException
	 */
	public Edge getEdgeByIndex(int index) throws CypherIndexOutofBoundException {
		if (index >= size() || index < 0) {
			throw new CypherIndexOutofBoundException("Index " + index + " out of bound " + size());
		}
		return edgeList.get(index);
	}

	/***
	 * Get the node by index, starting from 0.
	 * 
	 * @param index
	 * @return
	 * @throws CypherIndexOutofBoundException
	 */
	public Node getNodeByIndex(int index) throws CypherIndexOutofBoundException {
		if (index >= size() || index < 0) {
			throw new CypherIndexOutofBoundException("Index " + index + " out of bound " + size());
		}
		return edgeList.get(index).getTo();
	}
}
