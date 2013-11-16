package com.citation.construct;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.citation.datastructures.Edge;
import com.citation.datastructures.Graph;
import com.citation.datastructures.Node;

public class ProcessTopicTopicProbabilityFile {

	public double process(Graph graph, String topic1, String topic2) {

		double probability = 0.0;

		// Take every node in the graph.
		// Check if the node's topic probability for topic 1 is > 0. This
		// implies the node is in the topic plane of topic 1.
		// Get all the citation edges for node.
		// For every node involved in the citation,
		// Check if the cited node's topic probability for topic 2 is > 0. This
		// implies that the cited node is in the topic plane of topic 2.
		// Get all the edges from the citing node and cited node.
		// For each edge
		// get the topic probability of the cited topic and add that to overall
		// probability.

		// For each edge sum the probability.

		Map<String, Node> nodeList = graph.getNodeList();

		Set<String> nodeNames = nodeList.keySet();

		// Taking all nodes from the graph

		for (String node : nodeNames) {

			Node n = nodeList.get(node);

			// checking if the node is present in topic plane

			if ((n.getTopicProbability().get(topic1) != null)
					&& (n.getTopicProbability().get(topic1) > 0)) {

				if (n.getCitationEdgesMap() == null)
					continue;

				Map<Node, List<Edge>> citationEdgeMap = n.getCitationEdgesMap();

				Set<Node> citedNodesList = citationEdgeMap.keySet();

				for (Node citedNode : citedNodesList) {
					
					if(citedNode != null){
						System.out.println("cited node : " + citedNode.getPaperId());
						
						if ((citedNode.getTopicProbability().get(topic2) == null)
								|| (citedNode.getTopicProbability().get(topic2) <= 0)) {
							continue;
						}

						List<Edge> citationEdgesForNode = n.getCitationEdgesMap()
								.get(citedNode);

						if (citationEdgesForNode == null)
							continue;

						for (Edge e : citationEdgesForNode) {
							double impact = calculateCitationImpact(graph, topic1,
									citedNode.getPaperId());
							// What if citation is not contributing to topic 2
							probability = probability
									+ e.getCitationTopicProbability().get(topic1)
									* impact;
						}
					}					
				}
			}
		}

		return probability;

	}

	private double calculateCitationImpact(Graph graph, String topicId,
			String paperId) {

		return graph.getPageRankImportance().getProbability(topicId, paperId);

	}
}
