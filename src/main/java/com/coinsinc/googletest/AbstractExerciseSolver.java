package com.coinsinc.googletest;

public abstract class AbstractExerciseSolver<T extends AbstractExerciseCase> implements ExerciseSolver<T> {
	private final String name;
	private final Class<T> testCaseClass;

	public AbstractExerciseSolver(String name, Class<T> testCaseClass) {
		super();
		this.name = name;
		this.testCaseClass = testCaseClass;
	}

	public String getName() {
		return name;
	}

	public Class<T> getTestCaseClass() {
		return testCaseClass;
	}

}
