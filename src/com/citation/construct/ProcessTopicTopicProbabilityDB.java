package com.citation.construct;

import java.sql.SQLException;

import com.citation.database.DBFetch;
import com.citation.datastructures.Graph;

public class ProcessTopicTopicProbabilityDB {

	
	public double process(Graph graph, String topic1, String topic2)
			throws SQLException {

		// Take every node in the graph.
		// Check if the node's topic probability for topic 1 is > 0. This
		// implies the node is in the topic plane of topic 1.
		// Get all the citation edges for node.
		// For every node involved in the citation,
		// Check if the cited node's topic probability for topic 2 is > 0. This
		// implies that the cited node is in the topic plane of topic 2.
		// Get all the edges from the citing node and cited node.
		// For each edge
		// get the topic probability of the cited topic and add that to overall
		// probability.
		 
		
		double probability = DBFetch.calculateTopicTopicProbability(topic1, topic2);
		

		return probability;

	}
}
