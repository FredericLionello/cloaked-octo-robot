package com.coinsinc.googletest.users.zouille;

import com.coinsinc.googletest.AbstractCaseResult;
import com.coinsinc.googletest.AbstractProblemContainer;
import com.coinsinc.googletest.AbstractProblemSolver;
import com.coinsinc.googletest.problems.storecredit.StoreCreditTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StoreCreditSolver extends AbstractProblemSolver<StoreCreditTestCase>{

	public StoreCreditSolver(
			AbstractProblemContainer<StoreCreditTestCase> container) {
		super("zouille_sort", container);
	}

	@Override
	public AbstractCaseResult<StoreCreditTestCase> execute(
			StoreCreditTestCase test) {

		prepareData(test);
		int[] possibleSolutionIndices = getIndicesWhoseSumIsCredit(test.getCredit());

		return solutionOrErrorForTest(possibleSolutionIndices, test);
	}

	private void prepareData(StoreCreditTestCase testCase) {
		items = testCase.getItems();
		indicesArray = createIndicesArray(items.length);
	}

	private int[] items;
	private Integer[] indicesArray;

	private Integer[] createIndicesArray(int numberOfItems) {
		Integer[] indicesArray = new Integer[numberOfItems];
		for (int index = 0 ; index < numberOfItems ; index++) {
			indicesArray[index] = new Integer(index);
		}

		Arrays.sort(indicesArray, 0, indicesArray.length, comparatorAccordingToItems);
		return indicesArray;
	}


	private class ComparatorAccordingToItems implements java.util.Comparator<Object> {
		public int compare(Object i1, Object i2) {
			return items[(Integer)i1] > items[(Integer)i2] ? +1 : items[(Integer)i1] < items[(Integer)i2] ? -1 : 0;
		}
	};

	private final ComparatorAccordingToItems comparatorAccordingToItems = new ComparatorAccordingToItems();

	private int[] getIndicesWhoseSumIsCredit(int credit) {
		int minIndex = 0;
		int maxIndex = items.length - 1;

		while (indicesIntegrity(minIndex, maxIndex) && (priceSumFromIndices(minIndex, maxIndex) != credit)) {
			if (priceSumFromIndices(minIndex, maxIndex) > credit) {
				maxIndex -= 1;
			}
			else if (priceSumFromIndices(minIndex, maxIndex) < credit) {
				minIndex += 1;
			}
		}
		if (!indicesIntegrity(minIndex, maxIndex)) {
			return null;
		}

		return new int[] {minIndex, maxIndex};
	}

	private int priceSumFromIndices(int index1, int index2) {
		int price1 = items[indicesArray[index1]];
		int price2 = items[indicesArray[index2]];

		return price1 + price2;
	}

	private Boolean indicesIntegrity(int minIndex, int maxIndex) {
		Boolean outOfBounds = (minIndex >= items.length) || (maxIndex < 0);
		Boolean inversion = (minIndex >= maxIndex);

		return !outOfBounds && !inversion;
	}

	private AbstractCaseResult<StoreCreditTestCase> solutionOrErrorForTest(
			int[] indicesOrNull, StoreCreditTestCase test) {
		if (indicesOrNull == null) {
			throw new IllegalStateException("No solution for: " + test);
		}
		else {
			return test.makeResult(indicesOrNull[0], indicesOrNull[1]);
		}
	}

}
