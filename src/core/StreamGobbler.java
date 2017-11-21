package core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import ui.RefreshUICallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StreamGobbler extends Thread {
	private RefreshUICallback callback;

	private static final String ERROR_MSG = "error";
	private final InputStream is;
	/**
	 * <pre>
	 * 
	 * 是否需要打印错误log
	 * </pre>
	 */
	private final boolean printErrorLog;

	/**
	 * <pre>
	 *  
	 * &#64;param is 输入流 
	 * &#64;param type 类型
	 * </pre>
	 */
	public StreamGobbler(InputStream is, String type, RefreshUICallback callback) {
		super();
		this.callback = callback;
		Preconditions.checkNotNull(is, "InputStream is null.");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(type), "type is null or empty!");
		this.is = is;
		this.printErrorLog = type.trim().equalsIgnoreCase(ERROR_MSG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		boolean isChinese = false;
		StringBuilder log = new StringBuilder();
		StringBuilder result = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try {
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				if (isChinese(line)) {
					isChinese = true;
					log.append(line + '\n');
					result.append("---------------Exception:has GBK string--------------").append('\n');
					result.append(line).append('\n');
					result.append("---------------Exception:has End--------------").append('\n');
				} else
					result.append(line).append('\n');
			}
		} catch (IOException e) {
			System.out.println("-------IOException !" + result.toString());
			callback.append(result.toString());
		} finally {
			if (isChinese) {
				result.append('\n').append("---------------Exception:has GBK string--------------").append('\n');
				result.append(log.toString());
			}
			System.out.println(result.toString());
			callback.append(result.toString());
		}

	}

	private boolean isChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

}