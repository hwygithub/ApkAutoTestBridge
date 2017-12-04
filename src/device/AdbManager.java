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
	private Process process;

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

	public void stopCommand() {
		StreamGobbler.isProcessAlive = false;
	}

	public String exeCommandThread(String cmd) throws IOException, InterruptedException {
		StreamGobbler.isProcessAlive = true;

		callback.append("--------Run Cmd:" + cmd, 0);

		process = Runtime.getRuntime().exec(cmd);
		// 消费掉IO流，防止程序被阻塞卡死
		StreamGobbler gobbler = new StreamGobbler(process.getInputStream(), "normal", callback);
		StreamGobbler errorGobble = new StreamGobbler(process.getErrorStream(), "error", callback);
		gobbler.start();
		errorGobble.start();
		int exitCode = 0;
		if (!StreamGobbler.GAME_SSO_REQUEST_CHECK)
			exitCode = process.waitFor();
		boolean flag = (0 == exitCode);
		return "exitCode:" + flag;

	}

}
