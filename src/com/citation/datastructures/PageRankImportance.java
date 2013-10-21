package com.citation.datastructures;

import java.util.HashMap;
import java.util.Map;

public class PageRankImportance {

	private Map<String, Map<String, Double>> paperPageRankProbability;

	public Map<String, Map<String, Double>> getPaperPageRankProbability() {
		return paperPageRankProbability;
	}

	public PageRankImportance() {
		paperPageRankProbability = new HashMap<String, Map<String, Double>>();
	}

	public Double getProbability(String topicId, String paperId) {
		return paperPageRankProbability.get(topicId).get(paperId);
	}

	public void addTopicPaperPageRankProbability(String topicId,
			String paperId, Double probability) {

		Map<String, Double> topicPaperProbability = null;

		if (paperPageRankProbability.get(topicId) != null) {
			topicPaperProbability = paperPageRankProbability.get(topicId);
		} else {
			topicPaperProbability = new HashMap<String, Double>();
			paperPageRankProbability.put(topicId, topicPaperProbability);
		}

		topicPaperProbability.put(paperId, probability);
	}

}
