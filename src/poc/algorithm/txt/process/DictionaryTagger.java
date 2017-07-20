package poc.algorithm.txt.process;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVReader;

import poc.tools.FrenchStemmer;

// TODO implement singleton for loading dictionary and negation from CSV
public class DictionaryTagger {

	private static HashMap<String,String[]> dictionary;
	private static HashMap<String,String[]> negation;
	
	public static Word sentimentTagger(Word word) {
				
			String wordName = word.getWord().toLowerCase();
						
			switch ( word.getTag() ) {
				case "V":
					wordName = getInfinitiveVerbe(wordName);
					word.setSentiment( getSentimentFromDictionary(wordName) );
					break;
					
				case "ADJ":	
					
					if( dictionary.get( getSingularNoun( wordName ) ) != null )
						wordName = getSingularNoun( wordName );
					else
						wordName = getSingularNounWhithAl( wordName );
					
					word.setSentiment( getSentimentFromDictionary(wordName) );

					break;
					
				case "ADV":
					
					if( isNegation( wordName ) )
						word.setType( getNegationType( wordName ));
					
					word.setSentiment( getSentimentFromDictionary(wordName) );
					break;
				
				case "NC":
				case "PONCT":
				case "CLS":
					word.setSentiment( "neutral" );
					break;
					
				default:
					word.setSentiment( "neutral" );
					break;
			}
			
			

		return word;
	}
	
	// TOOD manage error >> return false if dictionary is not loaded 
	public static boolean loadDictionaryFromCSV( CSVReader dictionaryFile ) throws IOException {
		
		List<String[]> lines = dictionaryFile.readAll();
		
		dictionary = new HashMap<String,String[]>();
				
		for( String[] line : lines){
			line = line[0].split(";");
			dictionary.put(line[1], line);
		}
		
		return false;
	}
	
	// TOOD manage error >> return false if negation file is not loaded 
	public static boolean loadNegationFromCSV( CSVReader negationFile ) throws IOException {
		
		List<String[]> lines = negationFile.readAll();
		
		negation = new HashMap<String,String[]>();
				
		for( String[] line : lines){
			line = line[0].split(";");
			negation.put(line[0], line);
		}
		
		return false;
	}
		
	
	private static String getSentimentFromDictionary( String wordName ){
		
		if( dictionary.get(wordName) != null )
			return dictionary.get(wordName)[2];
		
		// approximate search (used especially for verbs)
		for( String word : dictionary.keySet()){
			if( wordName.length() < word.length() && wordName.equals( word.substring(0, wordName.length())) ){
				return dictionary.get(word)[2];
			}
		}
		
		return "neutral";
	}
		
	// TODO improve method to get infinitive, today it just return a stemmed verbe
	private static String getInfinitiveVerbe( String verbe ){
		FrenchStemmer stemer = new FrenchStemmer();
		return stemer.stem( verbe );
		
	}
	
	private static String getSingularNoun( String noun ){
		if( noun.length() > 3)
		{
			if(  "eux".equals( noun.substring(noun.length() - 3, noun.length()) ) ||
					"aux".equals( noun.substring(noun.length() - 3, noun.length()) ) ||
					"eaux".equals( noun.substring(noun.length() - 4, noun.length()) ) )
				return noun.substring(0, noun.length() - 1);
			
			if(  "aux".equals( noun.substring(noun.length() - 3, noun.length()) ) )
				return noun.replace("aux", "al");
			
			if( noun.charAt( noun.length() - 1 ) == 's' )
				return noun.substring(0, noun.length() - 1);	

		}
		
		return noun;
	}
	
	private static String getSingularNounWhithAl( String noun ){
		if( noun.length() > 3)
		{
			if(  "aux".equals( noun.substring(noun.length() - 3, noun.length()) ) )
				return noun.replace("aux", "al");
		}
		
		return noun;
	}
		
	
	private static boolean isNegation( String ADV ){
		return negation.get(ADV) != null ? true : false;
	}
	
	private static String getNegationType( String ADV ){	
		return negation.get(ADV)[1];
	}
	
}
