package com.coinsinc.googletest;

public interface ProblemSolver<T extends AbstractTestCase> {
	String getName();

	Class<T> getTestCaseClass();

	CaseResult<T> execute(T test);

}
