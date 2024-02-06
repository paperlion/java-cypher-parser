# Java Cipher Query Parser

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
    public static void main(String[] args) throws CypherParseException {
        // Example Cypher query
        String cypher = "Match (f:File)-[e]->(d:Directory {number:1, name:\"foo\"})-[*0..2]->() "
                + "WHERE f.a > d.x and a.d > f.v "
                + "RETURN s.a, b.b";

        // Parse the Cypher query
        Parser parser = Parser.from(cypher);
		    System.out.println(parser);

        // Get the part of the query
    		Node n = parser.getMatches().get(0).getEdgeList().get(1).getTo();
    		Property r = n.getProperties().get(0);
    		System.out.println(r);

        // Modify a property of the query 
        r.setScaler(Scaler.from("2"));

        // Convert back to string
        System.out.println(modifiedQuery);
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
