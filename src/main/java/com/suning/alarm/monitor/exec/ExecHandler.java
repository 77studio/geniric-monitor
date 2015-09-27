package com.suning.alarm.monitor.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ExecHandler {

	public void onStart(String name);

	public void onStdin(InputStream in);

	public void onStdout(OutputStream out);

	public void onStderr(OutputStream err);

	public void onCompleted(int exitCode);

	public void onFailed(IOException e);
}
