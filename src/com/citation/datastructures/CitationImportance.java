package com.citation.datastructures;
import java.util.List;

public class CitationImportance {

	private String citingPaper;

	private String citedPaper;

	private List<Integer> probabilities;

	public String getCitingPaper() {
		return citingPaper;
	}

	public void setCitingPaper(String citingPaper) {
		this.citingPaper = citingPaper;
	}

	public String getCitedPaper() {
		return citedPaper;
	}

	public void setCitedPaper(String citedPaper) {
		this.citedPaper = citedPaper;
	}

	public List<Integer> getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(List<Integer> probabilities) {
		this.probabilities = probabilities;
	}

}
