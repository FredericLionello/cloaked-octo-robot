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
	
	public StoreCreditCaseResult makeResult(int firstIdx, int secondIdx) {
		if (firstIdx < 0 || firstIdx >= items.length) {
			throw new IllegalArgumentException("Bad first idx to test case " + this + ": " + firstIdx);
		}
		if (secondIdx < 0 || secondIdx >= items.length) {
			throw new IllegalArgumentException("Bad second idx to test case " + this + ": " + secondIdx);
		}
		
		if (secondIdx <= firstIdx) {
			throw new IllegalArgumentException("Bad idxs to test case " + this + ": " + firstIdx + ", " + secondIdx);
		}

		return new StoreCreditCaseResult(this, firstIdx, secondIdx);
	}

	@Override
	public String toString() {
		return "StoreCreditTestCase [credit=" + credit + ", items="
				+ Arrays.toString(items) + "]";
	}
}
