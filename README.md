# Java Cypher Query Parser
![icon_for_neo4java](https://github.com/user-attachments/assets/b72ebe70-f6ae-4e3f-8a7c-7fd4789c8d0f)

## Overview
This Java project allows users to input a string of a Cypher query which is then parsed into Objects. Users can easily modify query properties such as variable names or attributes and convert the modified objects back into a string.

## Features
- Parse Cypher query strings into Objects
- Modify properties of the Cypher query easily
- Reconstruct the modified Objects back into query strings

## Usage Example

```java
// Example of using the Java Cypher Query Parser

public class Example {
    public static void main(String[] args) throws CypherParseException, CypherIndexOutofBoundException {
	String cypher = "Match (n1:NODE)-[e:EDGE]->(n2:NODE {numProp:1, stringProp:\"foo\"})-[*0..2]->() "
			+ "MATCH (n3:NODE:NODE_A)" + "CREATE (n4:NODE_B {stringProp:\"bar\"}) "
			+ "Merge (n1)-[:EDGE]->(n3)-[e5]->(n4) "
			+ "Delete e5 "
			+ "WHERE n1.prop1 > n2.prop1 and n1.prop2 <= n3.prop2 "
			+ "RETURN n1.prop1, n3.prop1";

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
```

## Installation
Clone the repository and build the project using your preferred Java build tool.

```bash
git clone https://github.com/paperlion/java-cypher-parser.git
```

## Contributions
Contributions are welcome. Please feel free to submit pull requests or open issues for any enhancements or bug fixes.
