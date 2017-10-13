package pok.algorithm.txt.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;


/**
* <h1>Structure</h1>
* <p>This class analyse text, and detect sentences and words. and for tag every word.</p>
*
* @author  Abdelkader Rhouati (arhouati)
* @version 1.0
* @since   2017-05-26
*/
public class Structure {

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
		List<Word> taggedWords = null;
		
		// build pipeline
		StanfordCoreNLP pipeline = new StanfordCoreNLP(
			PropertiesUtils.asProperties(
				"annotators", "tokenize,ssplit,pos,lemma,parse",
				"ssplit.isOneSentence", "true",
				"parse.model", "edu/stanford/nlp/models/srparser/frenchSR.ser.gz",
				"pos.model", "edu/stanford/nlp/models/pos-tagger/french/french.tagger",
				"tokenize.language", "fr"));

		Annotation document = new Annotation( text );

		pipeline.annotate(document);
		
		List<CoreMap> sentencesParser = document.get( SentencesAnnotation.class );

		for(CoreMap sentenceParser: sentencesParser) {

			int i = 0;
			
			taggedWords = new ArrayList<Word>();

			for (CoreLabel token: sentenceParser.get( TokensAnnotation.class )) {
			    
			    Word word = new Word();
				word.setId(i);
				word.setWord( token.get(TextAnnotation.class) );
				word.setTag( token.get(PartOfSpeechAnnotation.class) );
				
				taggedWords.add(word);
				
				i++;
			
			}
			
			// this is the parse tree of the current sentence
			// Tree tree = sentenceParser.get(TreeAnnotation.class);
			// tree.pennPrint();
		}

		taggedSentences.add((ArrayList<Word>) taggedWords);

		return taggedSentences;
	}
}
