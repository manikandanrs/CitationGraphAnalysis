package com.citation.datastructures;
import java.util.List;


public class TopicImportance {

	private String topicId;
	
	private int number;
	
	private List<Integer> probabilities;

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<Integer> getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(List<Integer> probabilities) {
		this.probabilities = probabilities;
	}
	
	
}
