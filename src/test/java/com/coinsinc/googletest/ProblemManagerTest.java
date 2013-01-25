package com.coinsinc.googletest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

import com.coinsinc.googletest.problems.storecredit.StoreCredProblemContainer;
import com.coinsinc.googletest.problems.storecredit.StoreCreditTestCase;

public class ProblemManagerTest {
	
	
	ProblemManager mgr;
	StoreCredProblemContainer container;
	BadSolver badSolver;
	
	@Before
	public void setup() {
		Logger.getLogger("").addHandler(new ConsoleHandler());
		Logger.getLogger("").setLevel(Level.FINEST);
		Logger.getLogger("").info("here");
		
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
		assertEquals(2, mgr.getSuiteNames(StoreCredProblemContainer.Name).size());
		assertTrue(mgr.getSuiteNames(StoreCredProblemContainer.Name).contains("small.txt"));
		assertTrue(mgr.getSuiteNames(StoreCredProblemContainer.Name).contains("big.txt"));
	}
	
	@Test
	public void testCheck() {		
		String lightCheck = container.getLightCheckSolverResult(badSolver);
		Logger.getLogger("test").warning("Light check: " + lightCheck);
	}
	
	
	private class BadSolver extends AbstractProblemSolver<StoreCreditTestCase> {

		public BadSolver(String name,
				AbstractProblemContainer<StoreCreditTestCase> container) {
			super(name, container);
		}

		@Override
		AbstractCaseResult<StoreCreditTestCase> execute(StoreCreditTestCase test) {
			return test.makeResult(0, 1);
		}
		
	}
}
