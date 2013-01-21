package com.coinsinc.googletest;

public abstract class AbstractTestCase {
	private final int id;

	public AbstractTestCase(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
