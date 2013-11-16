package com.citation.datastructures;

public class CitationTopicProbability {

	private String citedPaperId;

	private String citingPaperId;

	private int topicId;

	private double probability;

	public String getCitedPaperId() {
		return citedPaperId;
	}

	public void setCitedPaperId(String citedPaperId) {
		this.citedPaperId = citedPaperId;
	}

	public String getCitingPaperId() {
		return citingPaperId;
	}

	public void setCitingPaperId(String citingPaperId) {
		this.citingPaperId = citingPaperId;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

}
