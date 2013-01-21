package com.coinsinc.googletest;

import java.util.List;

public interface Dataset<T extends AbstractTestCase> {
	String getName();
	
	List<T> getTestCases();
}
