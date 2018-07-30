package pok.algorithm.txt.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pok.algorithm.txt.process.Word;

import it.uniroma1.lcl.babelmorph.BabelMorph;
import it.uniroma1.lcl.babelmorph.BabelMorphWord;
import it.uniroma1.lcl.jlt.util.Language;

/**
* <h1>Text pre-processing</h1>
* <p>this class pre-process text. this step is the first step of DataMining</p>
*
* @author  Abdelkader Rhouati (arhouati)
* @version 1.0
* @since   2017-05-26
*/
public class Text {
	
	final static Logger logger = Logger.getLogger(Text.class);

	public static List<ArrayList<Word>> preprocessing(String text, String lang) throws IOException {	
		
		List<ArrayList<Word>> textList = tokenization(text, lang);
		
		textList = lemmatization(textList, lang);
		
		return textList;
	}
	
	private static List<ArrayList<Word>> lemmatization(List<ArrayList<Word>> textList, String lang) throws IOException {
				
		for(ArrayList<Word> sentence : textList){
			for(Word word : sentence){
				String wordName = word.getWord().toLowerCase();
				word.setLemmer( getLemma( wordName ) ); 
			}	
		}
		return textList;
	}
	
	private static List<ArrayList<Word>> tokenization(String text, String lang) throws IOException {
		
		text = addDelimiterInthEndIFNotExist(text, lang);
		text = addSpaceBeforAndAfterDelimiter(text, lang);
		text = addSpaceBetweenfirstPPandVerb(text, lang);
		
		CoreNlpTools nlpTools = CoreNlpTools.getInstance();

		return nlpTools.wordTagger(text, lang);
	}

	/**
	 * this method add a '.' at the end of a text.
	 * 
	 * @param text
	 * @return text processed
	 */
	private static String addDelimiterInthEndIFNotExist(String text, String lang) {
		if ( !isDelimiterExist(text, lang) ) {
			text = text + " .";
		}
		return text;
	}

	/**
	 * this method check if a '.' exist at the end of a text
	 * 
	 * @param text
	 * @return text processed
	 */
	private static boolean isDelimiterExist(String text, String lang) {
		
		String lastChar = null;
		
		if ( text.substring(text.length() - 1).equals(" ")) {
			lastChar = text.substring(text.length() - 2);
		} else {
			lastChar = text.substring(text.length() - 1);
		}
		
		if (lastChar.equals(".")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * this method add space before and after a sentence Delimiter (. , ? ,! , ,)
	 * 
	 * @param text
	 * @return text processed
	 */
	private static String addSpaceBeforAndAfterDelimiter(String text, String lang) {
		text = text.replaceAll("[!|?|.|,|:]", " $0 ");
		return text;
	}

	/**
	 * this method add Space between words separated by ' (apostrophe)
	 * 
	 * @param text
	 * @return text processed
	 */
	private static String addSpaceBetweenfirstPPandVerb(String text, String lang) {
		
		text = text.replace("'", " ");
		return text;
	}

	private static String getLemma( String verb ) throws IOException{
		
		BabelMorph bm = BabelMorph.getInstance();
		
		List<BabelMorphWord> bmwFromWord = bm.getMorphologyFromWord(Language.FR, verb);
		
		return bmwFromWord.size() >= 1 ?  bmwFromWord.get(0).getLemma().toString() : verb;	
	}

}
