package com.citation.datastructures;

import java.util.List;
import java.util.Map;

public class Node {

	private String paperId;

	private int number;

	private Map<String, Double> topicProbability;

	private Map<Node, List<Edge>> citationEdgesMap;

	public Node(String paperId) {
		this.paperId = paperId;
	}

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	public Map<String, Double> getTopicProbability() {
		return topicProbability;
	}

	public void setTopicProbability(Map<String, Double> topicProbability) {
		this.topicProbability = topicProbability;
	}

	public Map<Node, List<Edge>> getCitationEdgesMap() {
		return citationEdgesMap;
	}

	public void setCitationEdgesMap(Map<Node, List<Edge>> citationEdgesMap) {
		this.citationEdgesMap = citationEdgesMap;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
