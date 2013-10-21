package com.citation.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.citation.datastructures.Edge;
import com.citation.datastructures.Graph;
import com.citation.datastructures.Node;
import com.citation.datastructures.PageRankImportance;

public class FileParser {

	private static Graph graph = new Graph();

	public static Graph getGraph() {
		return graph;
	}

	public static void parseTopicalImportance(String filePath) throws Exception {

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String input = null;
		Node newNode = null;

		try {
			while ((input = br.readLine()) != null) {

				String[] data = input.split("[,]");

				newNode = new Node(data[0]);
				newNode.setNumber(Integer.parseInt(data[1]));
				newNode.setTopicProbability(getProbablitiesForTopicImportance(data[2]));
				graph.addNode(newNode);
			}
		} finally {
			br.close();
		}

	}

	private static Map<String, Double> getProbablitiesForTopicImportance(
			String data) {

		Map<String, Double> topicProbablilityMap = new HashMap<String, Double>();

		String[] probabilities = data.split("[ ]");

		int i = 0;

		for (String probability : probabilities) {
			topicProbablilityMap.put("topic" + i++,
					(Double.parseDouble(probability) / Integer.MAX_VALUE));
		}

		return topicProbablilityMap;
	}

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
				newEdge.setCitationTopicProbability(getProbablitiesForTopicImportance(data[2]));
				edgeList.add(newEdge);
			}
		} finally {
			br.close();
		}

		for (String paper : citingPapers) {
			Node citingPaper = graph.getNode(paper);
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

		}

	}

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

	private static String getTopicIdFromFileNameForPageRank(String fileName) {

		return fileName.substring(fileName.indexOf('k') + 1,
				fileName.length() - 4);

	}

}
