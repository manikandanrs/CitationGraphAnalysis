package com.citation;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.citation.construct.ProcessPaperTopicProbabilityFile;
import com.citation.construct.ProcessTopicTopicProbabilityFile;
import com.citation.util.FileParser;

public class Foo {

	private static String paperTopicOutputFileName = "paper_topic_"
			+ System.currentTimeMillis() + ".csv";

	private static String topicTopicOutputFileName = "topic_topic_"
			+ System.currentTimeMillis() + ".csv";

	public static void main(String[] args) throws Exception {

		ProcessPaperTopicProbabilityFile paperTopicProbability = new ProcessPaperTopicProbabilityFile();
		ProcessTopicTopicProbabilityFile topicTopicProbability = new ProcessTopicTopicProbabilityFile();
		FileWriter paperTopicWriter = new FileWriter(paperTopicOutputFileName);
		FileWriter topicTopicWriter = new FileWriter(topicTopicOutputFileName);
		Map<String, String> topicIdNameMapping = null;
		Map<String, List<String>> paperRelatedTopics = null;

		try {

			Properties properties = new Properties();

			properties.load(new FileInputStream(args[0]));

			FileParser.loadData(properties);

			topicIdNameMapping = FileParser.getTopicIdNameMap();

			paperRelatedTopics = FileParser.getPaperRelatedTopics();

			List<String> topicList = FileParser.parseTopicFile(properties
					.getProperty("TOPICS_LIST"));
			List<String> paperList = FileParser.parsePaperIdFile(properties
					.getProperty("PAPER_LIST"));

			if (paperList.size() > 0) {
				// Paper topic matrix .

				// write topics to file
				paperTopicWriter.append("paper/topic,");
				for (int jIndex = 0; jIndex < topicList.size(); jIndex++) {
					paperTopicWriter.append(topicIdNameMapping.get(topicList
							.get(jIndex)));
					paperTopicWriter.append(",");
				}
				paperTopicWriter.append("\n");

				for (int iIndex = 0; iIndex < paperList.size(); iIndex++) {
					paperTopicWriter.append(paperList.get(iIndex));
					paperTopicWriter.append(",");
					for (int jIndex = 0; jIndex < topicList.size(); jIndex++) {
						Double probability = paperTopicProbability.process(
								FileParser.getGraph(), paperList.get(iIndex),
								topicList.get(jIndex));
						System.out.println(paperList.get(iIndex) + " -- "
								+ topicList.get(jIndex) + " , " + probability);
						paperTopicWriter.append(String.valueOf(probability));
						paperTopicWriter.append(",");
					}
					paperTopicWriter.append("\n");
				}

				paperTopicWriter.append("\n\n\n");

				for (int iIndex = 0; iIndex < paperList.size(); iIndex++) {
					List<String> keywords = paperRelatedTopics.get(paperList
							.get(iIndex));
					paperTopicWriter.append(paperList.get(iIndex));
					paperTopicWriter.append("-->");
					for (String keyword : keywords) {
						paperTopicWriter
								.append(topicIdNameMapping.get(keyword));
						paperTopicWriter.append(":");
					}
					paperTopicWriter.append("\n");
				}
			}

			// topic topic matrix

			topicTopicWriter.append(" ,");
			for (int iIndex = 0; iIndex < topicList.size(); iIndex++) {
				topicTopicWriter.append(topicIdNameMapping.get(topicList
						.get(iIndex)));
				topicTopicWriter.append(",");
			}
			topicTopicWriter.append("\n");
			for (int jIndex = 0; jIndex < topicList.size(); jIndex++) {
				topicTopicWriter.append(topicIdNameMapping.get(topicList
						.get(jIndex)));
				topicTopicWriter.append(",");
				for (int iIndex = 0; iIndex < topicList.size(); iIndex++) {
					double probability = 0.0;
					if (iIndex != jIndex) {
						probability = topicTopicProbability.process(
								FileParser.getGraph(), topicList.get(jIndex),
								topicList.get(iIndex));
						System.out.println(topicList.get(jIndex) + " -- "
								+ topicList.get(iIndex) + " -->" + probability);

						// call to calculate the topic topic probability.
						// save the results and project it in a html file.
					}

					topicTopicWriter.append(String.valueOf(probability));
					topicTopicWriter.append(",");
				}
				topicTopicWriter.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (paperTopicWriter != null) {
				paperTopicWriter.flush();
				paperTopicWriter.close();
			}
			if (topicTopicWriter != null) {
				topicTopicWriter.flush();
				topicTopicWriter.close();
			}
		}

	}
}
