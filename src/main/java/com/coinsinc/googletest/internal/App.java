package com.coinsinc.googletest.internal;

import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Hello world!
 * 
 */
public class App {
	private static final Logger Log = Logger.getLogger("App");

	public static void main(String[] args) {
		Log.info("Started.");

		Log.info("Stopping.");
	}

	private static final String OpHelp = "help";
	private static final String OpAppconfig = "appconfig";
	private static final String OpCommand = "command";
	private static final String OpName = "name";
	private static final String OpVerbose = "verbose";

	private final Options options = new Options();

	@SuppressWarnings("static-access")
	private App(String[] args) {
		options.addOption("h", OpHelp, false,
				"Get some help on command line options.");
		options.addOption("v", OpVerbose, false, "Set verbose logging.");

		options.addOption(OptionBuilder.hasArgs().withArgName(OpAppconfig)
				.withValueSeparator(',').create('a'));
		options.addOption(OptionBuilder.hasArg().withArgName(OpCommand)
				.create('c'));

		options.addOption(OptionBuilder.hasArg().withArgName(OpName)
				.create('n'));

		CommandLine line = null;

		try {
			CommandLineParser p = new PosixParser();
			line = p.parse(options, args);
		} catch (ParseException ex) {
			System.err.println("ARG ERROR: " + ex.getMessage());
			System.exit(1);
		}

		if (line.hasOption(OpHelp)) {
			printHelp();
			System.exit(0);
		}

		// First we load up the default appconfig containing init data for all
		// Pb containers and the appconfigs given in parameter (containing user
		// solver init).
		//

		// Then we scan the appconfig object and initialize one ProblemManager
		// instance with relevant data (first containers, then add solvers to
		// containers).

		// Upon command (either "list", "benchmark" or "check", we trigger the
		// relevant command to the relevant ProblemContainer).
		//

		throw new UnsupportedOperationException("Not yet.");
	}

	private void printHelp() {
		assert false;
	}

}
