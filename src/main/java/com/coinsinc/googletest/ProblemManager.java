package com.coinsinc.googletest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProblemManager {

	private final Map<String, AbstractProblemContainer<?>> containerMapByShortName = new LinkedHashMap<String, AbstractProblemContainer<?>>();
	private final Map<Class<?>, AbstractProblemContainer<?>> containerMapByTCClass = new LinkedHashMap<Class<?>, AbstractProblemContainer<?>>();

	public ProblemManager() {
		// Perform init.
		//
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:com/coinsinc/googletest/problems/problems.xml");

		for (AbstractProblemContainer<?> pbc : ctx.getBeansOfType(
				AbstractProblemContainer.class).values()) {
			addContainer(pbc);
		}

		for (AbstractProblemSolver<?> solver : ctx.getBeansOfType(
				AbstractProblemSolver.class).values()) {
			addSolver(solver);
		}
	}

	public AbstractProblemContainer<?> getContainer(String pbname) {
		AbstractProblemContainer<?> pb = containerMapByShortName.get(pbname);
		if (pb == null) {
			throw new IllegalArgumentException("Container not found: " + pbname
					+ ", had " + getProblemNames());
		}

		return pb;
	}

	public <T extends AbstractProblemContainer<?>> T getContainer(
			String pbname, Class<T> klass) {
		AbstractProblemContainer<?> pb = getContainer(pbname);
		if (pb.getClass().equals(klass) == false) {
			throw new IllegalArgumentException(
					"Container type mismatch for name <" + pbname
							+ "> . Problem has <" + pb.getClass()
							+ "> and asked for was <" + klass + ">.");
		}

		// Safe as per above check.
		//
		@SuppressWarnings("unchecked")
		T pbt = (T) pb;
		return pbt;
	}

	Set<String> getProblemNames() {
		return containerMapByShortName.keySet();
	}

	Set<String> getSolverNames(String pbName) {
		AbstractProblemContainer<?> pb = getContainer(pbName);

		return pb.getSolverNames();
	}

	Set<String> getSuiteNames(String pbName) {
		AbstractProblemContainer<?> pb = getContainer(pbName);

		return pb.getSolverNames();
	}

	private void addContainer(AbstractProblemContainer<?> container) {
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

	public <T extends AbstractTestCase> void addSolver(
			AbstractProblemSolver<T> solver) {

		//	This will make sure the container belongs to this manager.
		//
		getContainer(solver.getName());
		
		// This is convoluted. But I want solvers to plug-in freely
		// and cannot do this in the solver constructor as this would mean
		// reference escape... See AbstractProblemSolver constructor.
		//
		solver.getContainer().addSolver(solver);
	}

}
