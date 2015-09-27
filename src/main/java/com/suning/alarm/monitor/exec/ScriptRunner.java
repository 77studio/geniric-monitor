package com.suning.alarm.monitor.exec;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

public class ScriptRunner {
	private File scriptFile;
	private String[] params;

	private File cwd /* = dirname(script) */;
	private int timeout_seconds = 300;
	private int expectCode = 0;

	private OutputStream outputStream;
	private OutputStream errorStream;
	private DefaultExecuteResultHandler resultHandler;
	private ExecHandler execHandler;

	public ScriptRunner(File script, String[] params, ExecHandler handler)
			throws IOException {

		if (!script.exists()) {
			throw new IOException("File " + script.getPath()
					+ "does not exists.");
		}

		if (!script.canExecute()) {
			throw new IOException("File " + script.getPath()
					+ "cannot be executed.");

		}

		this.scriptFile = script;
		this.params = params;
		this.execHandler = handler;

		this.cwd = script.getParentFile();
		if (this.params == null) {
			this.params = new String[0];
		}
		
		this.outputStream = new ByteArrayOutputStream();
		this.errorStream  = new ByteArrayOutputStream();

		resultHandler = new DefaultExecuteResultHandler() {
			public void onProcessComplete(final int exitValue) {
				super.onProcessComplete(exitValue);
				execHandler.onStdout(outputStream);
				execHandler.onStderr(errorStream);
				execHandler.onCompleted(exitValue);
			}

			public void onProcessFailed(final ExecuteException e) {
				execHandler.onStdout(outputStream);
				execHandler.onStderr(errorStream);
				execHandler.onFailed(e);
			}

		};
	}

	public boolean end() {
		return this.resultHandler.hasResult();
	}

	public File getCwd() {
		return cwd;
	}

	public int getExpectCode() {
		return expectCode;
	}

	public String[] getParams() {
		return params;
	}

	public File getScriptFile() {
		return scriptFile;
	}

	public int getTimeout_seconds() {
		return timeout_seconds;
	}

	public void setCwd(File cwd) {
		this.cwd = cwd;
	}

	public void setExpectCode(int expectCode) {
		this.expectCode = expectCode;
	}

	protected void setParams(String[] params) {
		this.params = params;
	}

	protected void setScriptFile(File scriptFile) {
		this.scriptFile = scriptFile;
	}

	public void setTimeout_seconds(int timeout_seconds) {
		this.timeout_seconds = timeout_seconds;
	}

	public void start() throws ExecuteException, IOException {
		CommandLine cmdLine = new CommandLine(this.scriptFile.getAbsolutePath());
		cmdLine.addArguments(this.params);

		Executor executor = new DefaultExecutor();
		executor.setExitValue(this.expectCode);
		executor.setWorkingDirectory(this.cwd);

		ExecuteWatchdog watchdog = new ExecuteWatchdog(this.timeout_seconds);
		executor.setWatchdog(watchdog);

		ExecuteStreamHandler streamHandler = new PumpStreamHandler(
				this.outputStream, this.errorStream);
		executor.setStreamHandler(streamHandler);

		executor.execute(cmdLine, resultHandler);
		this.execHandler.onStart(this.scriptFile.getPath());
	}
	
	public void waitfor() throws InterruptedException {
		this.resultHandler.waitFor(this.timeout_seconds + 1);
	}

}
