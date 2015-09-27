package com.suning.alarm.monitor.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MockExecHandler implements ExecHandler {

	String name = "";
	
	public void onStart(String name) {
		this.name = name;
		String msg = "onStart:" + name;
		print(msg);
	}

	private void print(String msg) {
		System.out.println(msg);
	}

	public void onStdin(InputStream in) {
		
	}

	public void onStdout(OutputStream out) {
		print("onstdout:" + out.toString());
		
	}

	public void onStderr(OutputStream err) {
		print("onStderr:" + err.toString());
	}

	public void onCompleted(int exitCode) {
		print("oncompleted:" + this.name + ", exitCode:" + exitCode);
	}

	public void onFailed(IOException e) {
		print("onFailed:" + this.name + ", ex:" + e.getMessage());
	}

}
