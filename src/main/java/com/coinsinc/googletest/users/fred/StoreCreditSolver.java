package com.coinsinc.googletest.users.fred;

import java.util.HashMap;
import java.util.Map;

import com.coinsinc.googletest.AbstractCaseResult;
import com.coinsinc.googletest.AbstractProblemContainer;
import com.coinsinc.googletest.AbstractProblemSolver;
import com.coinsinc.googletest.problems.storecredit.StoreCreditTestCase;

public class StoreCreditSolver extends AbstractProblemSolver<StoreCreditTestCase>{

	public StoreCreditSolver(
			AbstractProblemContainer<StoreCreditTestCase> container) {
		super("fred_hash", container);
	}

	@Override
	public AbstractCaseResult<StoreCreditTestCase> execute(
			StoreCreditTestCase test) {
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		final int[] items = test.getItems();
		
		for (int i = 0 ; i < items.length ; i++) {
			int price = items[i];
			
			if (price > test.getCredit()) {
				continue;
			}
			
			int difference = test.getCredit() - price;
			Integer otherIdx = map.get(difference);
			
			if (otherIdx == null) {
				map.put(price, i);
				continue;
			}
			
			return test.makeResult(otherIdx, i);
		}
		
		throw new IllegalStateException("No solution for: " + test);
	}

}
