package pok.algorithm.txt.process;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

/**
* <h1>Structure</h1>
* <p>This class analyse text, and detect sentences and words. and for tag every word.</p>
*
* @author  Abdelkader Rhouati (arhouati)
* @version 1.0
* @since   2017-05-26
*/
public class Structure {
	
	//static Structure structure = new Structure();
	//static ClassLoader classLoader = structure.getClass().getClassLoader();

	/**
	* This method takes as a parameter the text that will be analyzed :
	* 	1/ Detect sentences of the text
	* 	2/ For each sentence tag each word as noun, verb, etc...
	* 
	* @param text
	* @return 
	*/
	public static List<ArrayList<Word>> WordTagger(String text, String lang) throws IOException {
		
		List<ArrayList<Word>> taggedSentences = new ArrayList<ArrayList<Word>>();
		List<Word> taggedWords;

		
		String[] sentences = sentenceDetect(text, lang);
	
		for (String sentence : sentences) {	

			taggedWords = new ArrayList<Word>();
			
			InputStream partOfSpeachModel = Structure.class.getClassLoader().getResourceAsStream("openNLPModels/"+ lang +"/"+ lang +"-pos.bin");

			POSModel posModel = new POSModel(partOfSpeachModel);
			POSTaggerME tagger = new POSTaggerME(posModel);
			
			String[] whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
			String[] tags = tagger.tag(whitespaceTokenizerLine);
			
			int i = 0;
			for (String token : whitespaceTokenizerLine) {
				Word word = new Word();
				word.setId(i);
				word.setWord(token);
				word.setTag(tags[i]);
				
				
				taggedWords.add(word);
				
				i++;
			}
			
			taggedSentences.add((ArrayList<Word>) taggedWords);
		}
	
		return taggedSentences;

	}
	
	/**
	 * this function return an array of sentences of a given text 
	 * 
	 * @param text
	 * @return List of sentences
	 */
	 private static String[] sentenceDetect(String text, String lang) throws IOException {

		InputStream frSentenceModel = Structure.class.getClassLoader().getResourceAsStream("openNLPModels/"+ lang +"/"+ lang +"-sent.bin");

		SentenceModel sentModel = new SentenceModel(frSentenceModel);
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentModel);
		
		String[] sentences = sentenceDetector.sentDetect(text);
		
		frSentenceModel.close();
		
		return sentences;
	}

	
}
