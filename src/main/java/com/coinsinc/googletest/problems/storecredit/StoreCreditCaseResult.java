package com.coinsinc.googletest.problems.storecredit;

import com.coinsinc.googletest.AbstractCaseResult;

public class StoreCreditCaseResult extends AbstractCaseResult<StoreCreditTestCase> {
	
	private final int firstIdx;
	private final int secondIdx;	

	public StoreCreditCaseResult(StoreCreditTestCase test, int firstIdx,
			int secondIdx) {
		super(test);
		this.firstIdx = firstIdx;
		this.secondIdx = secondIdx;
		
		assert firstIdx < secondIdx;
	}
	
	@Override
	protected void appendResult(StringBuilder sb) {
		sb.append(Integer.toString(firstIdx)).append(" ").append(Integer.toString(secondIdx));
	}


	@Override
	public String toString() {
		return "StoreCreditCaseResult [firstIdx=" + firstIdx + ", secondIdx="
				+ secondIdx + "]";
	}
}
