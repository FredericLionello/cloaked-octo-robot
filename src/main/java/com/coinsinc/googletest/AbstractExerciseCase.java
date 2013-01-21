package com.coinsinc.googletest;

public abstract class AbstractExerciseCase {
	private final int id;

	public AbstractExerciseCase(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public <T extends AbstractExerciseCase> void checkSolver(ExerciseSolver<T> solver) {
		if (solver.getTestCaseClass().equals(getClass()) == false) {
			throw new IllegalStateException("Bad solver test class. Should be "
					+ getClass() + " and is " + solver.getTestCaseClass());
		}
	}

	<T extends AbstractExerciseCase> CaseResult inject(ExerciseSolver<T> solver) {

		// The validity of this cast should be checked outside of the per test
		// case injection.
		//
		checkSolver(solver);
		T tc = (T) this;

		return solver.execute(tc);
	}
}
