package com.citation.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {

	private Map<String, Node> nodeList;

	private Map<String, List<Node>> topicContributingNodesMap;
	
	private PageRankImportance pageRankImportance;

	public Graph() {
		nodeList = new HashMap<String, Node>();
		topicContributingNodesMap = new HashMap<String, List<Node>>();
	}

	public Map<String, Node> getNodeList() {
		return nodeList;
	}
	
	public Node getNode(String paperId) {
		return nodeList.get(paperId);
	}

	public Map<String, List<Node>> getTopicContributingNodesMap() {
		return topicContributingNodesMap;
	}

	public void addNode(Node n) {

		nodeList.put(n.getPaperId(), n);

		Map<String, Double> topicProbability = n.getTopicProbability();

		Set<String> topics = topicProbability.keySet();

		for (String topic : topics) {

			if (topicProbability.get(topic) > 0) {
				List<Node> nodeListContributingToTopic = topicContributingNodesMap
						.get(topic);

				if (nodeListContributingToTopic != null) {

					if (!(nodeListContributingToTopic.contains(n))) {
						nodeListContributingToTopic.add(n);
					}

				} else {

					nodeListContributingToTopic = new ArrayList<Node>();
					nodeListContributingToTopic.add(n);
					topicContributingNodesMap.put(topic,
							nodeListContributingToTopic);
				}
			}
		}
	}
	
	public PageRankImportance getPageRankImportance() {
		return pageRankImportance;
	}

	public void setPageRankImportance(PageRankImportance pageRankImportance) {
		this.pageRankImportance = pageRankImportance;
	}
	
}
