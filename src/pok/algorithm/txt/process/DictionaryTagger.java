package pok.algorithm.txt.process;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVReader;

import it.uniroma1.lcl.babelmorph.BabelMorph;
import it.uniroma1.lcl.babelmorph.BabelMorphWord;
import it.uniroma1.lcl.jlt.util.Language;
import pok.tools.FrenchStemmer;

// TODO implement singleton for loading dictionary and negation from CSV
public class DictionaryTagger {

	private static HashMap<String,String[]> dictionary;
	private static HashMap<String,String[]> negation;
	
	public static Word sentimentTagger(Word word) throws IOException {
			
			String wordName = word.getWord().toLowerCase();
						
			switch ( word.getTag() ) {
			
				case "V": // verb
				case "VIMP": // imperative verb
				case "VPP": // past participle verb
				case "VPR": // present participle verb
					wordName = getInfinitiveVerb(wordName);
					word.setSentiment( getSentimentFromDictionary(wordName) );

					break;

				case "VINF": // infinitive verb
					word.setSentiment( getSentimentFromDictionary(wordName) );
					break;
					
				case "NC": // noun					
					if( dictionary.get( getSingularNoun( wordName ) ) != null )
						wordName = getSingularNoun( wordName );
					else
						wordName = getSingularNounWhithAl( wordName );
					
					word.setSentiment( getSentimentFromDictionary(wordName) );
					break;
				
				case "ADJ":	 // adjective
					wordName = getSingularAJD(wordName);
					word.setSentiment( getSentimentFromDictionary(wordName) );
					break;
					
				case "ADV": // adverb
					
					if( isNegation( wordName ) ){
						word.setType( getNegationType( wordName ));
						word.setSentiment( "neutral" );
						break;
					}
					
					// TODO: test if ADV is increment or decrements
					word.setSentiment( getSentimentFromDictionary(wordName) );

					break;
					
				case "CLS":
			
				// neutral words
				case "PUNC":  // punctuation for coreNLP
				case "PONCT": // punctuation for openNLP
				case "NPP":	  // proper noun
				case "ET": // foreign word 
				case "ADJWH": // Interrogative adjective
				case "CC": // conjunction and subordinating conjunction
				case "P": // preposition
				case "PREF": // prefix
				case "PRO": // strong pronoun
				case "PROREL": // relative pronoun
				case "PROWH": // interrogative pronoun
				case "ADVWH": // Interrogative adverb
				case "CS":   // subordinating conjunction
				case "DET":   // determiner
				case "DETWH": // interrogative determiners
				case "I":   // interjection

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
		
		return "neutral";
	}
		
	private static String getInfinitiveVerb( String verb ) throws IOException{
		
		BabelMorph bm = BabelMorph.getInstance();
		
		List<BabelMorphWord> bmwFromWord = bm.getMorphologyFromWord(Language.FR, verb);
		
		return bmwFromWord.size() >= 1 ?  bmwFromWord.get(0).getLemma().toString() : verb;	
	}
	
	private static String getSingularAJD( String adjective ) throws IOException{
		
		BabelMorph bm = BabelMorph.getInstance();
		
		List<BabelMorphWord> bmwFromWord = bm.getMorphologyFromWord(Language.FR, adjective);
		
		return bmwFromWord.size() > 1 ?  bmwFromWord.get(0).getLemma().toString() : adjective;	
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
