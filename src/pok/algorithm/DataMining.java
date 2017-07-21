package pok.algorithm;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

import pok.algorithm.txt.process.DictionaryTagger;
import pok.algorithm.txt.process.Structure;
import pok.algorithm.txt.process.Text;
import pok.algorithm.txt.process.Word;

/**
* <h1>Main Script</h1>
*
* @author  Abdelkader Rhouati (arhouati)
* @version 1.0
* @since   2017-05-26
* 
*/
public class DataMining {

	public static int process(String text, String lang) throws IOException {

		System.out.println("poc :: Score :: text preprocessing");
		System.out.println("poc :: Score :: text :: "+ text +"("+ lang +")");

		text = Text.preprocessing( text, lang );
		
		System.out.println("poc :: Score :: word's text tagging - process of 'POS / Part of Speach' (Noun, Adjectif, verb...)");
		List<ArrayList<Word>> words = Structure.WordTagger(text, lang);

		CSVReader dictionaryFile = new CSVReader(new FileReader("resources/dictionary/dictionary-fr.csv"));
		CSVReader negationFile = new CSVReader(new FileReader("resources/dictionary/negation-fr.csv"));
		
		DictionaryTagger.loadDictionaryFromCSV( dictionaryFile );
		DictionaryTagger.loadNegationFromCSV( negationFile );

		
		for(ArrayList<Word> sentences : words)
		{
			for(Word word : sentences){
				word = DictionaryTagger.sentimentTagger( word );
			}
		}

		for(ArrayList<Word> sentences : words)
		{
			System.out.println("poc :: Score :: start Sentence ----------- ");
			for(Word word : sentences){
				System.out.println("poc :: Score :: word :: "  + word.getId() +" >> " + word.getWord() + " >> " + word.getTag()  +" >> " + word.getSentiment()  +" >> " + word.getType());
			}
			System.out.println("poc :: Score :: end Sentence ----------- ");

		}
		return Scoring.getContentScore( words );

	}
	
	
	
	
}
