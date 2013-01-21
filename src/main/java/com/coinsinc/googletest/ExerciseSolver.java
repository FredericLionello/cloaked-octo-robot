package com.coinsinc.googletest;

public interface ExerciseSolver<T extends AbstractExerciseCase> {
	String getName();

	Class<T> getTestCaseClass();

	CaseResult execute(T test);

}
