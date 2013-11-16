package com.citation;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.citation.construct.ProcessPaperTopicProbabilityDB;
import com.citation.construct.ProcessTopicTopicProbabilityDB;
import com.citation.database.DBFetch;
import com.citation.util.FileParser;

public class FooFB {

	private static String paperTopicOutputFileName = "paper_topic_"
			+ System.currentTimeMillis() + ".csv";

	private static String topicTopicOutputFileName = "topic_topic_"
			+ System.currentTimeMillis() + ".csv";

	public static void main(String[] args) throws Exception {

		ProcessPaperTopicProbabilityDB paperTopicProbability = new ProcessPaperTopicProbabilityDB();
		ProcessTopicTopicProbabilityDB topicTopicProbability = new ProcessTopicTopicProbabilityDB();
		FileWriter paperTopicWriter = new FileWriter(paperTopicOutputFileName);
		FileWriter topicTopicWriter = new FileWriter(topicTopicOutputFileName);
		DBFetch.createConnection("graph", "ms+gr=my+sql",
				"jdbc:mysql://localhost/graph");

		try {

			Properties properties = new Properties();

			properties.load(new FileInputStream(args[0]));

			List<String> topicList = FileParser.parseTopicFile(properties
					.getProperty("TOPICS_LIST"));
			List<String> paperList = FileParser.parsePaperIdFile(properties
					.getProperty("PAPER_LIST"));

		/*	if (paperList.size() > 0) {
				// Paper topic matrix .

				// write topics to file
				paperTopicWriter.append("paper/topic,");
				for (int jIndex = 0; jIndex < topicList.size(); jIndex++) {
					paperTopicWriter.append(DBFetch.getTopicName(topicList
							.get(jIndex)));
					paperTopicWriter.append(",");
				}
				paperTopicWriter.append("\n");

				StringBuilder lineToBeWritten = null;
				for (int iIndex = 0; iIndex < paperList.size(); iIndex++) {
					lineToBeWritten = new StringBuilder();
					lineToBeWritten.append(paperList.get(iIndex));
					lineToBeWritten.append(",");

					for (int jIndex = 0; jIndex < topicList.size(); jIndex++) {
						Double probability = paperTopicProbability.process(
								FileParser.getGraph(), paperList.get(iIndex),
								topicList.get(jIndex));
						/*
						 * System.out.println(paperList.get(iIndex) + " -- " +
						 * topicList.get(jIndex) + " , " + probability);
						 */
			/*			lineToBeWritten.append(String.valueOf(probability));
						lineToBeWritten.append(",");
					}
					System.out.println("Papers Completed : " + (iIndex + 1));

					lineToBeWritten.append("\n");
					paperTopicWriter.append(lineToBeWritten.toString());
				}

				paperTopicWriter.append("\n\n\n");
				
				

				/*
				 * for (int iIndex = 0; iIndex < paperList.size(); iIndex++) {
				 * List<String> keywords = paperRelatedTopics.get(paperList
				 * .get(iIndex));
				 * paperTopicWriter.append(paperList.get(iIndex));
				 * paperTopicWriter.append("-->"); for (String keyword :
				 * keywords) { paperTopicWriter
				 * .append(topicIdNameMapping.get(keyword));
				 * paperTopicWriter.append(":"); }
				 * paperTopicWriter.append("\n"); }
				 */
/*			}
*/
			// topic topic matrix

			topicTopicWriter.append(" ,");
			for (int iIndex = 0; iIndex < topicList.size(); iIndex++) {
				topicTopicWriter.append(DBFetch.getTopicName(topicList
						.get(iIndex)));
				topicTopicWriter.append(",");
			}
			topicTopicWriter.append("\n");

			StringBuilder lineToBeWritten = null;

			for (int jIndex = 0; jIndex < topicList.size(); jIndex++) {
				lineToBeWritten = new StringBuilder();
				lineToBeWritten.append(DBFetch.getTopicName(topicList
						.get(jIndex)));
				lineToBeWritten.append(",");
				for (int iIndex = 0; iIndex < topicList.size(); iIndex++) {
					double probability = 0.0;
					if (iIndex != jIndex) {
						probability = topicTopicProbability.process(
								FileParser.getGraph(), topicList.get(jIndex),
								topicList.get(iIndex));
						/*
						 * System.out.println(topicList.get(jIndex) + " -- " +
						 * topicList.get(iIndex) + " -->" + probability);
						 */

						// call to calculate the topic topic probability.
					// save the results and project it in a html file.
					}

					lineToBeWritten.append(String.valueOf(probability));
					lineToBeWritten.append(",");
				}
				System.out.println("topics Completed : " + (jIndex + 1));
				lineToBeWritten.append("\n");
				topicTopicWriter.append(lineToBeWritten.toString());
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
