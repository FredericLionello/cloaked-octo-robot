package com.coinsinc.googletest;

public abstract class AbstractCaseResult<T extends AbstractTestCase> {
	private final T test;
	
	public AbstractCaseResult(T test) {
		super();
		this.test = test;
	}

	T getTestCase() {
		return test;
	}

	void output(StringBuilder sb) {
		sb.append("Case #" + test.getId() + ": ");
		appendResult(sb);
	}
	
	protected abstract void appendResult(StringBuilder sb);
}
