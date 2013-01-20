package com.coinsinc.googletest;

import java.util.List;

public interface TestSuite<T extends AbstractTestCase> {
	String getName();
	
	List<T> getTestCases();
}
