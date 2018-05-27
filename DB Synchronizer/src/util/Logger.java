package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
	Class<?> handleClass;
	SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	Calendar calendar = Calendar.getInstance();
	String logFolder=Constans.logproperty;

	public Logger(Class<?> handleClass) {
		super();
		this.handleClass = handleClass;
	}

	public void info(String message) {
		appendLog(" [INFO] " +handleClass.getName() +  " "+message);
	}

	public void error(String message) {
		appendLog(" [ERROR] "+handleClass.getName() +  " "+message);
	}

	public void error(String message, Exception e) {
		appendLog(" [ERROR] "+handleClass.getName() + " "+message);
		appendLog(" [ERROR] "+handleClass.getName() + " "+genErrorMsg(e));
	}
	public void debug(String message) {
		appendLog(" [DEBUG] "+handleClass.getName() +  " "+message);
	}

	public void debug(String message, Exception e) {
		appendLog(" [DEBUG] "+handleClass.getName() + " "+message);
		appendLog(" [DEBUG] "+handleClass.getName() + " "+genErrorMsg(e));
	}

	public String getTime() {
		return sf.format(calendar.getTime());
	}

	public String getLogFilePath() {
		String logFilePath = null;
		if (logFolder != null) {
			File folderFile = new File(logFolder);
			if (folderFile.exists()) {
				String year = "" + calendar.get(Calendar.YEAR);
				String month = "" + (calendar.get(Calendar.MONTH) + 1);
				if (month != null && month.length() < 2) {
					month = "0" + month;
				}
				String date = "" + calendar.get(Calendar.DATE);
				logFilePath = logFolder + File.separator + year + month + File.separator + year + month + date + ".log";
			}
		}
		return logFilePath;
	}

	public void appendLog(String message) {
		String logFilePath = getLogFilePath();
		try {
			synchronized (Logger.class) {
				System.out.println(getTime() + " " + message);
				if(logFilePath!=null) {
					File logFile = new File(logFilePath);
					if (!logFile.exists()) {
						logFile.getParentFile().mkdirs();
						logFile.createNewFile();
					}
					OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(logFile,true),"UTF-8");
					out.append(getTime() + " " + message + " \r\n");
					out.flush();
					out.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String genErrorMsg(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		e.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}
}
