package com.coinsinc.googletest.users.fred;

import java.util.HashMap;
import java.util.Map;

import com.coinsinc.googletest.AbstractCaseResult;
import com.coinsinc.googletest.AbstractProblemContainer;
import com.coinsinc.googletest.AbstractProblemSolver;
import com.coinsinc.googletest.problems.storecredit.StoreCreditTestCase;

public class Dumb extends AbstractProblemSolver<StoreCreditTestCase>{

	public Dumb(
			AbstractProblemContainer<StoreCreditTestCase> container) {
		super("fred_dumb", container);
	}

	@Override
	public AbstractCaseResult<StoreCreditTestCase> execute(
			StoreCreditTestCase test) {
		
		final int[] items = test.getItems();
		
		for (int i = 0 ; i < items.length ; i++) {
			for (int j = i+1 ; j < items.length ; j++) {
				if (items[i] + items[j] == test.getCredit()) {
					return test.makeResult(i, j);
				}
			}
		}
		
		throw new IllegalStateException("No solution for: " + test);
	}


}
