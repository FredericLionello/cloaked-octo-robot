package com.coinsinc.googletest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProblemManager {

	private final Map<String, AbstractProblemContainer<?>> containerMapByShortName = new LinkedHashMap<String, AbstractProblemContainer<?>>();
	private final Map<Class<?>, AbstractProblemContainer<?>> containerMapByTCClass = new LinkedHashMap<Class<?>, AbstractProblemContainer<?>>();
	

	public ProblemManager() {
		// Perform init.
		//
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"problems/problems.xml", "users/*_solvers.xml");

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
		
		pb.init();

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

	public Set<String> getProblemNames() {
		return containerMapByShortName.keySet();
	}

	public Set<String> getSolverNames(String pbName) {
		AbstractProblemContainer<?> pb = getContainer(pbName);

		return pb.getSolverNames();
	}

	public Set<String> getSuiteNames(String pbName) {
		AbstractProblemContainer<?> pb = getContainer(pbName);

		return pb.getSuiteNames();
	}
	
	public void runLightChecks(String pbName) {
		AbstractProblemContainer<?> pb = getContainer(pbName);
		pb.runLightChecks();
	}
	
	public void runLongChecks(String pbName) {
		AbstractProblemContainer<?> pb = getContainer(pbName);
		pb.runLongChecks();
	}
	
	public void runBenchmark(String pbName, String solverName, String suiteName) {
		AbstractProblemContainer<?> pb = getContainer(pbName);
		pb.runSolverBenchmark(solverName, suiteName);
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
		getContainer(solver.getContainer().getShortName());
		
		// This is convoluted. But I want solvers to plug-in freely
		// and cannot do this in the solver constructor as this would mean
		// reference escape... See AbstractProblemSolver constructor.
		//
		solver.getContainer().addSolver(solver);
	}

	
	static {
		//Logger.getLogger("").addHandler(new ConsoleHandler());
		for (Handler handler: Logger.getLogger("").getHandlers()) {
			Logger.getLogger("").removeHandler(handler);
		}
		ConsoleHandler h = new ConsoleHandler();
		h.setFormatter(new Formatter() {
			
			@Override
			public String format(LogRecord record) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
				Date resultdate = new Date(record.getMillis());
				StringBuilder sb = new StringBuilder();
				String[] split = record.getSourceClassName().split("\\.");
				String cls = split[split.length -1];
				sb.append(sdf.format(resultdate)).append(" ")
					.append(cls)
					.append(" ").append(record.getSourceMethodName())
					.append(" ").append(record.getMessage()).append("\n");
				
				return sb.toString(); 
			}
		});
		Logger.getLogger("").addHandler(h);
		Logger.getLogger("").setLevel(Level.FINEST);
	}

}
