package poc.algorithm.txt.process;

public class Word {
	
	private Integer id;
	private String word;
	private String tag;
	private String sentiment;
	private boolean increment;
	private boolean decrement;
	private String type;

	public Word(Integer id, String word, String tag, String sentiment,
			boolean increment, boolean decrement) {
		super();
		this.id = id;
		this.word = word;
		this.tag = tag;
		this.sentiment = sentiment;
		this.increment = increment;
		this.decrement = decrement;
	}

	public Word() {
		super();
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public boolean isIncrement() {
		return increment;
	}

	public void setIncrement(boolean increment) {
		this.increment = increment;
	}

	public boolean isDecrement() {
		return decrement;
	}

	public void setDecrement(boolean decrement) {
		this.decrement = decrement;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
