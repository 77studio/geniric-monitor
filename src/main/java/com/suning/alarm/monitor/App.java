package com.suning.alarm.monitor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ExecuteException, IOException, InterruptedException
    {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    	ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
    	
        CommandLine cmdLine = new CommandLine("ping");
        cmdLine.addArgument("www.baidu.com");

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        ExecuteWatchdog watchdog = new ExecuteWatchdog(5*1000);
        Executor executor = new DefaultExecutor();
        executor.setExitValue(0);
        executor.setWatchdog(watchdog);
        ExecuteStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
		executor.setStreamHandler(streamHandler );
        executor.execute(cmdLine, resultHandler);

        // some time later the result handler callback was invoked so we
        // can safely request the exit value
        resultHandler.waitFor();
        System.out.println("Out: " + outputStream.toString("utf-8"));
        System.out.println("Err: " + errorStream.toString("utf-8"));
    }
}
