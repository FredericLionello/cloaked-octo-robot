package com.coinsinc.googletest.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.coinsinc.googletest.ProblemManager;

public class App {
	private static final Logger Log = Logger.getLogger("App");

	public static void main(String[] args) {
		new App(args);
	}

	private static final String OpHelp = "h";
	private static final String OpMessage = "m";
	private static final String OpCommand = "c";
	private static final String OpProblem = "p";
	private static final String OpSolver = "s";
	private static final String OpSuite = "t";
	private static final String OpVerbose = "v";

	private final Options options = new Options();

	@SuppressWarnings("static-access")
	private App(String[] args) {
		options.addOption(OpHelp, "help", false,
				"Get some help on command line options.");
		options.addOption(OpVerbose, "verbose", false, "Set verbose logging.");

		options.addOption(OptionBuilder.hasArg().withArgName("command")
				.create(OpCommand));

		options.addOption(OptionBuilder.hasArg().withArgName("problem")
				.create(OpProblem));
		options.addOption(OptionBuilder.hasArg().withArgName("solver")
				.create(OpSolver));
		options.addOption(OptionBuilder.hasArg().withArgName("tsuite")
				.create(OpSuite));

		CommandLine line = null;

		try {
			CommandLineParser p = new PosixParser();
			line = p.parse(options, args);
		} catch (ParseException ex) {
			error(ex.getMessage());
		}

		System.out.println(Arrays.toString(line.getOptions()));

		if (line.hasOption(OpHelp) || line.hasOption('c') == false) {
			printHelp();
			System.exit(0);
		}

		if (line.hasOption(OpMessage)) {
			printMessage();
			System.exit(0);
		}

		String command = line.getOptionValue(OpCommand);
		Map<String, Cmd> cmds = fillCmdMap(line);

		if (cmds.containsKey(command) == false) {
			error("command not found <" + command + ">. Supported: "
					+ cmds.keySet());
		}

		ProblemManager mgr = new ProblemManager();
		cmds.get(command).execute(mgr);
	}

	private void print(Object ob) {
		System.out.println(ob.toString());
	}

	private void error(String msg) {
		System.err.println("ERROR: " + msg);
		System.exit(1);
	}

	private String getOption(CommandLine line, String name) {
		if (line.hasOption(name) == false) {
			error("Need option <" + name + ">.");
		}
		return line.getOptionValue(name);
	}

	private Map<String, Cmd> fillCmdMap(final CommandLine line) {
		Map<String, Cmd> cmds = new HashMap<String, Cmd>();

		cmds.put("lspb", new Cmd() {

			public void execute(ProblemManager mgr) {
				print(mgr.getProblemNames());
			}
		});
		cmds.put("lssolver", new Cmd() {

			public void execute(ProblemManager mgr) {

				print(mgr.getSolverNames(getOption(line, OpProblem)));
			}
		});
		cmds.put("lssuite", new Cmd() {

			public void execute(ProblemManager mgr) {
				print(mgr.getSuiteNames(getOption(line, OpProblem)));
			}
		});
		cmds.put("lightcheck", new Cmd() {

			public void execute(ProblemManager mgr) {
				mgr.runLightChecks(getOption(line, OpProblem));
			}
		});
		cmds.put("longcheck", new Cmd() {

			public void execute(ProblemManager mgr) {
				mgr.runLongChecks(getOption(line, OpProblem));
			}
		});
		cmds.put("benchmark", new Cmd() {

			public void execute(ProblemManager mgr) {
				mgr.runBenchmark(getOption(line, OpProblem),
						getOption(line, OpSolver), getOption(line, OpSuite));
			}
		});

		return cmds;
	}

	private void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("utiltity", options);
	}

	private void printMessage() {
		System.out.println("Hello there!");
		assert false;
	}

	interface Cmd {
		void execute(ProblemManager mgr);
	}

}
