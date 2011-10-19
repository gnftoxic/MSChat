package com.billyewing.mschat.logging;



import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogHandle extends Handler
{
    FileOutputStream fileOutputStream;
    PrintWriter printWriter;
    
    public LogHandle(String filename)
    {
        super();
        try
        {
            fileOutputStream = new FileOutputStream(new File("server.log"));
            printWriter = new PrintWriter(fileOutputStream);
        } catch(Exception e)
        {
            System.exit(1);
        }
        setFormatter(new LogFormatter());
    }
    
    @Override
    public void publish(LogRecord record)
    {
        if(!isLoggable(record))
            return;
        
        String result = getFormatter().format(record);
        printWriter.println(result);
        System.out.println(result);
    }

    @Override
    public void flush()
    {
        printWriter.flush();
    }

    @Override
    public void close() throws SecurityException
    {
        printWriter.close();
    }
    
}
