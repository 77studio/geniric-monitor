package com.suning.alarm.monitor.exec;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScriptRunnerTest {

	@Before
	public void before() {
		
	}
	
	@After
	public void after() {
		
	}
	
	@Test
	public void test() throws IOException, InterruptedException {
		ScriptRunner runner = new ScriptRunner(new File("/tmp/test.sh"), new String[0], new MockExecHandler());
		runner.setExpectCode(1);
		runner.setTimeout_seconds(2);
		runner.start();
		runner.waitfor();
	}
	
}
