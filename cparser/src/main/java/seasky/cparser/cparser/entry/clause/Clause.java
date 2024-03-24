package seasky.cparser.cparser.entry.clause;

public abstract class Clause {
	
	public abstract String toString();
	
	public abstract Keyword type();
	
	public Create asCreate()
	{
		return (Create)this;
	}
	
	public Delete asDelete()
	{
		return (Delete)this;
	}
	
	public Match asMatch()
	{
		return (Match)this;
	}
	
	public Merge asMerge()
	{
		return (Merge)this;
	}
	
	public Return asReturn()
	{
		return (Return)this;
	}
	
	public Where asWhere()
	{
		return (Where)this;
	}
}
