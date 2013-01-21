package com.coinsinc.googletest;

import java.util.List;

public interface ExerciseSuite<T extends AbstractExerciseCase> {
	String getName();
	
	List<T> getExerciseCases();
}
