package br.ufmg.lcc.arangitester.log;

import java.util.ArrayList;
import java.util.List;

public class FunctionalSuite {
	/*
	 * This list will contain all informations about all of use cases
	 */
	private List<UseCase> cases = new ArrayList<UseCase>();

	/**
	 * Get informations about a specific use case
	 * @return cases
	 */
	public List<UseCase> getCases() {
		return cases;
	}

	/**
	 * Modify a specific use case
	 * @param cases
	 */
	public void setCases(List<UseCase> cases) {
		this.cases = cases;
	}

}
