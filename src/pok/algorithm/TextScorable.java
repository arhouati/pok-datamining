package pok.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pok.algorithm.txt.process.Word;

public interface TextScorable {

	public int getContentScore( List<ArrayList<Word>> words) throws IOException;

}
