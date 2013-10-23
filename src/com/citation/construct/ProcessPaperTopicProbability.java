package com.citation.construct;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.citation.datastructures.Edge;
import com.citation.datastructures.Graph;
import com.citation.datastructures.Node;

public class ProcessPaperTopicProbability {

	public double process(Graph graph, String paperId, String topicId) {
		double probability = 0.0;

		// for all papers in topic plane,
		// check if the there is an citation edge from paper to paperID
		// if the edge is also in topic plane just add the probability
		// if the edge is between planes then calculate the Impact the cited
		// paper makes on the citing topic. Multiply this impact to the edge
		// citation probability and add it

		Map<String, Node> nodeList = graph.getNodeList();

		Set<String> nodeNames = nodeList.keySet();

		// Taking all nodes from the graph

		for (String node : nodeNames) {
			if (!(node.equals(paperId))) {

				Node n = nodeList.get(node);

				// checking if the node is present in topic plane

				if ((n.getTopicProbability().get(topicId) !=null ) && (n.getTopicProbability().get(topicId) > 0)) {

					if(n.getCitationEdgesMap() == null) continue;
					
					List<Edge> citationEdgesForNode = n.getCitationEdgesMap()
							.get(graph.getNode(paperId));
					
					if(citationEdgesForNode == null) continue;

					for (Edge e : citationEdgesForNode) {

						Map<String, Double> citationTopicProbability = e
								.getCitationTopicProbability();

						Set<String> topicProbability = citationTopicProbability
								.keySet();

						for (String topic : topicProbability) {
							if (topic.equals(topicId)) {
								probability = probability
										+ citationTopicProbability.get(topicId);
							} else {
								
								// A bit unclear. Clarify with professor.
								
								
								// Calculate impact.
								
								double impact = calculateCitationImpact(graph,topicId,paperId);
								probability = probability
										+ citationTopicProbability.get(topic)
										* impact;
							}
						}

					}
				}
			}
		}

		return probability;

	}
	
	private double calculateCitationImpact(Graph graph,String topicId, String paperId){
		
		return graph.getPageRankImportance().getProbability(topicId, paperId);
		
	}
	
}
