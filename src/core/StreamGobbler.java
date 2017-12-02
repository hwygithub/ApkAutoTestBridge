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
	public static boolean GAME_RES_GBK_CHECK = false;
	public static boolean GAME_SSO_REQUEST_CHECK = false;

	public static boolean isProcessAlive = true;

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

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try {
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				if (!isProcessAlive) {
					reader.close();
					break;
				}
				if (GAME_RES_GBK_CHECK && isChinese(line)) {
					isChinese = true;
					log.append(line + '\n');
					callback.append("\n---------------Exception:has GBK string--------------\n", -1);
					callback.append(line, -1);
					callback.append("\n---------------Exception:has End--------------\n", -1);
				}
				if (GAME_SSO_REQUEST_CHECK) {
					if (line.contains("[doCMGameReq]")) {
						String[] arr = line.split(",");
						callback.append("sso request:" + arr[1], 0);
					}
					if (line.contains("ret")) {
						String[] arr = line.split(",");

						callback.append("→ " + arr[0], 0);
					}
				} else
					callback.append(line, 0);
			}
		} catch (IOException e) {
			callback.append("-------IOException !" + e.toString(), -1);
		} finally {
			if (isChinese) {
				callback.append('\n' + "---------------Exception:has GBK string--------------", -1);
				callback.append('\n' + log.toString(), -1);
			}
			callback.append("\n", 0);
			try {
				reader.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

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