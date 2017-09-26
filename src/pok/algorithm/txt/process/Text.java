package pok.algorithm.txt.process;

/**
* <h1>Text pre-processing</h1>
* <p>this class pre-process text. this step is the first step of DataMining</p>
*
* @author  Abdelkader Rhouati (arhouati)
* @version 1.0
* @since   2017-05-26
*/
public class Text {
	
	/**
	 * This method pre-process text to get the correct format that make easy to analysis
	 * 
	 * @param text
	 * @return text processed
	 */
	public static String preprocessing(String text, String lang) {
		
		text = addDelimiterInthEndIFNotExist(text, lang);		
		text = addSpaceBeforAndAfterDelimiter(text, lang);
		text = addSpaceBetweenfirstPPandVerb(text, lang);

		return text;
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

	
	
}
