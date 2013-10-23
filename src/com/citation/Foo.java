package com.citation;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;
import java.util.Properties;

import com.citation.construct.ProcessPaperTopicProbability;
import com.citation.construct.ProcessTopicTopicProbability;
import com.citation.util.FileParser;

public class Foo {

	private static String paperTopicOutputFileName = "paper_topic_"
			+ System.currentTimeMillis();

	private static String topicTopicOutputFileName = "topic_topic_"
			+ System.currentTimeMillis();

	public static void main(String[] args) throws Exception {

		ProcessPaperTopicProbability paperTopicProbability = new ProcessPaperTopicProbability();
		ProcessTopicTopicProbability topicTopicProbability = new ProcessTopicTopicProbability();
		FileWriter paperTopicWriter = new FileWriter(paperTopicOutputFileName);
		FileWriter topicTopicWriter = new FileWriter(topicTopicOutputFileName);

		try {

			Properties properties = new Properties();

			properties.load(new FileInputStream(args[0]));

			FileParser.loadData(properties);

			List<String> topicList = FileParser.parseTopicFile(properties
					.getProperty("TOPICS_LIST"));
			List<String> paperList = FileParser.parsePaperIdFile(properties
					.getProperty("PAPER_LIST"));

			if (paperList.size() > 0) {
				// Paper topic matrix .

				// write topics to file
				paperTopicWriter.append(" ,");
				for (int jIndex = 0; jIndex < topicList.size(); jIndex++) {
					paperTopicWriter.append(topicList.get(jIndex));
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

			}

			// topic topic matrix

			topicTopicWriter.append(" ,");
			for (int iIndex = 0; iIndex < topicList.size(); iIndex++) {
				topicTopicWriter.append(topicList.get(iIndex));
				topicTopicWriter.append(",");
			}
			topicTopicWriter.append("\n");
			for (int jIndex = 0; jIndex < topicList.size(); jIndex++) {
				topicTopicWriter.append(topicList.get(jIndex));
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
