package seasky.cparser.cparser;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seasky.cparser.cparser.entry.clause.Clause;
import seasky.cparser.cparser.entry.clause.Create;
import seasky.cparser.cparser.entry.clause.Delete;
import seasky.cparser.cparser.entry.clause.Keyword;
import seasky.cparser.cparser.entry.clause.Match;
import seasky.cparser.cparser.entry.clause.Merge;
import seasky.cparser.cparser.entry.clause.Return;
import seasky.cparser.cparser.entry.clause.Where;
import seasky.cparser.cparser.exception.CypherIndexOutofBoundException;
import seasky.cparser.cparser.exception.CypherParseException;
import seasky.cparser.cparser.tool.Counter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CypherQuery {
	private List<Clause> clauses = new ArrayList<>();
	private List<Integer> matchesIndex = new ArrayList<>();
	private List<Integer> mergesIndex = new ArrayList<>();
	private List<Integer> wheresIndex = new ArrayList<>();
	private List<Integer> returnsIndex = new ArrayList<>();
	private List<Integer> createsIndex = new ArrayList<>();
	private List<Integer> deletesIndex = new ArrayList<>();

	public static CypherQuery from(String string) throws CypherParseException {
		CypherQuery parser = new CypherQuery();
		Counter counter = Counter.build();
		while (counter.get() < string.length()) {
			char c = string.charAt(counter.get());
			if (Character.isSpaceChar(c)) {
				counter.inc();
			} else {
				Keyword keyword = Keyword.from(string, counter, true);
				if (keyword == Keyword.MATCH) {
					parser.getClauses().add(Match.from(string, counter));
				} else if (keyword == Keyword.MERGE) {
					parser.getClauses().add(Merge.from(string, counter));
				} else if (keyword == Keyword.WHERE) {
					parser.getClauses().add(Where.from(string, counter));
				} else if (keyword == Keyword.RETURN) {
					parser.getClauses().add(Return.from(string, counter));
				} else if (keyword == Keyword.CREATE) {
					parser.getClauses().add(Create.from(string, counter));
				} else if (keyword == Keyword.DELETE) {
					parser.getClauses().add(Delete.from(string, counter));
				} else if (c == ';') {
					counter.inc();
					break;
				} else {
					throw new CypherParseException(
							"Unexpected keyword " + keyword.getString() + " at index " + counter.get());
				}
			}
		}
		parser.generateIndex();
		return parser;
	}
	
	private void generateIndex()
	{
		matchesIndex.clear();
		mergesIndex.clear();
		wheresIndex.clear();
		returnsIndex.clear();
		createsIndex.clear();
		deletesIndex.clear();
		
		for (int i = 0; i < size(); i++)
		{
			Keyword type = clauses.get(i).type();
			if (type == Keyword.MATCH) {
				matchesIndex.add(i);
			} else if (type == Keyword.MERGE) {
				mergesIndex.add(i);
			} else if (type == Keyword.WHERE) {
				wheresIndex.add(i);
			} else if (type == Keyword.RETURN) {
				returnsIndex.add(i);
			} else if (type == Keyword.CREATE) {
				createsIndex.add(i);
			} else if (type == Keyword.DELETE) {
				deletesIndex.add(i);
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Clause m : clauses) {
			sb.append(m.toString()).append(" ");
		}
		
		return sb.toString();
	}
	
	public int size() {
		return clauses.size();
	}
	
	/***
	 * Get the Clause by index, starting from 0.
	 * 
	 * @param index
	 * @return
	 * @throws CypherIndexOutofBoundException
	 */
	public Clause getClauseByIndex(int index)throws CypherIndexOutofBoundException{
    	if (index >= size() || index < 0)
    	{
    		throw new CypherIndexOutofBoundException("Index " + index + " out of bound " + size());
    	}
    	return clauses.get(index);
    }
	
	/***
	 * Get the Match Clause by index, starting from 0.
	 * 
	 * @param index
	 * @return
	 * @throws CypherIndexOutofBoundException
	 */
	public Match getMatchByIndex(int index) throws CypherIndexOutofBoundException{
    	if (index >= matchesIndex.size() || index < 0)
    	{
    		throw new CypherIndexOutofBoundException("Index " + index + " out of bound " + matchesIndex.size());
    	}
    	return clauses.get(matchesIndex.get(index)).asMatch();
    }
	
	/***
	 * Get the Merge Clause by index, starting from 0.
	 * 
	 * @param index
	 * @return
	 * @throws CypherIndexOutofBoundException
	 */
	public Merge getMergeByIndex(int index) throws CypherIndexOutofBoundException{
    	if (index >= mergesIndex.size() || index < 0)
    	{
    		throw new CypherIndexOutofBoundException("Index " + index + " out of bound " + mergesIndex.size());
    	}
    	return clauses.get(mergesIndex.get(index)).asMerge();
    }
	
	/***
	 * Get the Where Clause by index, starting from 0.
	 * 
	 * @param index
	 * @return
	 * @throws CypherIndexOutofBoundException
	 */
	public Where getWhereByIndex(int index) throws CypherIndexOutofBoundException{
    	if (index >= wheresIndex.size() || index < 0)
    	{
    		throw new CypherIndexOutofBoundException("Index " + index + " out of bound " + wheresIndex.size());
    	}
    	return clauses.get(wheresIndex.get(index)).asWhere();
    }
	
	/***
	 * Get the Return Clause by index, starting from 0.
	 * 
	 * @param index
	 * @return
	 * @throws CypherIndexOutofBoundException
	 */
	public Return getReturnByIndex(int index) throws CypherIndexOutofBoundException{
    	if (index >= returnsIndex.size() || index < 0)
    	{
    		throw new CypherIndexOutofBoundException("Index " + index + " out of bound " + returnsIndex.size());
    	}
    	return clauses.get(returnsIndex.get(index)).asReturn();
    }
	
	/***
	 * Get the Create Clause by index, starting from 0.
	 * 
	 * @param index
	 * @return
	 * @throws CypherIndexOutofBoundException
	 */
	public Create getCreateByIndex(int index) throws CypherIndexOutofBoundException{
    	if (index >= createsIndex.size() || index < 0)
    	{
    		throw new CypherIndexOutofBoundException("Index " + index + " out of bound " + createsIndex.size());
    	}
    	return clauses.get(createsIndex.get(index)).asCreate();
    }
	
	/***
	 * Get the Delete Clause by index, starting from 0.
	 * 
	 * @param index
	 * @return
	 * @throws CypherIndexOutofBoundException
	 */
	public Delete getDeleteByIndex(int index) throws CypherIndexOutofBoundException{
    	if (index >= deletesIndex.size() || index < 0)
    	{
    		throw new CypherIndexOutofBoundException("Index " + index + " out of bound " + deletesIndex.size());
    	}
    	return clauses.get(deletesIndex.get(index)).asDelete();
    }
	
	public void insertClause(int index, Clause clause) {
		if (index > size())
		{
			index = size();
		}
		clauses.add(index, clause);
		generateIndex();
	}
	
	public void addClause(Clause clause)
	{
		clauses.add(clause);
		generateIndex();
	}
	
	public void removeClause(int index) 
	{
		clauses.remove(index);
		generateIndex();
	}

}
