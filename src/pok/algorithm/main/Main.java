package pok.algorithm.main;

import java.io.IOException;

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

// TODO implement log4j for all classes
// TODO implement specific exception 
// TODO add processing language : EN and Arabic
// TODO do some factory of all code
public class Main {	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// fr
		String text = "Non je ne suis pas prête à rejoindre Emmanuel Macron !";
		
		String lang = "fr";
		int score = 0;
		try {
			score = DataMining.process( text , lang);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("poc :: Score text :: " + score);

		if( score > 0){
			System.out.println("poc :: Orientation text :: Orientation The given text has a positive orientation.");
		}else if( score < 0 ){
			System.out.println("poc :: Orientation text :: The given text has a negative orientation.");
		}else if( score == 0){
			System.out.println("poc :: Orientation text :: The given text has a neutral orientation.");
		}
	}
}
