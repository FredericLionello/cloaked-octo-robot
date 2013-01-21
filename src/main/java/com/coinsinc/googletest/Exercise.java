package com.coinsinc.googletest;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Exercise<T extends AbstractExerciseCase> {
	private final String name;
	private final Map<String, ExerciseSuite<T>> suiteMap = new LinkedHashMap<String, ExerciseSuite<T>>();
	private final Map<String, ExerciseSolver<T>> solverMap = new LinkedHashMap<String, ExerciseSolver<T>>();
	private static final Logger Log = Logger.getLogger("TestContainer");

	Exercise(String name) {
		super();
		this.name = name;
	}

	void registerSolver(ExerciseSolver<T> solver) {
		if (solverMap.containsKey(solver.getName())) {
			throw new RuntimeException("Already a solver named: "
					+ solver.getName());
		}

		solverMap.put(solver.getName(), solver);
	}

	void runSolverBenchmark(String solverName, String suiteName) {
		// Time and run.
		//
		ExerciseSolver<T> solver = solverMap.get(solverName);
		if (solver == null) {
			throw new IllegalArgumentException("No solver named <" + solverName
					+ ">.");
		}
		
		ExerciseSuite<T> suite = suiteMap.get(suiteName);
		if (suite == null) {
			throw new IllegalArgumentException("No suite named <" + suiteName
					+ ">.");
		}
		

		Log.info("Starting benchmark, test <" + name + ">, solver <" + solverName
				+ ">, suite <" + suiteName + ">.");

		// This is micro benchmarking with the associated errors. We should
		// probably have a warmup phase to allow JIT magic to happen, but the
		// benchmark need to happen with a single test loaded in memory, and we
		// could end up with the JVM compiling out the results.
		// So, all solvers run without warmup. May the best win.
		//
		
		long t1 = System.nanoTime();
		for (AbstractExerciseCase aec: suite.getExerciseCases()) {
			aec.inject(solver);
		}
		long t2 = System.nanoTime();
		
		Log.info("Execution time: " + ((t2 - t1) * 1e-6) + " msecs");
	}

	void checkSolvers() {
		Log.info("Checking solvers.");

		Set<ExerciseSolver<T>> s = new HashSet<ExerciseSolver<T>>(
				solverMap.values());
		for (ExerciseSuite<T> suite : suiteMap.values()) {
			Log.info("Running suite: " + suite.getName());
			for (T test : suite.getExerciseCases()) {
				CaseResult referenceRes = null;
				Log.info("Test: " + test);

				for (ExerciseSolver<T> solver : s) {
					Log.info("Executing for solver: " + solver.getName()
							+ " ...");
					CaseResult res = solver.execute(test);
					Log.info("Result for solver: " + solver.getName() + ": "
							+ res);

					if (referenceRes != null) {
						if (res.equals(referenceRes) == false) {
							Log.severe("Different results !");
							throw new RuntimeException("Different results !");
						}
					} else {
						referenceRes = res;
					}
				}
			}
		}

		Log.info("Solver check OK.");
	}

}