package com.coinsinc.googletest;

public abstract class CaseResult<T extends AbstractTestCase> {
	private final T test;
	
	public CaseResult(T test) {
		super();
		this.test = test;
	}

	T getTestCase() {
		return test;
	}

	void output(StringBuilder sb) {
		assert false;
	}
}
