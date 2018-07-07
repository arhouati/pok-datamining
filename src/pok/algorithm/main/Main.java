package pok.algorithm.main;

import java.io.IOException;

import org.apache.log4j.Logger;

import pok.algorithm.DataMining;

/**
* <h1>Main Script</h1>
* <p>The main class to get the orientation of a given text</p>
* <p>if score > 0 => the given text have a positive orientation </p>
* <p>if score > 0 => the given text have a negative orientation </p>
* <p>if score > 0 => the given text have a neutral orientation </p>
*
* @author  Abdelkader Rhouati (arhouati)
* @version 1.0
* @since   2017-05-26
* 
*/
public class Main {	
	
	final static Logger logger = Logger.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		String text = "je suis très gentil";
		String lang = "fr";
		int score = 0;
		try {
			
			score = DataMining.process( text , lang);
			
		} catch (IOException e) 
		{	
			logger.error(e.getMessage());
		}
		
		logger.info("poc :: text :: " + text);
		logger.info("poc :: Score  :: " + score);
		if( score > 0){
			logger.info("poc :: Orientation text :: Orientation The given text has a positive orientation.");
		}else if( score < 0 ){
			logger.info("poc :: Orientation text :: The given text has a negative orientation.");
		}else if( score == 0){
			logger.info("poc :: Orientation text :: The given text has a neutral orientation.");
		}
	
	}
}
