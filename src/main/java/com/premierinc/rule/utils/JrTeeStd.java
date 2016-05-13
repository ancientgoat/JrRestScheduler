package com.premierinc.rule.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.apache.commons.io.output.TeeOutputStream;

/**
 * Tee STDOUT and STDERR, save for later.
 */
public class JrTeeStd {

	private static ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	private static ByteArrayOutputStream byteErr = new ByteArrayOutputStream();
	private static PrintStream stdOut = System.out;
	private static PrintStream stdErr = System.err;
	private static PrintStream out;
	private static PrintStream err;

	private JrTeeStd() {
	}

	public static void tee() {
		byteOut = new ByteArrayOutputStream();
		byteErr = new ByteArrayOutputStream();
		out = new PrintStream(new TeeOutputStream(System.out, byteOut));
		err = new PrintStream(new TeeOutputStream(System.err, byteErr));
		System.setOut(out);
		System.setErr(err);
	}

	public static void close() {
		if (null != out) {
			out.flush();
		}
		if (null != err) {
			err.flush();
		}
		System.setOut(stdOut);
		System.setErr(stdErr);
	}

	public static String getOut() {
		return byteOut.toString();
	}

	public static String getErr() {
		return byteErr.toString();
	}
}
