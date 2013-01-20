package com.coinsinc.googletest;

public interface TestSolver<T extends AbstractTestCase> {
	String getName();

	Class<T> getTestCaseClass();

	TestResult execute(T test);

}
