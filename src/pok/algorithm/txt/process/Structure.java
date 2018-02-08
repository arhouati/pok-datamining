package pok.algorithm.txt.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphFormatter;
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
	
	static private Annotation document;
	static private List<CoreMap> sentencesParser;

	public static void initParser(String text, String lang) {
		
		// build pipeline
		StanfordCoreNLP pipeline = new StanfordCoreNLP(
			PropertiesUtils.asProperties(
				"annotators", "tokenize,ssplit,pos,lemma,parse,depparse",
				"ssplit.isOneSentence", "true",
				"parse.model", "edu/stanford/nlp/models/srparser/frenchSR.ser.gz",
				"pos.model", "edu/stanford/nlp/models/pos-tagger/french/french.tagger",
				"depparse.model", "edu/stanford/nlp/models/parser/nndep/UD_French.gz",
				"tokenize.language", "fr"));

		document = new Annotation( text );
		
		pipeline.annotate(document);
	}
	
	public static List<SemanticGraph> getDependecyGraph(){
		
		// this is the parse tree of the current sentence
		// Tree tree = sentenceParser.get(TreeAnnotation.class);
		// tree.pennPrint();
		
		List<SemanticGraph> dependencieText = new ArrayList<SemanticGraph>();

		// this is the Stanford dependency graph of the current sentence
		for(CoreMap sentenceParser: sentencesParser) {
			SemanticGraph dependencieSent = sentenceParser.get( EnhancedPlusPlusDependenciesAnnotation.class );
		 	
			dependencieSent.prettyPrint();
		 	
			dependencieText.add(dependencieSent);

		}
		
		return dependencieText;
	}
	
	
	/**
	* This method takes as a parameter the text that will be analyzed :
	* 	1/ Detect sentences of the text
	* 	2/ For each sentence tag each word as noun, verb, etc...
	* 
	* @param text
	* @return 
	*/
	public static List<ArrayList<Word>> WordTagger() throws IOException {
		
		List<ArrayList<Word>> taggedSentences = new ArrayList<ArrayList<Word>>();
		List<Word> taggedWords = null;
		
		sentencesParser = document.get( SentencesAnnotation.class );

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
			
			taggedSentences.add((ArrayList<Word>) taggedWords);
			
			// Getting Tree parser of sentence
			/*InputStream parserModelStream = Structure.class.getClassLoader().getResourceAsStream("openNLPModels/"+ lang +"/"+ lang +"-parser-chunking.bin");
			
			ParserModel parserModel = new ParserModel( parserModelStream );
			Parser parser = ParserFactory.create(parserModel);
			Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);
			topParses[0].show();*/
			}
			

		}

		taggedSentences.add((ArrayList<Word>) taggedWords);

		return taggedSentences;
	}
}
