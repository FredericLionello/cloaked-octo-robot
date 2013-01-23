package com.coinsinc.googletest;

public interface ProblemSolver<T extends AbstractTestCase> {
		
	String getName();

	Class<T> getTestCaseClass();

	AbstractCaseResult<T> execute(T test);

}
