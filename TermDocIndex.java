package newIndex;

import java.util.ArrayList;

public class TermDocIndex {

	
	private ArrayList<Integer> positions;
	private   Integer termFrequency;
	private   Float tfidf;
	
	public TermDocIndex(){
		this.termFrequency = 0;
		this.tfidf =0.0f;
		positions = new ArrayList<Integer>();
		if(!this.positions.isEmpty())
			this.positions.clear();
	}
	
	public void termFreqIncrease(){
		this.termFrequency += 1;
	}
	public Integer getTermFrequency(){
		return this.termFrequency;
	}
	public void add_tfidf(Float TFIDF){
		this.tfidf=TFIDF;
	}
	public Float get_tfidf(){
		return this.tfidf;
	}
	public void pushPosition(Integer pos){
		this.positions.add(pos);
	}
	
	
	public void clear(){
		this.termFrequency = 0;
		this.tfidf =0.0f;
		if(!this.positions.isEmpty())
			this.positions.clear();
		
	}
	
	public ArrayList<Integer> getPositions(){
		
		return this.positions;
	}
}
