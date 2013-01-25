package com.coinsinc.googletest.problems.storecredit;

import com.coinsinc.googletest.AbstractCaseResult;

public class StoreCreditCaseResult extends AbstractCaseResult<StoreCreditTestCase> {
	



	private final int firstIdx;
	private final int secondIdx;	

	StoreCreditCaseResult(StoreCreditTestCase test, int firstIdx,
			int secondIdx) {
		super(test);
		this.firstIdx = firstIdx;
		this.secondIdx = secondIdx;
		
		assert firstIdx < secondIdx;
	}
	
	@Override
	protected void appendResult(StringBuilder sb) {
		sb.append(Integer.toString(firstIdx+1)).append(" ").append(Integer.toString(secondIdx+1));
	}


	@Override
	public String toString() {
		return "StoreCreditCaseResult [firstIdx=" + firstIdx + ", secondIdx="
				+ secondIdx + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + firstIdx;
		result = prime * result + secondIdx;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StoreCreditCaseResult other = (StoreCreditCaseResult) obj;
		if (firstIdx != other.firstIdx)
			return false;
		if (secondIdx != other.secondIdx)
			return false;
		return true;
	}
}
