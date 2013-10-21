package com.citation.datastructures;

import java.util.Map;

public class Edge {

	private Node citingPaper;

	private Node citedPaper;

	private Map<String, Double> citationTopicProbability;

	public Node getCitingPaper() {
		return citingPaper;
	}

	public void setCitingPaper(Node citingPaper) {
		this.citingPaper = citingPaper;
	}

	public Node getCitedPaper() {
		return citedPaper;
	}

	public void setCitedPaper(Node citedPaper) {
		this.citedPaper = citedPaper;
	}

	public Map<String, Double> getCitationTopicProbability() {
		return citationTopicProbability;
	}

	public void setCitationTopicProbability(
			Map<String, Double> citationTopicProbability) {
		this.citationTopicProbability = citationTopicProbability;
	}

}
