package com.coinsinc.googletest;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class FileParser {
	private final LineNumberReader lnr;
	private final Reader reader;

	public FileParser(Reader reader) {
		lnr = new LineNumberReader(reader);

		this.reader = reader;
	}

	public String getNextLine() {
		String res;
		try {
			res = lnr.readLine();
		} catch (IOException e) {
			throw new RuntimeException("I/O error reading: " + reader + ": " + e,
					e);
		}

		if (res == null) {
			throw new IllegalStateException("Trying to read past end of file <"
					+ reader + ">.");
		}
		
		System.out.println("FileParser getNextLine: " + res);
 
		return res;
	}

	public void parse() {
		int nbIterations = getNbIterations(getNextLine());
		for (int i = 0; i < nbIterations; i++) {
			parseIteration(i);
		}

		// Check that we reached the end of file.
		//
		try {
			String last = getNextLine();
			throw new RuntimeException("We have not read all lines. Line #"
					+ lnr.getLineNumber() + ", Unread: " + last);
		} catch (IllegalStateException ise) {
			// OK...
		}
	}

	public int toInt(String token) {
		return Integer.parseInt(token);
	}

	public int[] toIntArray(String line, int nbTokens) {
		Scanner scanner = new Scanner(line);
		int[] results = new int[nbTokens];

		int nbFoundTokens = 0;
		while (scanner.hasNext()) {
			nbFoundTokens += 1;

			if (nbFoundTokens > nbTokens) {
				// We will verify nbTokens after the scanning loop.
				//
				break;
			}

			try {
				results[nbFoundTokens - 1] = scanner.nextInt();
			} catch (InputMismatchException ime) {
				throw new RuntimeException("Error parsing int in: " + reader
						+ " line " + lnr.getLineNumber() + " " + ime, ime);
			}
		}

		if (nbFoundTokens != nbTokens) {
			throw new RuntimeException("Error parsing int array in: " + reader
					+ " line " + lnr.getLineNumber() + " should have "
					+ nbTokens + " tokens but has (at least) " + nbFoundTokens);
		}

		return results;
	}

	protected abstract void parseIteration(int i);

	protected int getNbIterations(String firstLine) {
		return this.toInt(firstLine);
	}
}
