package com.coinsinc.googletest.internal;

import java.util.LinkedHashMap;
import java.util.Map;

import com.coinsinc.googletest.AbstractProblemContainer;
import com.coinsinc.googletest.AbstractProblemSolver;
import com.coinsinc.googletest.AbstractTestCase;

public class ProblemManager {

	private final Map<String, AbstractProblemContainer<?>> containerMapByShortName = new LinkedHashMap<String, AbstractProblemContainer<?>>();
	private final Map<Class<?>, AbstractProblemContainer<?>> containerMapByTCClass = new LinkedHashMap<Class<?>, AbstractProblemContainer<?>>();

	public ProblemManager() {
	}

	public void addContainer(AbstractProblemContainer<?> container) {
		if (containerMapByShortName.containsKey(container.getShortName())) {
			throw new IllegalArgumentException("Dupe pb container short name <"
					+ container.getShortName() + ">.");
		}

		if (containerMapByTCClass.containsKey(container.getTestCaseClass())) {
			throw new IllegalArgumentException("Dupe pb container test class <"
					+ container.getTestCaseClass() + ">.");
		}

		containerMapByShortName.put(container.getShortName(), container);
		containerMapByTCClass.put(container.getTestCaseClass(), container);
	}

	public <T extends AbstractTestCase> void addSolver(AbstractProblemSolver<T> solver) {
		AbstractProblemContainer<?> container = containerMapByTCClass.get(solver
				.getTestCaseClass());
		
		assert solver.getTestCaseClass().equals(container.getTestCaseClass());
		@SuppressWarnings("unchecked")
		AbstractProblemContainer<T> ct = (AbstractProblemContainer<T>) container;
		
		ct.addSolver(solver);
	}

}
