package pok.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pok.algorithm.dictionary.DictionaryScoring;
import pok.algorithm.txt.process.Text;
import pok.algorithm.txt.process.Word;

/**
* <h1>Main Script</h1>
*
* @author  Abdelkader Rhouati (arhouati)
* @version 1.0
* @since   2017-05-26
* 
*/
public class DataMining {
	
	final static Logger logger = Logger.getLogger(DataMining.class);

	public static int process(String text, String lang) throws IOException {

		if(logger.isDebugEnabled()){
		    logger.debug("poc :: Score :: text preprocessing :: " + text);
		}
		
		List<ArrayList<Word>> formatedText = Text.preprocessing( text, lang );
		
		DictionaryScoring opinionMiningAlgo = new DictionaryScoring();
		int score = opinionMiningAlgo.getContentScore( formatedText );
		
		if(logger.isDebugEnabled()){
			for(ArrayList<Word> sentences : formatedText)
			{
				logger.debug("poc :: Score :: start Sentence ----------- ");
				for(Word word : sentences){
					logger.debug("poc :: Score :: word :: "  + word.getId() +" >> " + word.getWord() + " >> "+ word.getLemmer() + " >> " + word.getTag()  +" >> " + word.getSentiment()  +" >> " + word.getType());
				}
				logger.debug("poc :: Score :: end Sentence ----------- ");
			}
		}
		
		return score;
		
	}
	

}
