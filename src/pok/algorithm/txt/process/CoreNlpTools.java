package pok.algorithm.txt.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

public class CoreNlpTools { 

	/**
	 * The only instance of CoreNlp 
	 */
	static private CoreNlpTools instance;
	
	private final StanfordCoreNLP pipelineFr;
	private Annotation document;
	private List<CoreMap> sentencesParser;
	
	final static Logger logger = Logger.getLogger(Text.class);

	private CoreNlpTools() throws IOException {
		
		// build pipeline
		pipelineFr = new StanfordCoreNLP(
			PropertiesUtils.asProperties(
				"annotators", "tokenize,ssplit,pos,lemma,parse,depparse",
				"ssplit.isOneSentence", "false",
				"parse.model", "edu/stanford/nlp/models/srparser/frenchSR.ser.gz",
				"pos.model", "edu/stanford/nlp/models/pos-tagger/french/french.tagger",
				"depparse.model", "edu/stanford/nlp/models/parser/nndep/UD_French.gz",
				"tokenize.language", "fr"));
	}
	
	/**
	 * Used to access {@link CoreNlpTools}
	 * 
	 * @return an instance of {@link CoreNlpTools}
	 */
	public static synchronized CoreNlpTools getInstance()
	{
		try
		{
			if (instance == null) instance = new CoreNlpTools();
			return instance;
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not init CoreNlpTools: " + e.getMessage());
		}
	}
	
	
	/**
	* This method takes as a parameter the text that will be analyzed :
	* 	1/ Detect sentences of the text
	* 	2/ For each sentence tag each word as noun, verb, etc...
	* 
	* @param text
	* @return 
	*/
	public List<ArrayList<Word>> wordTagger(String text, String lang) throws IOException {
		
		document = new Annotation( text );
		
		pipelineFr.annotate(document);
		
		List<ArrayList<Word>> taggedSentences = new ArrayList<ArrayList<Word>>();
		List<Word> taggedWords = null;
		
		sentencesParser = document.get( SentencesAnnotation.class );

		for(CoreMap sentenceParser: sentencesParser) {

			// this is the parse tree of the current sentence
			if(logger.isDebugEnabled()){
				Tree tree = (Tree) sentenceParser.get( TreeAnnotation.class );
				tree.pennPrint();
			}
			
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

			taggedSentences.add((ArrayList<Word>) taggedWords);

		}

		return taggedSentences;
	}
	
	
	public List<SemanticGraph> getDependecyGraph(){
		
		List<SemanticGraph> dependencieText = new ArrayList<SemanticGraph>();

		// this is the Stanford dependency graph of the current sentence
		for(CoreMap sentenceParser: sentencesParser) {
			
			SemanticGraph dependencieSent = sentenceParser.get( SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class );
		 	
			// this is the parse tree of the current sentence
			if(logger.isDebugEnabled()){
				dependencieSent.prettyPrint();
			}
			
			dependencieText.add(dependencieSent);
		}
		
		return dependencieText;
	}
}
