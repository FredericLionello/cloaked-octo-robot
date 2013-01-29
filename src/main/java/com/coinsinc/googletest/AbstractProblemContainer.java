package com.coinsinc.googletest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.core.io.Resource;

//import org.springframework.core.io.Resource;

public abstract class AbstractProblemContainer<T extends AbstractTestCase> {

	private final String shortName;
	private boolean initDone = false;
	private final Class<T> testCaseClass;
	private final List<Resource> datasetFiles = new ArrayList<Resource>();
	private final Map<String, Dataset<T>> suiteMap = new LinkedHashMap<String, Dataset<T>>();
	private final Map<String, AbstractProblemSolver<T>> solverMap = new LinkedHashMap<String, AbstractProblemSolver<T>>();
	private Dataset<T> testInput;
	private String testOutput;
	private final Logger logger;

	public AbstractProblemContainer(String name, Class<T> testCaseClass) {
		super();
		this.shortName = name;
		this.testCaseClass = testCaseClass;
		logger = Logger.getLogger("Container<" + name + ">");
	}

	public String getShortName() {
		return shortName;
	}

	public Class<T> getTestCaseClass() {
		return testCaseClass;
	}

	Set<String> getSuiteNames() {
		return suiteMap.keySet();
	}

	Set<String> getSolverNames() {
		return solverMap.keySet();
	}

	public void setTestOutput(Resource testOutput) {
		StringBuilder sb = new StringBuilder();

		try {
			// Trying to get rid of Win/*nix line file difference. Not sure how
			// this is going to work.
			//
			LineNumberReader lnr;
			lnr = new LineNumberReader(new InputStreamReader(testOutput.getInputStream()));

			String line;

			while ((line = lnr.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Cannot read <" + this.testOutput + ">",
					e);
		} catch (IOException e) {
			throw new RuntimeException("Cannot read <" + this.testOutput + ">",
					e);
		}

		this.testOutput = sb.toString();
	}

	public void setTestInput(Resource testInputResource) {
		if (this.testInput != null) {
			throw new IllegalStateException("Only 1 test input allowed.");
		}

		Dataset<T> test = parseDataset(testInputResource);

		this.testInput = test;
	}

	public void init() {
		if (initDone == true) {
			return;
		}

		for (Resource df : datasetFiles) {
			Dataset<T> dataset = parseDataset(df);

			addDataset(dataset);
		}

		if (suiteMap.isEmpty()) {
			throw new IllegalStateException("Problem <" + shortName
					+ "> has no test suite after init.");
		}

		initDone = true;
	}

	private Dataset<T> parseDataset(Resource df) {
		List<T> l = new ArrayList<T>();
		Reader r = null;

		//try {
			try {
				r = new InputStreamReader(df.getInputStream());
			} catch (IOException e) {
				throw new RuntimeException("Cannot open " + df + ": " + e, e);
			}
		/*} catch (FileNotFoundException e) {
			throw new RuntimeException("Cannot open " + df + ": " + e, e);
		}*/

		parseDataset(r, l);
		Dataset<T> dataset = new Dataset<T>(df.getFilename(), l);
		return dataset;
	}

	public List<Resource> getDatasetFiles() {
		return datasetFiles;
	}

	public void setDatasetFiles(List<Resource> datasetFiles) {
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
		assert solver.getContainer().equals(this);

		if (solverMap.containsKey(solver.getName())) {
			throw new RuntimeException("Already a solver named: "
					+ solver.getName());
		}

		solverMap.put(solver.getName(), solver);
	}

	public void runSolverBenchmark(String solverName, String suiteName) {
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
					+ ">. Available: " + suiteMap.keySet());
		}

		logger.info("Starting benchmark, solver <"
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

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));

			out.write(sb.toString());
			out.close();
		} catch (IOException e) {
			throw new RuntimeException("Cannot write result file out.txt", e);
		}

		long t2 = System.nanoTime();

		logger.info("Execution time for <" + solver.getName()
				+ ":" + suite.getName() + "> " + ((t2 - t1) * 1e-3) + " usecs");
	}

	public String getLightCheckSolverResult(AbstractProblemSolver<T> solver) {
		StringBuilder sb = new StringBuilder();
		for (T test : testInput.getTestCases()) {
			AbstractCaseResult<T> res = solver.execute(test);
			res.output(sb);
		}

		return sb.toString();
	}

	public void runLightChecks() {
		if (testInput == null) {
			throw new UnsupportedOperationException(
					"No test input is configured !");
		}
		logger.info("Long checking solvers.");

		for (AbstractProblemSolver<T> solver : solverMap.values()) {
			logger.info("Executing for solver: " + solver.getName() + " ...");

			String res = getLightCheckSolverResult(solver);
			if (res.equals(testOutput) == false) {
				logger.severe("Light check failure.");
				logger.severe("Expecting: \n" + testOutput);
				logger.severe("Got: \n" + res);
				throw new RuntimeException("Light check failure");
			}
		}
		logger.info("Light solver check OK.");
	}

	public void runLongChecks() {
		init();

		logger.info("Long checking solvers.");

		for (Dataset<T> suite : suiteMap.values()) {
			logger.info("Running suite: " + suite.getName());
			for (T test : suite.getTestCases()) {
				AbstractCaseResult<T> referenceRes = null;
				logger.info("Test: " + test);

				for (AbstractProblemSolver<T> solver : solverMap.values()) {
					logger.info("Executing for solver: " + solver.getName()
							+ " ...");
					AbstractCaseResult<T> res = solver.execute(test);
					logger.info("Result for solver: " + solver.getName() + ": "
							+ res);

					if (referenceRes != null) {
						if (res.equals(referenceRes) == false) {
							logger.severe("Different results !");
							throw new RuntimeException("Different results !");
						}
					} else {
						referenceRes = res;
					}
				}
			}
		}

		logger.info("Solver check OK.");
	}
}