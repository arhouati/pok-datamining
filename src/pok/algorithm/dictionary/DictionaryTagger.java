package pok.algorithm.dictionary;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVReader;

import pok.algorithm.txt.process.Word;

// TODO implement singleton for loading dictionary and negation from CSV
public class DictionaryTagger {
	
	/**
	 * The only instance of Dictionary 
	 */
	static private DictionaryTagger instance;
	
	private static HashMap<String,String[]> dictionaryFr;
	private static HashMap<String,String[]> negationFr;
	
	private DictionaryTagger() throws IOException {
		
		CSVReader dictionaryFileFr = new CSVReader(new FileReader("resources/dictionary/fr/dictionary-fr.csv"));
		CSVReader negationFileFr = new CSVReader(new FileReader("resources/dictionary/fr/negation-fr.csv"));
		
		loadDictionaryFromCSV( dictionaryFileFr );
		loadNegationFromCSV( negationFileFr );
		
	}
	
	/**
	 * Used to access {@link DictionaryTagger}
	 * 
	 * @return an instance of {@link DictionaryTagger}
	 */
	public static synchronized DictionaryTagger getInstance()
	{
		try
		{
			if (instance == null) instance = new DictionaryTagger();
			return instance;
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not init DictionaryTagger: " + e.getMessage());
		}
	}
	
	public Word sentimentTagger(Word word) throws IOException {
			
			String wordLemma = word.getLemmer().toLowerCase();
						
			switch ( word.getTag() ) {
			
				case "V": // verb
				case "VIMP": // imperative verb
				case "VPP": // past participle verb
				case "VPR": // present participle verb
				case "VINF": // infinitive verb			
				case "NC": // noun					
				case "ADJ":	 // adjective
					word.setSentiment( getSentimentFromDictionary(wordLemma) );
					break;
					
				case "ADV": // adverb
				case "N": // adverb	
					if( isNegation( wordLemma ) ){
						word.setType( getNegationType( wordLemma ));
						word.setSentiment( "neutral" );
						break;
					}
					
					// TODO: test if ADV is increment or decrements
					word.setSentiment( getSentimentFromDictionary( wordLemma ) );

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
	
	private static boolean loadDictionaryFromCSV( CSVReader dictionaryFile ) throws IOException {
		
		List<String[]> lines = dictionaryFile.readAll();
		
		dictionaryFr = new HashMap<String,String[]>();
				
		for( String[] line : lines){
			line = line[0].split(";");
			dictionaryFr.put(line[1], line);
		}
		
		return false;
	}
	
	private static boolean loadNegationFromCSV( CSVReader negationFile ) throws IOException {
		
		List<String[]> lines = negationFile.readAll();
		
		negationFr = new HashMap<String,String[]>();
				
		for( String[] line : lines){
			line = line[0].split(";");
			negationFr.put(line[0], line);
		}
		
		return false;
	}
	
	private static String getSentimentFromDictionary( String wordName ){
		
		if( dictionaryFr.get(wordName) != null )
			return dictionaryFr.get(wordName)[2];
		
		return "neutral";
	}
	
	private static boolean isNegation( String ADV ){
		return negationFr.get(ADV) != null ? true : false;
	}
	
	private static String getNegationType( String ADV ){	
		return negationFr.get(ADV)[1];
	}
	
}
