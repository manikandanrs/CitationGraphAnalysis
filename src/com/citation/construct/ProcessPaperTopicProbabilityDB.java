package com.citation.construct;

import java.sql.SQLException;

import com.citation.database.DBFetch;
import com.citation.datastructures.Graph;

public class ProcessPaperTopicProbabilityDB {

	public double process(Graph graph, String paperId, String topicId)
			throws SQLException {
		
		// for all papers in topic plane,
		// check if the there is an citation edge from paper to paperID
		// if the edge is also in topic plane just add the probability
		// if the edge is between planes then calculate the Impact the cited
		// paper makes on the citing topic. Multiply this impact to the edge
		// citation probability and add it

		// Taking all nodes from the graph

		return DBFetch.calculatePaperTopicProbability(paperId, topicId);

	}

	
	
	
	

}
