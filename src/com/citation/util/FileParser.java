package com.citation.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.citation.datastructures.Edge;
import com.citation.datastructures.Graph;
import com.citation.datastructures.Node;
import com.citation.datastructures.PageRankImportance;

public class FileParser {

	/** Graph that contains the nodes and the citation edges for each node. */
	private static Graph graph = new Graph();

	/** Contains the ID of all the topics available in the data set. */
	private static List<String> topicIds = new ArrayList<String>();

	/** Contains the ID of all the topics related to each paper ID. */
	private static Map<String, List<String>> paperRelatedTopics = new HashMap<String, List<String>>();

	public static Graph getGraph() {
		return graph;
	}

	/**
	 * Parses the topic file. For every record, create a new node in the graph.
	 * The node id will be the paper id. The node is also associated with a set
	 * of probabilities for each topic associated with the paper.
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void parseTopicalImportance(String filePath) throws Exception {

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String input = null;
		Node newNode = null;

		try {
			while ((input = br.readLine()) != null) {

				String[] data = input.split("[,]");

				newNode = new Node(data[0]);
				newNode.setNumber(Integer.parseInt(data[1]));
				newNode.setTopicProbability(getProbablitiesForTopicImportance(
						data[0], data[2]));

				/*
				 * System.out.println("Paper ID : " + newNode.getPaperId() +
				 * " . Probability List Size " +
				 * newNode.getTopicProbability().size());
				 */

				if (newNode.getTopicProbability().size() == 0) {
					System.out
							.println("Topic values not available for paper --> "
									+ newNode.getPaperId());
				}

				graph.addNode(newNode);
			}
		} finally {
			br.close();
		}

		System.out.println("Total number of papers "
				+ graph.getNodeList().size());

		System.out.println("--------");

	}

	/**
	 * Parses the probability data field in topic and citation file. It
	 * considers the probabilities that are associated with the topic related to
	 * paper. Other probabilities are discarded.
	 * 
	 * @param paperId
	 * @param data
	 * @return
	 */
	private static Map<String, Double> getProbablitiesForTopicImportance(
			String paperId, String data) {

		Map<String, Double> topicProbablilityMap = new HashMap<String, Double>();
		List<String> topicsRelatedToPaper = paperRelatedTopics.get(paperId);

		String[] probabilities = data.split("[ ]");
		Double probabilitySum = 0.0;

		if (topicsRelatedToPaper != null) {
			for (int iIndex = 0; iIndex < topicIds.size(); iIndex++) {
				if (topicsRelatedToPaper.contains(topicIds.get(iIndex))) {
					topicProbablilityMap.put(topicIds.get(iIndex),
							(Double.parseDouble(probabilities[iIndex])));
					probabilitySum = probabilitySum
							+ Double.parseDouble(probabilities[iIndex]);
				}
			}

			Set<String> topicIdListForPaper = topicProbablilityMap.keySet();

			for (String topic : topicIdListForPaper) {
				topicProbablilityMap.put(topic, topicProbablilityMap.get(topic)
						/ probabilitySum);
			}
		}

		return topicProbablilityMap;
	}

	/**
	 * Parses the citation file. For every record, create a new edge in the
	 * graph. The node id will be the paper id. The node is also associated with
	 * a set of probabilities for each topic associated with the paper.
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void parseCitationImportance(String filePath)
			throws Exception {

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String input = null;

		Edge newEdge = null;
		List<Edge> edgeList = new ArrayList<Edge>();
		List<String> citingPapers = new ArrayList<String>();

		try {
			while ((input = br.readLine()) != null) {

				String[] data = input.split("[,]");

				newEdge = new Edge();
				newEdge.setCitingPaper(graph.getNode(data[0]));
				if (!(citingPapers.contains(data[0]))) {
					citingPapers.add(data[0]);
				}
				newEdge.setCitedPaper(graph.getNode(data[1]));
				newEdge.setCitationTopicProbability(getProbablitiesForTopicImportance(
						data[0], data[2]));
				edgeList.add(newEdge);
			}
		} finally {
			br.close();
		}

		for (String paper : citingPapers) {

			/* System.out.println("Citing Paper : " + paper); */
			Node citingPaper = graph.getNode(paper);

			if (citingPaper != null) {
				Map<Node, List<Edge>> citingEdges = new HashMap<Node, List<Edge>>();

				for (Edge e : edgeList) {
					if (e.getCitingPaper() == citingPaper) {

						List<Edge> edgeListForCitedPaper = citingEdges.get(e
								.getCitedPaper());

						if (edgeListForCitedPaper != null) {
							edgeListForCitedPaper.add(e);
						} else {
							edgeListForCitedPaper = new ArrayList<Edge>();
							edgeListForCitedPaper.add(e);
							citingEdges.put(e.getCitedPaper(),
									edgeListForCitedPaper);
						}

					}
				}
				citingPaper.setCitationEdgesMap(citingEdges);
			} else {
				System.out
						.println("Citation with citing paper "
								+ paper
								+ " exists. But no corresponding node in topic importance.");
			}
		}

	}

	/**
	 * Parses the PageRankImportance for each of the topic file. For every
	 * topic, the page rank importance of each paper is added to the graph.
	 * 
	 * @param folderPath
	 * @throws Exception
	 */
	public static void parsePageRankImportance(String folderPath)
			throws Exception {

		PageRankImportance pageRank = new PageRankImportance();
		File directory = new File(folderPath);

		if (!(directory.isDirectory())) {
			throw new Exception("Provide folder path for page rank files.");
		}

		File pageRankFiles[] = directory.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".csv");
			}
		});

		BufferedReader br = null;
		String topicId = null;

		for (File f : pageRankFiles) {

			br = new BufferedReader(new FileReader(f));

			topicId = getTopicIdFromFileNameForPageRank(f.getName());

			String input = null;

			try {
				while ((input = br.readLine()) != null) {

					String[] data = input.split("[,]");

					pageRank.addTopicPaperPageRankProbability(topicId, data[0],
							Double.parseDouble(data[1]));
				}
			} finally {
				br.close();
			}

		}

		graph.setPageRankImportance(pageRank);

	}

	/**
	 * Returns the topic list for which the matrix needs to be constructed.
	 * 
	 * @param filepath
	 * @return list of topics.
	 * @throws Exception
	 */
	public static List<String> parseTopicFile(String filepath) throws Exception {

		List<String> topicList = new ArrayList<String>();

		File f = new File(filepath);
		BufferedReader br = new BufferedReader(new FileReader(f));

		String input = null;

		try {
			while ((input = br.readLine()) != null) {

				String[] data = input.split("[,]");

				for (int iIndex = 0; iIndex < data.length; iIndex++) {
					topicList.add(data[iIndex]);
				}
			}
		} finally {
			br.close();
		}

		return topicList;

	}

	/**
	 * Returns the paper id list for which the matrix needs to be constructed.
	 * 
	 * @param filepath
	 * @return list of paper ids.
	 * @throws Exception
	 */
	public static List<String> parsePaperIdFile(String filepath)
			throws Exception {

		List<String> paperList = new ArrayList<String>();

		File f = new File(filepath);
		BufferedReader br = new BufferedReader(new FileReader(f));

		String input = null;

		try {
			while ((input = br.readLine()) != null) {

				String[] data = input.split("[,]");

				for (int iIndex = 0; iIndex < data.length; iIndex++) {
					paperList.add(data[iIndex]);
				}
			}
		} finally {
			br.close();
		}

		return paperList;

	}

	/**
	 * Parses the topic name , id data set and creates a list of topic id's.
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void parseTopicId(String filePath) throws Exception {

		File f = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(f));

		String input = null;

		try {
			while ((input = br.readLine()) != null) {

				String[] data = input.split("[,]");

				topicIds.add(data[1]);

			}
		} finally {
			br.close();
		}
	}

	/**
	 * Parses the paper id and related topics data set and creates a map.
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void parsePaperTopicsFile(String filePath) throws Exception {

		List<String> paperTopicIds = null;

		File f = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(f));

		String input = null;

		try {

			while ((input = br.readLine()) != null) {

				paperTopicIds = new ArrayList<String>();

				String[] data = input.split("[,]");

				String topicIds[] = data[1].split("[ ]");

				for (String topicId : topicIds) {
					paperTopicIds.add(topicId);
				}

				paperRelatedTopics.put(data[0], paperTopicIds);

			}
		} finally {
			br.close();
		}

		System.out.println("paperRelatedTopics count : "
				+ paperRelatedTopics.size());

	}

	private static String getTopicIdFromFileNameForPageRank(String fileName) {

		return fileName.substring(fileName.indexOf('k') + 1,
				fileName.length() - 4);

	}

	public static Graph loadData(Properties properties) throws Exception {

		parseTopicId(properties.getProperty("TOPIC_ID_FILE"));

		parsePaperTopicsFile(properties.getProperty("PAPER_TOPIC_KEYWORD_FILE"));

		parseTopicalImportance(properties
				.getProperty("PAPER_TOPIC_DISTRIBUTION_FILE"));

		parseCitationImportance(properties
				.getProperty("CITATION_TOPIC_DISTRIBUTION_FILE"));

		parsePageRankImportance(properties
				.getProperty("PAGE_RANK_IMPORTANCE_FOLDER"));

		return graph;

	}

}
