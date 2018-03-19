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
	public static int mGameCheckMode = -1;
	private RefreshUICallback callback;
	private Process process;
	private StreamGobbler gobbler;
	private StreamGobbler errorGobble;

	public AdbManager(RefreshUICallback callback) {
		this.callback = callback;
	}

	public void runCommand(String command) {
		// check device
		int deviceNum = DeviceManager.getInstance().mDeviceList.size();
		if(deviceNum==0) {
			callback.append("→ error，没有设备连接！！！", 0);
			return;
		}else if(deviceNum>=2) {
			callback.append("→ error，已连接设备太多！！！", 0);
			return;
		}
			
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
		StreamGobbler.GAME_SSO_REQUEST_CHECK = false;
		StreamGobbler.GAME_FILE_CHECK = false;
		StreamGobbler.isProcessAlive = false;

		callback.append("--------stop cmd process--------", 0);
	}

	public String exeCommandThread(String cmd) throws IOException, InterruptedException {
		StreamGobbler.isProcessAlive = true;

		callback.append("--------Run Cmd:" + cmd, 0);

		process = Runtime.getRuntime().exec(cmd);
		// 消费掉IO流，防止程序被阻塞卡死
		// 不同线程执行传入不同的参数
		gobbler = new StreamGobbler(process.getInputStream(), mGameCheckMode, callback);
		errorGobble = new StreamGobbler(process.getErrorStream(), mGameCheckMode, callback);
		gobbler.start();
		errorGobble.start();

		int exitCode = 0;
		// 执行完替换后需要重置状态
		if (mGameCheckMode != StreamGobbler.GAME_PLAYING_CHECK) {
			exitCode = process.waitFor();
		}
		boolean flag = (0 == exitCode);
		return "exitCode:" + flag;

	}

}
