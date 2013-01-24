package com.coinsinc.googletest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class AbstractProblemContainer<T extends AbstractTestCase> {
	private static final Logger Log = Logger.getLogger("TestContainer");
	
	private final String shortName;
	private boolean initDone = false;
	private final Class<T> testCaseClass;
	private final List<File> datasetFiles = new ArrayList<File>();
	private final Map<String, Dataset<T>> suiteMap = new LinkedHashMap<String, Dataset<T>>();
	private final Map<String, AbstractProblemSolver<T>> solverMap = new LinkedHashMap<String, AbstractProblemSolver<T>>();
	private Dataset<T> testInput;
	private String testOutputString;

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
	
	public void setTestInput(String testInput) {
		if (this.testInput != null) {
			throw new IllegalStateException("Only 1 test input allowed.");
		}
		List<T> l = new ArrayList<T>();
		Reader r = new StringReader(testInput);
		
		parseDataset(r, l);

		this.testInput = new Dataset<T>("SampleTest", l);
	}

	private void init() {
		if (initDone == true) {
			return;
		}

		for (File df : datasetFiles) {
			List<T> l = new ArrayList<T>();
			Reader r = null;

			try {
				r = new FileReader(df);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Cannot open " + df + ": " + e, e);
			}

			parseDataset(r, l);

			addDataset(new Dataset<T>(df.getName(), l));
		}

		if (suiteMap.isEmpty()) {
			throw new IllegalStateException("Problem <" + shortName
					+ "> has no test suite after init.");
		}

		initDone = true;
	}

	public List<File> getDatasetFiles() {
		return datasetFiles;
	}

	public void setDatasetFiles(List<File> datasetFiles) {
		if (initDone == true || this.datasetFiles.isEmpty() == false) {
			throw new IllegalStateException("Not allowed twice or after init.");
		}

		this.datasetFiles.addAll(datasetFiles);
	}

	protected abstract void parseDataset(Reader reader, List<T> list);

	public void addDataset(Dataset<T> dataset) {
		if (suiteMap.containsKey(dataset.getName()) == true) {
			throw new IllegalArgumentException("Twice the suite named <"
					+ dataset.getName() + ">.");
		}

		suiteMap.put(dataset.getName(), dataset);
	}

	public void addSolver(AbstractProblemSolver<T> solver) {
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
		init();

		// Time and run.
		//
		AbstractProblemSolver<T> solver = solverMap.get(solverName);
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
	
	public String getLightCheckSolverResult(AbstractProblemSolver<T> solver) {
		StringBuilder sb = new StringBuilder();
		for (T test: testInput.getTestCases()) {
			AbstractCaseResult<T> res = solver.execute(test);
			res.appendResult(sb);
		}
		
		return sb.toString();
	}
	
	void lightCheckSolvers() {
		if (testInput == null) {
			throw new UnsupportedOperationException("No test input is configured !");
		}
		Log.info("Long checking solvers.");
		
		for (AbstractProblemSolver<T> solver : solverMap.values()) {
			Log.info("Executing for solver: " + solver.getName()
					+ " ...");

			String res = getLightCheckSolverResult(solver);
			
		}
		Log.info("Solver check OK.");
	}

	void longCheckSolvers() {
		init();

		Log.info("Long checking solvers.");

		for (Dataset<T> suite : suiteMap.values()) {
			Log.info("Running suite: " + suite.getName());
			for (T test : suite.getTestCases()) {
				AbstractCaseResult<T> referenceRes = null;
				Log.info("Test: " + test);

				for (AbstractProblemSolver<T> solver : solverMap.values()) {
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