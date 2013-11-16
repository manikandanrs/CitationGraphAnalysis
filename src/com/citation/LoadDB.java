package com.citation;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.citation.datastructures.Edge;
import com.citation.datastructures.Graph;
import com.citation.datastructures.Node;
import com.citation.datastructures.PageRankImportance;
import com.citation.util.FileParser;

public class LoadDB {

	private static Connection dbConn = null;

	public static void main(String[] args) throws Exception {

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		dbConn = DriverManager.getConnection("jdbc:mysql://localhost/graph",
				"graph", "ms+gr=my+sql");

		if (dbConn != null) {
			System.out.println("Db connection succesfull");
		}

		Properties properties = new Properties();

		properties.load(new FileInputStream(args[0]));

		FileParser.loadData(properties);

	//	Map<String, String> topicIdNameMap = FileParser.getTopicIdNameMap();

	//	loadTopicIdNames(topicIdNameMap);

		//loadPaperAndCitationTopicProbability(FileParser.getGraph());

		//loadPageRankImportance(FileParser.getGraph());

	}

	private static void loadTopicIdNames(Map<String, String> topicIdNameMap)
			throws SQLException {

		PreparedStatement pstmt = dbConn
				.prepareStatement("INSERT INTO topics values(?,?)");

		for (String topicId : topicIdNameMap.keySet()) {
			pstmt.setInt(1, Integer.parseInt(topicId));
			pstmt.setString(2, topicIdNameMap.get(topicId));
			pstmt.execute();
		}
	}

	private static void loadPaperAndCitationTopicProbability(Graph graph)
			throws SQLException {
		PreparedStatement pstmtTopic = dbConn
				.prepareStatement("INSERT INTO paper_topic_probability values(?,?,?)");

		PreparedStatement pstmtCitation = dbConn
				.prepareStatement("INSERT INTO citation_topic_probability values(?,?,?,?)");

		Map<String, Node> nodes = graph.getNodeList();

		for (String nodeName : nodes.keySet()) {

			Node n = nodes.get(nodeName);

			String paperId = n.getPaperId();

			Map<String, Double> paperTopicProbability = n.getTopicProbability();

			for (String topicId : paperTopicProbability.keySet()) {
				pstmtTopic.setString(1, paperId);
				pstmtTopic.setInt(2, Integer.parseInt(topicId));
				pstmtTopic.setDouble(3, paperTopicProbability.get(topicId));
				pstmtTopic.execute();
			}

			Map<Node, List<Edge>> citationEdges = n.getCitationEdgesMap();

			if (citationEdges != null) {
				for (Node citedNode : citationEdges.keySet()) {

					if (citedNode != null) {
						String citedPaperId = citedNode.getPaperId();

						for (Edge e : citationEdges.get(citedNode)) {
							pstmtCitation.setString(1, paperId);
							pstmtCitation.setString(2, citedPaperId);

							Map<String, Double> citationProbability = e
									.getCitationTopicProbability();

							for (String topicId : citationProbability.keySet()) {
								pstmtCitation.setInt(3,
										Integer.parseInt(topicId));
								if(citationProbability.get(topicId).isNaN())
									continue;
								pstmtCitation.setDouble(4,
										citationProbability.get(topicId));
								pstmtCitation.execute();
							}
						}

					}
				}
			}
		}
	}

	private static void loadPageRankImportance(Graph graph) throws Exception {

		 
		

		PageRankImportance pri = graph.getPageRankImportance();

		Map<String, Map<String, Double>> paperPageRank = pri
				.getPaperPageRankProbability();

		for (String topicId : paperPageRank.keySet()) {
			
			Statement stmt = dbConn.createStatement();
			stmt.executeUpdate("CREATE TABLE pg_" + topicId + "(paper_id varchar(64) primary key, probability double)");
			
			
			String sql = "INSERT INTO pg_" + topicId + " VALUES(?,?)";
			PreparedStatement pstmtPageRank = dbConn
			.prepareStatement(sql);
			
			Map<String, Double> probability = paperPageRank.get(topicId);

			for (String paperId : probability.keySet()) {
				pstmtPageRank.setString(1, paperId);
				if(probability.get(paperId).isNaN())
					continue;
				pstmtPageRank.setDouble(2, probability.get(paperId));
				pstmtPageRank.execute();
			}
		}
	}
}
