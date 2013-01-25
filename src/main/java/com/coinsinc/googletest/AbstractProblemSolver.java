package com.coinsinc.googletest;

public abstract class AbstractProblemSolver<T extends AbstractTestCase> {
	private final String name;
	private final AbstractProblemContainer<T> container;

	public AbstractProblemSolver(String name,
			AbstractProblemContainer<T> container) {
		super();
		this.name = name;
		this.container = container;

		// I do not do container.addSolver(this) here, as this would mean the
		// "this" reference would escape before all other constructors of "this"
		// (i.e. constructor of the derived class) is ever called. This would be
		// bad. But I agree that the initialization as I do it here has a code
		// smell...
		//
		// One way to fix this would be to use static factory methods, but that
		// does not play well with inheritance. The factory pattern would be
		// good, but it would force every solver to implement a factory just to
		// avoid one smell in generic code. Let's keep it straight for solvers
		// and a little funny looking under the covers for now.
	}

	public String getName() {
		return name;
	}

	public AbstractProblemContainer<T> getContainer() {
		return container;
	}

	abstract AbstractCaseResult<T> execute(T test);

}
