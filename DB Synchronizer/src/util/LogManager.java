package util;

public class LogManager {
	public static <T> Logger getLogger(Class<T> handleClass) {
		Logger logger = new Logger(handleClass);
		return logger;
	}

}
