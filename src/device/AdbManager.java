package device;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import core.StreamGobbler;
import ui.RefreshUICallback;

public class AdbManager {
	private RefreshUICallback callback;

	public AdbManager(RefreshUICallback callback) {
		this.callback = callback;
	}

	public void runCommand(String command) {
		// callback.append(exeCommand(command));
		try {
			exeCommandThread(command);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String exeCommandThread(String cmd) throws IOException, InterruptedException {
		String msg = String.format("execute cmd:%s", cmd);

		callback.append("--------Run Cmd:" + cmd,0);

		Process process = Runtime.getRuntime().exec(cmd);
		// 消费掉IO流，防止程序被阻塞卡死
		new StreamGobbler(process.getInputStream(), "normal", callback).start();
		new StreamGobbler(process.getErrorStream(), "error", callback).start();

		int exitCode = process.waitFor();
		boolean flag = (0 == exitCode);
		return "exitCode:" + flag;

	}

}
