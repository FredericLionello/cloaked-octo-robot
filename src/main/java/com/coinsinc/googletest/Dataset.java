package com.coinsinc.googletest;

import java.util.ArrayList;
import java.util.List;

public class Dataset<T extends AbstractTestCase> {
	private final String name;
	private final List<T> testCases;
	
	public Dataset(String name, List<T> testCases) {
		super();
		this.name = name;
		this.testCases = new ArrayList<T>(testCases);
	}
	
	
	public String getName() {
		return name;
	}
	public List<T> getTestCases() {
		return testCases;
	}
	
	
}
