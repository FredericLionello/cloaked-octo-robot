package com.coinsinc.googletest;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public abstract class AbstractProblemContainer<T extends AbstractTestCase> {
	private final String shortName;
	private boolean initDone = false;
	private final Class<T> testCaseClass;
	private final Map<String, Dataset<T>> suiteMap = new LinkedHashMap<String, Dataset<T>>();
	private final Map<String, ProblemSolver<T>> solverMap = new LinkedHashMap<String, ProblemSolver<T>>();
	private static final Logger Log = Logger.getLogger("TestContainer");

	public AbstractProblemContainer(String name, Class<T> testCaseClass) {
		super();
		this.shortName = name;
		this.testCaseClass = testCaseClass;
	}

	public String getShortName() {
		return shortName;
	}

	public Class<T> getTestCaseClass() {
		return testCaseClass;
	}

	protected abstract void init();

	private void wrapInit() {
		if (initDone == true) {
			return;
		}

		init();

		if (suiteMap.isEmpty()) {
			throw new IllegalStateException("Problem <" + shortName
					+ "> has no test suite after init.");
		}

		initDone = true;
	}

	public final void addDataset(Dataset<T>... suites) {
		for (Dataset<T> s : suites) {
			if (suiteMap.containsKey(s.getName()) == true) {
				throw new IllegalArgumentException("Twice the suite named <"
						+ s.getName() + ">.");
			}

			suiteMap.put(s.getName(), s);
		}
	}

	public void addSolver(ProblemSolver<T> solver) {
		if (solver.getTestCaseClass().equals(testCaseClass) == false) {
			throw new IllegalArgumentException("Solver <" + solver.getName()
					+ "> bad test class type. Expected <" + testCaseClass
					+ "> and solver has <" + solver.getTestCaseClass() + ">.");
		}
		if (solverMap.containsKey(solver.getName())) {
			throw new RuntimeException("Already a solver named: "
					+ solver.getName());
		}

		solverMap.put(solver.getName(), solver);
	}

	void runSolverBenchmark(String solverName, String suiteName) {
		wrapInit();

		// Time and run.
		//
		ProblemSolver<T> solver = solverMap.get(solverName);
		if (solver == null) {
			throw new IllegalArgumentException("No solver named <" + solverName
					+ ">.");
		}

		Dataset<T> suite = suiteMap.get(suiteName);
		if (suite == null) {
			throw new IllegalArgumentException("No suite named <" + suiteName
					+ ">.");
		}

		Log.info("Starting benchmark, test <" + shortName + ">, solver <"
				+ solverName + ">, suite <" + suiteName + ">.");

		// This is micro benchmarking with the associated errors. We should
		// probably have a warmup phase to allow JIT magic to happen, but the
		// benchmark need to happen with a single test loaded in memory, and we
		// could end up with the JVM compiling out the results.
		// So, all solvers run without warmup. May the best win.
		//

		long t1 = System.nanoTime();
		StringBuilder sb = new StringBuilder();
		for (T aec : suite.getTestCases()) {
			AbstractCaseResult<T> result = solver.execute(aec);
			result.output(sb);
		}
		long t2 = System.nanoTime();

		Log.info("Execution time: " + ((t2 - t1) * 1e-6) + " msecs");
	}

	void checkSolvers() {
		wrapInit();

		Log.info("Checking solvers.");

		Set<ProblemSolver<T>> s = new HashSet<ProblemSolver<T>>(
				solverMap.values());
		for (Dataset<T> suite : suiteMap.values()) {
			Log.info("Running suite: " + suite.getName());
			for (T test : suite.getTestCases()) {
				AbstractCaseResult<T> referenceRes = null;
				Log.info("Test: " + test);

				for (ProblemSolver<T> solver : s) {
					Log.info("Executing for solver: " + solver.getName()
							+ " ...");
					AbstractCaseResult<T> res = solver.execute(test);
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