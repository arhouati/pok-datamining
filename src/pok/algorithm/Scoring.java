package pok.algorithm;

import java.util.ArrayList;
import java.util.List;

import pok.algorithm.txt.process.Word;

public class Scoring {
	
	// TODO refactoring algorythm
	// TODO handle increment and decrement proposition
	public static int getContentScore( List<ArrayList<Word>> words) {
		
		int score = 0;
		
		for(ArrayList<Word> sentence : words)
		{
			int scoreSentence = 0;
			
			int scoreVerb = 1;
			
			System.out.println("start Socring Sentence ----------------");
			
			for(Word word : sentence){
				
				Word prevWord = ! isFirstWord(word, sentence) ? sentence.get( word.getId()-1 ) : new Word();
				Word nextWord = ! isLastWord(word, sentence) ? sentence.get( word.getId()+1 ) : new Word();

				switch ( word.getTag() ) {
					case "NC": // noun											
					case "ADJ":
					case "ADV":
						if ( !"negationAV".equals( word.getType() )
								&& !"negationAP".equals( word.getType() )
								&& word.getScore() != 0)
							scoreSentence = word.getScore() * scoreVerb;
						else
							scoreSentence += word.getScore();

						break;
					
					case "V": // verb
					case "VIMP": // imperative verb
					case "VPP": // past participle verb
					case "VPR":
					case "VINF": // infinitive verb

						if ( "negationAV".equals( prevWord.getType() )
								|| "negationAP".equals( nextWord.getType() )) {	
							scoreVerb = word.getScore() != 0 ? word.getScore() * -1 * scoreVerb : -1;
						}else{
							scoreVerb = word.getScore() != 0 ? word.getScore() * scoreVerb : 1;
						}
						
					default :
						if ( "negationAV".equals( prevWord.getType() )
								|| "negationAP".equals( nextWord.getType() )) {	
							scoreSentence += word.getScore() != 0 ? word.getScore() * -1 : -1;
						}else{
							scoreSentence += word.getScore();
						}
					
				}
			}
			
			System.out.println("Sentence score :" + scoreSentence);
			
			score += scoreSentence;
		}
		
		
		return score;
	}

	private static boolean isFirstWord(Word word, ArrayList<Word> sentence){
		
		return word.getId().equals(0)? true : false; 
	}
	
	private static boolean isLastWord(Word word, ArrayList<Word> sentence){
		
		return word.getId().equals( sentence.size() - 1) ? true : false; 

	}
	
}
