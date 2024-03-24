package seasky.cparser.cparser;

import java.util.List;

import org.junit.Test;

import seasky.cparser.cparser.entry.CType;
import seasky.cparser.cparser.entry.Edge;
import seasky.cparser.cparser.entry.Node;
import seasky.cparser.cparser.entry.Requirement;
import seasky.cparser.cparser.entry.Scaler;
import seasky.cparser.cparser.entry.Variable;
import seasky.cparser.cparser.entry.clause.Delete;
import seasky.cparser.cparser.entry.clause.Match;
import seasky.cparser.cparser.entry.clause.Return;
import seasky.cparser.cparser.exception.CypherIndexOutofBoundException;
import seasky.cparser.cparser.exception.CypherParseException;

/**
 * Unit test for simple CParser.
 */
public class CParserTest {
	@Test
	public void testParser() throws CypherParseException, CypherIndexOutofBoundException {
		String cypher = "Match (n1:NODE)-[e:EDGE]->(n2:NODE {numProp:1, stringProp:\"foo\"})-[*0..2]->() "
				+ "MATCH (n3:NODE:NODE_A)" + "CREATE (n4:NODE_B {stringProp:\"bar\"}) "
				+ "Merge (n1)-[:EDGE]->(n3)-[e5]->(n4) " + "Delete e5 "
				+ "WHERE n1.prop1 > n2.prop1 and n1.prop2 <= n3.prop2 " + "RETURN n1.prop1, n3.prop1";

		// Load the query
		CypherQuery query = CypherQuery.from(cypher);
		System.out.println(query);

		// Get the Clause (Two ways)
		Match match1 = query.getMatchByIndex(0); // Recommended
		Match match2 = query.getClauseByIndex(1).asMatch();
		System.out.println(match1);
		System.out.println(match2);

		// Get the node or edge
		Node n2 = match1.getNodeByIndex(1);
		System.out.println(n2);
		Edge e = match1.getEdgeByIndex(1); // Notice : The first edge start from index 1, not zero
		System.out.println(e);

		// Get the node requirement
		Requirement r = n2.getRequirements().get(0);
		System.out.println(r);

		// Modify the node requirements
		r.setScaler(Scaler.from("2"));
		System.out.println(query); // the modification is reflected in the original query

		// Get and change the node types
		Node n3 = match2.getNodeByIndex(0);
		List<CType> types = n3.getTypes();
		System.out.println(types);
		types.set(1, CType.from(":TYPE_NEW"));
		System.out.println(n3);

		// Get the Delete clause
		Delete deleteClause = query.getDeleteByIndex(0);
		System.out.println(deleteClause);

		// Add a to-delete node
		deleteClause.getVars().add(Variable.from("n3"));
		System.out.println(query);

		// Get the Return clause and Remove a return value
		Return returnClause = query.getReturnByIndex(0);
		returnClause.getVarPropers().remove(1);
		System.out.println(returnClause);
		
		// Delete a clause
		query.removeClause(1);
		System.out.println(query);
	}
}
