package seasky.cparser.cparser.entry.clause;

import lombok.AllArgsConstructor;
import lombok.Getter;
import seasky.cparser.cparser.tool.Counter;

@AllArgsConstructor
@Getter
public enum Keyword {
	MATCH("match"), MERGE("merge"), WHERE("where"), RETURN("return"), AND("and"), CREATE("create"), DELETE("delete"),
	DETACH("detach"), OTHERS("");

	private String string;

	public String toString() {

		return string;
	}

	public static Keyword from(String string) {
		return from(string, Counter.build(), false);
	}

	public static Keyword from(String string, Counter counter, boolean counted) {
		StringBuilder sb = new StringBuilder();
		int index = counter.get();
		while (index < string.length()) {
			char c = string.charAt(index);
			if (Character.isSpaceChar(c)) {
				index++;
				break;
			} else {
				sb.append(c);
			}
			index++;
		}
		for (Keyword keyword : Keyword.values()) {
			if (keyword.string.equals(sb.toString().toLowerCase())) {
				if (counted)
					counter.inc(sb.length());
				return keyword;
			}
		}
		Keyword k = Keyword.OTHERS;
		k.string = sb.toString();
		return k;
	}
}
