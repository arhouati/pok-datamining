package pok.algorithm.dictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import pok.algorithm.TextScorable;
import pok.algorithm.txt.process.CoreNlpTools;
import pok.algorithm.txt.process.Text;
import pok.algorithm.txt.process.Word;

public class DictionaryScoring implements TextScorable{
	
	final static Logger logger = Logger.getLogger(Text.class);
	
	// TODO handle increment and decrement proposition
	public int getContentScore( List<ArrayList<Word>> formatedText ) throws IOException {
		
		DictionaryTagger dictionary = DictionaryTagger.getInstance();
		for(ArrayList<Word> sentences : formatedText)
		{
			for(Word word : sentences){
				
				word = dictionary.sentimentTagger( word );
			}
		}
		
		CoreNlpTools nlpTools = CoreNlpTools.getInstance();
	 
		List<SemanticGraph> dependencieText =  nlpTools.getDependecyGraph();

		int score = 0;
		int i = 0;
		
		for(SemanticGraph dependencySent : dependencieText){
						
			ArrayList<Word> sentence = formatedText.get(i);
			Collection<IndexedWord> roots = dependencySent.getRoots();
			
			for(IndexedWord root : roots){
				score += getDeepScore( root, dependencySent, sentence );
			}
			
			i++;
		}

		return score;
	}
	
	private static int getDeepScore( IndexedWord word, SemanticGraph dependencySent, ArrayList<Word> sentence ){
		
		int score = 0;
		int deepScore = 0;
		
		score = getScoreWord( sentence.get(word.index()-1), sentence);
						
		for(IndexedWord child : dependencySent.getChildren( word ) ) {
			
				int chlidScore = getDeepScore( child, dependencySent, sentence );
				
				switch ( child.tag() ) {
				
				case "V": // verb
				case "VIMP": // imperative verb
				case "VPP": // past participle verb
				case "VPR":
				case "VINF": // infinitive verb
					
					deepScore =  chlidScore != 0 ? ( deepScore != 0 ? chlidScore * deepScore : chlidScore ) : deepScore ;
					break;
				
				default:
					deepScore += chlidScore;
					
				}
		}

		switch ( word.tag() ) {
		
			case "V": // verb
			case "VIMP": // imperative verb
			case "VPP": // past participle verb
			case "VPR":
			case "VINF": // infinitive verb
				
				score =  ( score ==-1 && deepScore == 1) || ( score == 1 && deepScore == -1)  ? ( score != 0 ? deepScore * score : deepScore ) :  score ;
				break;
				
			case "NC": // noun											
			case "ADJ":
			case "ADV":
				
				score =  ( score ==-1 && deepScore == 1) || ( score == 1 && deepScore == -1)  ? deepScore * score : deepScore + score ;
				break;
				
			default :
	
				score += deepScore;
				break;
		}
		
		return score;
	}

	private static int getScoreWord( Word word, ArrayList<Word> sentence  ){		
				
		Word prevWord = ! isFirstWord(word, sentence) ? sentence.get( word.getId()-1 ) : new Word();
		Word nextWord = ! isLastWord(word, sentence) ? sentence.get( word.getId()+1 ) : new Word();
		
		switch ( word.getTag() ) {
				
			case "V": // verb
			case "VIMP": // imperative verb
			case "VPP": // past participle verb
			case "VPR":
			case "VINF": // infinitive verb

				if ( "negationAV".equals( prevWord.getType() )
						|| "negationAP".equals( nextWord.getType() )) {	
					return word.getScore() != 0 ? word.getScore() * -1 : -1;
				}else{
					return word.getScore() != 0 ? word.getScore() : 1;
				}
			
			case "NC": // noun											
			case "ADJ":
			case "ADV":
			default :

				return word.getScore();
		
		}
								
	}
	
	private static boolean isFirstWord(Word word, ArrayList<Word> sentence){
		
		return word.getId().equals(0)? true : false; 
	}
	
	private static boolean isLastWord(Word word, ArrayList<Word> sentence){
		
		return word.getId().equals( sentence.size() - 1) ? true : false; 

	}
	
}
