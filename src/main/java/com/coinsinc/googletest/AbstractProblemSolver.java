package com.coinsinc.googletest;

public abstract class AbstractProblemSolver<T extends AbstractTestCase> implements ProblemSolver<T> {
	private final String name;
	private final Class<T> testCaseClass;

	public AbstractProblemSolver(String name, Class<T> testCaseClass) {
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
