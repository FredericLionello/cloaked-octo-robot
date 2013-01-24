package com.coinsinc.googletest.problems.storecredit;

import java.util.Arrays;

import com.coinsinc.googletest.AbstractTestCase;

public class StoreCreditTestCase extends AbstractTestCase {
	
	private final int credit;
	private final int[] items;

	public StoreCreditTestCase(int id, int credit, int[] items) {
		super(id);
		this.credit = credit;
		this.items = items;
	}

	public int getCredit() {
		return credit;
	}

	public int[] getItems() {
		return Arrays.copyOf(items, items.length);
	}
}
