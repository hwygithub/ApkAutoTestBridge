package device;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import ui.RefreshUICallback;

public class AdbManager {
	private RefreshUICallback callback;

	public AdbManager(RefreshUICallback callback) {
		this.callback = callback;
	}

	public void runCommand(String command) {
		callback.append(exeCommand(command));
	}

	public String exeCommand(String command) {
		BufferedReader br2 = null;
		String line = null;
		InputStream is = null;
		InputStreamReader isReader = null;
		try {
			Process proc = Runtime.getRuntime().exec(command);
			is = proc.getInputStream();
			isReader = new InputStreamReader(is, "utf-8");
			br2 = new BufferedReader(isReader);
			while ((line = br2.readLine()) != null) {
				return line;
			}
		} catch (IOException e) {
			return line;
		} finally {
			if (isReader != null) {
				try {
					isReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}

			if (br2 != null) {
				try {
					br2.close();
				} catch (IOException e) {
					// TODO
				}
			}
		}
		return "successed! " + " run commond: " + command;
	}

}
