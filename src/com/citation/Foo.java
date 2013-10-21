package com.citation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.citation.construct.ProcessPaperTopicProbability;
import com.citation.construct.ProcessTopicTopicProbability;
import com.citation.datastructures.CitationImportance;
import com.citation.datastructures.Edge;
import com.citation.datastructures.Graph;
import com.citation.datastructures.Node;
import com.citation.datastructures.PageRankImportance;
import com.citation.datastructures.TopicImportance;
import com.citation.util.FileParser;

public class Foo {

	public static void displayGraph(Graph graph) {

		try {

			Map<String, Node> nodeList = graph.getNodeList();
			Set<String> nodeNames = nodeList.keySet();

			System.out.println("Node List ......");

			for (String name : nodeNames) {

				Node n = nodeList.get(name);

				System.out.println("Paper ID : " + n.getPaperId());

				Map<String, Double> topicProbability = n.getTopicProbability();

				Set<String> topics = topicProbability.keySet();

				for (String topic : topics) {
					System.out.println(topic + " --> "
							+ topicProbability.get(topic));
				}

				Map<Node, List<Edge>> edgeCitationsMap = n
						.getCitationEdgesMap();

				if (edgeCitationsMap != null) {
					Set<Node> citedPapers = edgeCitationsMap.keySet();

					for (Node cited : citedPapers) {

						List<Edge> edgeList = edgeCitationsMap.get(cited);

						for (Edge e : edgeList) {

							System.out.println(e.getCitingPaper().getPaperId()
									+ " --> " + e.getCitedPaper().getPaperId());

							Map<String, Double> probability = e
									.getCitationTopicProbability();
							Set<String> edgeTopics = probability.keySet();
							for (String topic : edgeTopics) {
								System.out.println(topic + " --> "
										+ probability.get(topic));
							}
						}
					}
				}

				System.out
						.println("---------------------------------------------");

			}

			System.out.println("Nodes Contributing To each Topic ....");

			Map<String, List<Node>> nodeContributingToTopic = graph
					.getTopicContributingNodesMap();

			Set<String> topicNodeMap = nodeContributingToTopic.keySet();

			for (String topic : topicNodeMap) {
				System.out.println("Topic : " + topic);

				List<Node> nodes = nodeContributingToTopic.get(topic);

				for (Node n : nodes) {
					System.out.println(n.getPaperId());
				}
				System.out.println("-------------------------------");
			}

			System.out.println("Topic Importance Records Size : "
					+ nodeList.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		ProcessPaperTopicProbability paperTopicProbability = new ProcessPaperTopicProbability();
		ProcessTopicTopicProbability topicTopicProbability = new ProcessTopicTopicProbability();

		FileParser.parseTopicalImportance(args[0]);

		FileParser.parseCitationImportance(args[1]);

		FileParser.parsePageRankImportance(args[2]);

		List<String> topicList = FileParser.parseTopicFile("data/topics");
		List<String> paperList = FileParser.parseTopicFile("data/papers");

		// displayGraph(FileParser.getGraph());

		if (paperList.size() > 0) {
			// Paper topic matrix .
			for (int iIndex = 0; iIndex < paperList.size(); iIndex++) {
				for (int jIndex = 0; jIndex < topicList.size(); jIndex++) {
					System.out.println(paperList.get(iIndex)
							+ " -- "
							+ topicList.get(jIndex)
							+ " , "
							+ paperTopicProbability.process(
									FileParser.getGraph(),
									paperList.get(iIndex),
									topicList.get(jIndex)));
				}
			}

		}

		// topic topic matrix

		for (int iIndex = 0; iIndex < topicList.size(); iIndex++) {
			for (int jIndex = 0; jIndex < topicList.size(); jIndex++) {
				if (iIndex != jIndex) {
					System.out.println(topicList.get(jIndex)
							+ " -- "
							+ topicList.get(iIndex)
							+ " -->"
							+ topicTopicProbability.process(
									FileParser.getGraph(),
									topicList.get(jIndex),
									topicList.get(iIndex)));
					// call to calculate the topic topic probability.
					// save the results and project it in a html file.
				}
			}
		}

		// System.out.println(paperTopicProbability.process(FileParser.getGraph(),
		// "R_5673", "topic0"));

		/*
		 * System.out.println("---------"); displayCitationImportance(args[1]);
		 * System.out.println("---------"); displayPageRankImportance(args[2]);
		 */

	}
}
