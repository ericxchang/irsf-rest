package com.iconectiv.irsf.util;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iconectiv.ima.snmp.api.iMAHelper;
import com.iconectiv.ima.snmp.common.SNMPException;
import com.iconectiv.irsf.portal.exception.AppException;
 

public class ISRFExceptionHelper {
	private static  Logger logger = LoggerFactory.getLogger(ISRFExceptionHelper.class);
	
	
	public ISRFExceptionHelper()
	{
	}

 
	public static void log(Exception e, String code, String message)
    {
		logger.info((new StringBuilder()).append(new Date(System.currentTimeMillis())).append("::").toString());
        e.printStackTrace();
        logger.error(getExceptionLog(e, code, message));
    }
	

    public static void logAndNotify(Exception e, String code, String message)
    {
        log(e, code, message);
        notify(code, message);
    }
    
     
    public static void log(String code, String message)
    {
        //System.out.print(new Date(System.currentTimeMillis()));
        logger.debug((new StringBuilder()).append(code).append("::").append(message).toString());
    }
  
    public static void logAndNotify(String code, String message)
    {
        log(code, message);
        notify(code, message);
    }

    public static void logAndThrow(Exception e, String code, String message)
        throws AppException
    {
        System.out.println(new Date(System.currentTimeMillis()));
        e.printStackTrace();
        logger.error(getExceptionLog(e, code, message));
        throw new AppException(code, (new StringBuilder()).append(e.getMessage()).append(":").append(message).toString());
    }

    public static void logThrowNotify( String code, String message,Exception e)
        throws AppException
    {
        notify(code, message);
        logAndThrow(e, code, message);
    }

    public static void logAndThrow(Exception e, String code, String message, String addInfo)
        throws AppException
    {
        System.out.println(new Date(System.currentTimeMillis()));
        e.printStackTrace();
        logger.error(getExceptionLog(e, code, message));
        throw new AppException(code, (new StringBuilder()).append(e.getMessage()).append(":").append(message).toString(), addInfo);
    }

    public static void logThrowNotify(Exception e, String code, String message, String addInfo)
        throws AppException
    {
        notify(code, message);
        logAndThrow(e, code, message, addInfo);
    }

    public static void logAndThrow(String code, String message)
        throws AppException
    {
        logger.error((new StringBuilder()).append("Code: ").append(code).append("\nMessage: ").append(message).toString());
        throw new AppException(code, message);
    }

    public static void logThrowNotify(String code, String message)
        throws AppException
    {
        notify(code, message);
        logAndThrow(code, message);
    }
    public static void logThrowNotify(String code, String message, int sev, int cat, String hostName, String moduleName, String processName)
            throws AppException
        {
            notify(code, message, sev,cat,hostName, moduleName, processName);
            logAndThrow(code, message);
        }
    public static void logNotify(String code, String message,  int sev, int cat, String hostName, String moduleName, String processName)
        {
    	 	log(code, message);
            notify(code, message,sev, cat, hostName, moduleName, processName);
           
        }
    public static void logNotifyClear(String code, String message, String hostName, String moduleName, String processName)
            throws SNMPException, UnknownHostException
        {
            notify(code, message,hostName, moduleName, processName);
            log(code, message);
        }
    public static void logAndThrow(String code, String message, String addInfo)
        throws AppException
    {
        logger.error((new StringBuilder()).append("Code: ").append(code).append("\nMessage: ").append(message).toString());
        throw new AppException(code, message, addInfo);
    }

    public static void logAndThrowAndNotify(String code, String message, String addInfo)
        throws AppException
    {
        notify(code, message);
        logAndThrow(code, message, addInfo);
    }

    public static void log(Throwable th)
    {
        String message = getDetailedMessage(th);
        logger.error(message);
    }
    
    public static String getExceptionLog(Throwable exp, String code, String message)
    {
        String detailedMessage = getDetailedMessage(exp);
        StringBuffer lBuffer = new StringBuffer(1024);
        String msg = null;
        if (code != null) {
        	lBuffer.append("Code :");
        	lBuffer.append(code);
        	lBuffer.append("\n");
        }
        lBuffer.append("Message :");
        lBuffer.append(message);
        lBuffer.append("\n");
        lBuffer.append("Exception Stack Trace\n :");
        lBuffer.append(detailedMessage);
        lBuffer.append("\n");
        lBuffer.append("--------------------------------------------------------------------------------------\n");
        msg = lBuffer.toString();
        return msg;
    }
   
    public static void notifySNMP(String eventType, String eventInfo) throws AppException
    {
        
    }
    public static void notifySNMPRaise(String errorCode, String errorMsg, int sev, int cat, String hostName, String moduleName, String processName)
            throws SNMPException, UnknownHostException
        {
    	//public static boolean iMARaiseAlarm(  alarmCode,  alarmMessage, int severity, int category,String hostName, String appName, String moduleName, String processName)
    	boolean retValue = iMAHelper.iMARaiseAlarm(errorCode, errorMsg, sev , cat,InetAddress.getLocalHost().getHostName(), "IRSF", moduleName, processName);
    	
        }
    public static void notifySNMPClear(String errorCode, String errorMsg,String hostName, String moduleName, String processName)
            throws AppException, UnknownHostException, SNMPException
        {
    	
    		boolean retValue = iMAHelper.iMAClearAlarm(errorCode, errorMsg, InetAddress.getLocalHost().getHostName(), "IRSF", moduleName, processName);
    	
        }
    public static void notify(String eventType, String eventInfo)
    {
        try
        {
            notifySNMP(eventType, eventInfo);
        }
        catch(AppException e)
        {
            log(e.getCode(), "Failed to communicate with CM");
        }
    }
    public static void notify(String errorCode, String errorMsg, int sev, int cat, String hostName, String moduleName, String processName) 
    {
        try
        {
        		notifySNMPRaise(errorCode, errorMsg, sev, cat, hostName, moduleName, processName);
        }
        catch (UnknownHostException | SNMPException e1) {
    		log(e1, null, "Failed to communicate with SNMP");
    	}
    }
    public static void notify(String errorCode, String errorMsg, String hostName, String moduleName, String processName) 
    {
        try
        {
        		notifySNMPClear(errorCode, errorMsg, hostName, moduleName, processName);
        	
        }
        catch (UnknownHostException | SNMPException e1) {
    		log("Failed to communicate with SNMP: {}", e1.getMessage());
    	}
        catch(AppException e)
        {
            log(e.getCode(), "Failed to communicate with SNMP");
    	}
    }

    public static String getDetailedMessage(Exception exception)
    {
        StringBuffer msg = new StringBuffer(1024);
        if(exception.getMessage() != null)
        {
            msg.append("Message : ");
            msg.append(exception.getMessage());
            msg.append("\n");
        }
        msg.append("Exception Stack Trace\n");
        try
        {
            StringWriter sw = new StringWriter(1024);
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            msg.append(sw.toString());
            sw.close();
        }
        catch(Exception e)
        {
            msg.append(exception.toString());
        }
        Throwable rootCause = exception.getCause();
        if(rootCause != null)
        {
            msg.append("\n Root Exception Stack Trace : ");
            msg.append(rootCause.toString());
            msg.append("\n");
            try
            {
                StringWriter sw = new StringWriter(1024);
                PrintWriter pw = new PrintWriter(sw);
                rootCause.printStackTrace(pw);
                msg.append(sw.toString());
                sw.close();
            }
            catch(Exception e)
            {
                msg.append(rootCause.toString());
            }
        }
        return msg.toString();
    }

    public static String getDetailedMessage(Throwable a)
    {
        StringBuffer msg = new StringBuffer();
        msg.append("Message : ");
        msg.append(a.getMessage());
        msg.append("\n");
        msg.append("Exception Stack Trace\n");
        try
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            a.printStackTrace(pw);
            msg.append(sw.toString());
            sw.close();
        }
        catch(Exception e)
        {
            msg.append(a.toString());
        }
        String ret = msg.toString();
        msg = null;
        return ret;
    }
     


}
