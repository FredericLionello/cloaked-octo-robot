package com.coinsinc.googletest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.coinsinc.googletest.problems.storecredit.StoreCredProblemContainer;
import com.coinsinc.googletest.problems.storecredit.StoreCreditTestCase;

public class ProblemManagerTest {
	
	
	ProblemManager mgr;
	StoreCredProblemContainer container;
	BadSolver badSolver;
	
	@Before
	public void setup() {

		
		mgr = new ProblemManager();
		container = mgr.getContainer(StoreCredProblemContainer.Name, StoreCredProblemContainer.class);
		
		assertNotNull(container);
		
		badSolver = new BadSolver("mybadsolver", container);
		container.addSolver(badSolver);
	}
	
	@Test
	public void testLists() {		
		assertTrue(mgr.getProblemNames().contains(container.getShortName()));
		assertTrue(mgr.getSolverNames(StoreCredProblemContainer.Name).contains(badSolver.getName()));
		
		//	Forcing init to parse data sets.
		container.init();
		assertEquals(2, mgr.getSuiteNames(StoreCredProblemContainer.Name).size());
		assertTrue(mgr.getSuiteNames(StoreCredProblemContainer.Name).contains("small.txt"));
		assertTrue(mgr.getSuiteNames(StoreCredProblemContainer.Name).contains("big.txt"));
	}
	
	@Test
	public void testBadSolverLightCheck() {		
		String lightCheck = container.getLightCheckSolverResult(badSolver);
		Logger.getLogger("test").warning("Light check: " + lightCheck);
		assertEquals("Case #1: 1 2\nCase #2: 1 2\nCase #3: 1 2\n", lightCheck);
	}
	
	@Test(expected=RuntimeException.class)
	public void testRunLightChecks() {
		container.runLightChecks();
	}
	
	@Test
	public void testBench() {
		container.runSolverBenchmark(badSolver.getName(), "small.txt");
	}
	
	
	private class BadSolver extends AbstractProblemSolver<StoreCreditTestCase> {

		public BadSolver(String name,
				AbstractProblemContainer<StoreCreditTestCase> container) {
			super(name, container);
		}

		@Override
		public AbstractCaseResult<StoreCreditTestCase> execute(StoreCreditTestCase test) {
			return test.makeResult(0, 1);
		}
		
	}
}
