package com.citation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.citation.datastructures.CitationTopicProbability;
import com.citation.datastructures.PageRankProbability;
import com.citation.datastructures.PaperTopicProbability;

public class DBFetch {

	private static Connection dbConnection = null;

	public static void createConnection(String userid, String password,
			String url) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		dbConnection = DriverManager.getConnection(url, userid, password);
	}

	public static double calculateTopicTopicProbability(String topic1, String topic2) throws SQLException{
		
		double result = 0.0;
		ResultSet rs = null;
		
			
		//String sql = "select sum(ctp.probability*pg.probability) from paper_topic_probability ptp, citation_topic_probability ctp, page_rank_importance pg where ptp.topic_id = ? and ctp.citing_paper_id = ptp.paper_id and ctp.topic_id = ? and pg.topic_id = ptp.topic_id and pg.paper_id = ctp.cited_paper_id";
		
		//String sql = "select sum(ctp.probability) from paper_topic_probability ptp, citation_topic_probability ctp where ptp.topic_id = ? and ctp.citing_paper_id = ptp.paper_id and ctp.topic_id = ?";
		
		//String sql = "select sum(ctp.probability) from citation_topic_probability ctp, (select paper_id from paper_topic_probability where topic_id = ?) as ptp where ctp.citing_paper_id = ptp.paper_id and ctp.topic_id = ?;";
		
		String sql = "select sum(ctp.probability) from paper_topic_probability ptp, citation_topic_probability ctp where ptp.topic_id = ? and ctp.citing_paper_id = ptp.paper_id and ctp.topic_id = ?";
		
		//StringBuilder sql = new StringBuilder("select sum(ctp.probability*pg.probability) from paper_topic_probability ptp, citation_topic_probability ctp, page_rank_importance pg where ptp.topic_id = ? and ctp.citing_paper_id = ptp.paper_id and ctp.topic_id = ? and pg.topic_id = ptp.topic_id and pg.paper_id = ctp.cited_paper_id");
		
		PreparedStatement pstmt = dbConnection.prepareStatement(sql.toString());

		int i = 1;

		pstmt.setInt(1, Integer.parseInt(topic1));
		pstmt.setInt(2, Integer.parseInt(topic2));
		
		rs = pstmt.executeQuery();

		if (rs.next()) {

			result = rs.getDouble(1);

		}

		rs.close();
		pstmt.close();

		return result;
		
	}
	
	public static double calculatePaperTopicProbability(String paperId, String topic
			) throws SQLException {
		double result = 0.0;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder(
				"select((select sum(probability) from citation_topic_probability where cited_paper_id = ? AND topic_id = ?) + (select sum(c.probability*pg.probability) from citation_topic_probability c,(select probability from page_rank_importance where paper_id = ? and topic_id = ?) as pg where c.cited_paper_id = ? and c.topic_id != ?))");

		// System.out.println(sql.toString());

		/*
		 * PreparedStatement pstmt = dbConnection .prepareStatement(
		 * "select((select sum(probability) from citation_topic_probability where citing_paper_id IN (select paper_id from paper_topic_probability where topic_id = ? and paper_id != ?) AND cited_paper_id = ? AND topic_id = ?) + (select sum(c.probability*pg.probability) from citation_topic_probability c,(select probability from page_rank_importance where paper_id = ? and topic_id = ?) as pg where c.citing_paper_id IN (select paper_id from paper_topic_probability where topic_id = ? and paper_id != ?) and c.cited_paper_id = ? and c.topic_id != ?))"
		 * );
		 */

		PreparedStatement pstmt = dbConnection.prepareStatement(sql.toString());

		int i = 1;

		pstmt.setString(i++, paperId);
		pstmt.setInt(i++, Integer.parseInt(topic));
		pstmt.setString(i++, paperId);
		pstmt.setInt(i++, Integer.parseInt(topic));

		pstmt.setString(i++, paperId);
		pstmt.setInt(i++, Integer.parseInt(topic));

		rs = pstmt.executeQuery();

		if (rs.next()) {

			result = rs.getDouble(1);

		}

		rs.close();
		pstmt.close();

		return result;
	}

	public static List<PaperTopicProbability> getPapersForTopicPlane(
			String topicId) throws SQLException {

		List<PaperTopicProbability> paperList = new ArrayList<PaperTopicProbability>();
		ResultSet rs = null;

		PreparedStatement pstmt = dbConnection
				.prepareStatement("SELECT paper_id FROM paper_topic_probability WHERE topic_id = ?");

		pstmt.setInt(1, Integer.parseInt(topicId));
		
		rs = pstmt.executeQuery();

		PaperTopicProbability ptp = null;
		while (rs.next()) {

			ptp = new PaperTopicProbability();

			ptp.setPaperId(rs.getString(1));
			ptp.setTopicId(Integer.parseInt(topicId));
			ptp.setProbability(rs.getDouble(2));

			paperList.add(ptp);

		}

		rs.close();
		pstmt.close();

		return paperList;

	}

	public static List<CitationTopicProbability> getCitationsFromPaperToPaper(
			String citingPaperId, String citedPaperId) throws SQLException {

		List<CitationTopicProbability> paperList = new ArrayList<CitationTopicProbability>();
		ResultSet rs = null;

		PreparedStatement pstmt = dbConnection
				.prepareStatement("SELECT topic_id, probability FROM citation_topic_probability WHERE citing_paper_id = ? AND cited_paper_id = ?");

		pstmt.setString(1, citingPaperId);
		pstmt.setString(2, citedPaperId);

		rs = pstmt.executeQuery();

		CitationTopicProbability ctp = null;
		while (rs.next()) {

			ctp = new CitationTopicProbability();

			ctp.setCitingPaperId(citingPaperId);
			ctp.setCitedPaperId(citedPaperId);
			ctp.setTopicId(rs.getInt(1));
			ctp.setProbability(rs.getDouble(2));

			paperList.add(ctp);

		}

		rs.close();
		pstmt.close();

		return paperList;

	}

	public static List<CitationTopicProbability> getCitationsFromPaperToTopic(
			String citingPaperId, String topicId) throws SQLException {

		List<CitationTopicProbability> paperList = new ArrayList<CitationTopicProbability>();
		ResultSet rs = null;

		PreparedStatement pstmt = dbConnection
				.prepareStatement("SELECT cited_paper_id, probability FROM citation_topic_probability WHERE citing_paper_id = ? AND topic_id = ?");

		pstmt.setString(1, citingPaperId);
		pstmt.setInt(2, Integer.parseInt(topicId));

		rs = pstmt.executeQuery();

		CitationTopicProbability ctp = null;
		while (rs.next()) {

			ctp = new CitationTopicProbability();

			ctp.setCitingPaperId(citingPaperId);
			ctp.setCitedPaperId(rs.getString(1));
			ctp.setTopicId(Integer.parseInt(topicId));
			ctp.setProbability(rs.getDouble(2));

			paperList.add(ctp);

		}

		rs.close();
		pstmt.close();

		return paperList;

	}

	public static PageRankProbability getPageRankValue(String paperId,
			String topicId) throws SQLException {

		ResultSet rs = null;

		String sql = "SELECT probability FROM pg_" + topicId
				+ " WHERE paper_id = ?";

		PreparedStatement pstmt = dbConnection.prepareStatement(sql);

		pstmt.setString(1, paperId);

		rs = pstmt.executeQuery();

		PageRankProbability pageRank = null;
		while (rs.next()) {

			pageRank = new PageRankProbability();

			pageRank.setPaperId(paperId);
			pageRank.setTopicId(Integer.parseInt(topicId));
			pageRank.setPageRankValue(rs.getDouble(1));

		}

		rs.close();
		pstmt.close();

		return pageRank;

	}

	public static String getTopicName(String topicId) throws SQLException {

		ResultSet rs = null;

		PreparedStatement pstmt = dbConnection
				.prepareStatement("SELECT topic_name FROM topics WHERE topic_id = ?");

		pstmt.setInt(1, Integer.parseInt(topicId));

		rs = pstmt.executeQuery();

		String topicName = null;
		if (rs.next()) {

			topicName = rs.getString(1);
		}

		rs.close();
		pstmt.close();

		return topicName;

	}

}
