package com.coinsinc.googletest.problems.storecredit;

import java.io.Reader;
import java.util.List;

import com.coinsinc.googletest.AbstractProblemContainer;
import com.coinsinc.googletest.FileParser;

public class StoreCredProblemContainer extends AbstractProblemContainer<StoreCreditTestCase> {

	public StoreCredProblemContainer(String name,
			Class<StoreCreditTestCase> testCaseClass) {
		super(name, testCaseClass);
	}

	@Override
	protected void parseDataset(Reader reader, final List<StoreCreditTestCase> list) {
		
		new FileParser(reader) {

			@Override
			protected void parseIteration(int i) {
				int credit = toInt(getNextLine());
				int nbTokens = toInt(getNextLine());
				int[] items = toIntArray(getNextLine(), nbTokens);
				
				list.add(new StoreCreditTestCase(i, credit, items));
			}
		}.parse();
		
	}

}
