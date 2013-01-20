package com.coinsinc.googletest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Exercise<T extends AbstractTestCase> {
	private final TestSuite<T> suite;
	private final Map<String, TestSolver<T>> solverMap = new HashMap<String, TestSolver<T>>();
	private static final Logger Log = Logger.getLogger("TestContainer");
	
	Exercise(TestSuite<T> suite) {
		super();
		this.suite = suite;
	}
	
	void registerSolver(TestSolver<T> solver) {
		if (solverMap.containsKey(solver.getName())) {
			throw new RuntimeException("Already a solver named: " + solver.getName());
		}
		
		solverMap.put(solver.getName(), solver);
	}
	
	void runSolverBenchmark(String name) {
		//	Time and run.
		//
		assert false;
	}
	
	void checkSolvers() {
		Log.info("Checking solvers.");
		
		Set<TestSolver<T>> s = new HashSet<TestSolver<T>>(solverMap.values());
		for (T test: suite.getTestCases()) {
			TestResult referenceRes = null;
			Log.info("Test: " + test);
			
			for (TestSolver<T> solver: s) {
				Log.info("Executing for solver: " + solver.getName() + " ...");
				TestResult res = solver.execute(test);
				Log.info("Result for solver: " + solver.getName() + ": " + res);
				
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
		
		Log.info("Solver check OK.");
	}
	
}