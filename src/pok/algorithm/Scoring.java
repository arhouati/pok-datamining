package pok.algorithm;

import java.util.ArrayList;
import java.util.List;

import pok.algorithm.txt.process.Word;

public class Scoring {
	
	// TODO refactoring algorythm
	public static int getContentScore( List<ArrayList<Word>> words) {
		
		int score = 0;
		
		for(ArrayList<Word> sentence : words)
		{
			int scoreSentence = 0;
			
			System.out.println("start Socring Sentence ----------------");

			
			for(Word word : sentence){
				
				if ( isFirstWord(word, sentence) ) {
					
					if (word.getSentiment().equals("positive")) {
						scoreSentence += 1;
					} else if (word.getSentiment().equals("negative")) {
						scoreSentence -= 1;
					} 
					
				}else if ( isLastWord( word, sentence ) ) {
					
					Word prevWord = sentence.get( word.getId()-1);
					
					if (word.getSentiment().equals("positive")) {
						
						if (prevWord.isIncrement()) {
							scoreSentence += 2;
						} else if (prevWord.isDecrement()) {
							scoreSentence += 0;
						} 
						else if ("negationAV".equals( prevWord.getType() )) {
							scoreSentence -= 1;
						} else {
							scoreSentence += 1;
						}
					}
					if (word.getSentiment().equals("negative")) {
						if (prevWord.isIncrement()) {
							scoreSentence -= 2;
						} else if (prevWord.isDecrement()) {
							scoreSentence += 0;
						}
						

						else if ("negationAV".equals( prevWord.getType())) {
							scoreSentence += 1;
						} else {
							scoreSentence -= 1;
						}
					} 
				}else {
					
					Word prevWord = sentence.get( word.getId()-1);
					Word nextWord = sentence.get( word.getId()+1);

					if (word.getSentiment().equals("positive")) {
						
						if (prevWord.isIncrement()) {
							scoreSentence += 2;
						} else if (prevWord.isDecrement()) {
							scoreSentence += 0;
						} 
						else if (nextWord.isIncrement()) {
							scoreSentence += 2;
						} else if (nextWord.isDecrement()) {
							scoreSentence += 0;
						}
						else if ("negationAV".equals( prevWord.getType() )
								| "negationAP".equals( nextWord.getType() )) {
							scoreSentence -= 1;
						} else {
							scoreSentence += 1;
						}
					}
					if (word.getSentiment().equals("negative")) {
						
						
						if (prevWord.isIncrement()) {
							scoreSentence -= 2;
						} else if (prevWord.isDecrement()) {
							scoreSentence += 0;
						}
						else if (nextWord.isIncrement()) {
							scoreSentence -= 2;
						} else if (nextWord.isDecrement()) {
							scoreSentence += 0;
						}

						else if ("negationAV".equals( prevWord.getType())
								| "negationAP".equals( nextWord.getType() )) {

							scoreSentence += 1;
						} else {
							scoreSentence -= 1;
						}
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
