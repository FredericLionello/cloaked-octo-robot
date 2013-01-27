package com.coinsinc.googletest.users.zouille;

import com.coinsinc.googletest.AbstractCaseResult;
import com.coinsinc.googletest.AbstractProblemContainer;
import com.coinsinc.googletest.AbstractProblemSolver;
import com.coinsinc.googletest.problems.storecredit.StoreCreditTestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
		sortIndicesAccordingToItems();
	}

	private int[] items;
	private ArrayList<Integer> indicesArray;

	private ArrayList<Integer> createIndicesArray(int numberOfItems) {
		ArrayList<Integer> indicesArray = new ArrayList<Integer>(numberOfItems);

		for (int index = 0 ; index < numberOfItems ; index++) {
			indicesArray.add(index);
		}
		return indicesArray;

	}


	private void sortIndicesAccordingToItems() {
		class ComparatorAccordingToItems implements Comparator<Integer> {
			@Override
			public int compare(Integer i1, Integer i2) {
				return items[i1] > items[i2] ? +1 : items[i1] < items[i2] ? -1 : 0;
			}
		}

		Collections.sort(indicesArray, new ComparatorAccordingToItems());
	}

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
		int price1 = items[indicesArray.get(index1)];
		int price2 = items[indicesArray.get(index2)];

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
