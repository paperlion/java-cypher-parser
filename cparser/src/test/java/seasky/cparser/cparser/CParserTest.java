package seasky.cparser.cparser;

import org.junit.Test;

import seasky.cparser.cparser.entry.Node;
import seasky.cparser.cparser.entry.Property;
import seasky.cparser.cparser.entry.Scaler;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.parser.Parser;

/**
 * Unit test for simple CParser.
 */
public class CParserTest 
{
    @Test
    public void testParser()
    {
    	String cypher = "Match (f:File)-[e]->(d:Directory {number:1, name:\"foo\"})-[*0..2]->() "
                + "WHERE f.a > d.x and a.d > f.v "
                + "RETURN s.a, b.b";
    	
    	try {
			Parser parser = Parser.from(cypher);
			System.out.println(parser);
			// change the directory properties
			Node n = parser.getMatches().get(0).getEdgeList().get(1).getTo();
			Property r = n.getProperties().get(0);
			System.out.println(r);
			r.setScaler(Scaler.from("2"));
			System.out.println(parser);
		} catch (CypherParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
