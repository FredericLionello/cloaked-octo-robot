package com.coinsinc.googletest;

public abstract class AbstractTestSolver<T extends AbstractTestCase> implements TestSolver<T> {
	private final String name;
	private final Class<T> testCaseClass;

	public AbstractTestSolver(String name, Class<T> testCaseClass) {
		super();
		this.name = name;
		this.testCaseClass = testCaseClass;
	}

	public String getName() {
		return name;
	}

	public Class<T> getTestCaseClass() {
		return testCaseClass;
	}

}
