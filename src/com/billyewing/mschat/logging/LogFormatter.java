package com.billyewing.mschat.logging;



import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter
{
    public LogFormatter()
    {
        super();
    }
    
    @Override
    public String format(LogRecord record)
    {
        String output = "";
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date d = new Date();
        output += sdf.format(d);
        
        output += " [" + record.getLevel().getName() + "] " + record.getMessage();
        
        return output;
    }
    
}
