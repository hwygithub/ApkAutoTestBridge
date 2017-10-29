package ui;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;

import device.DeviceManager;
import javax.swing.JTextPane;
import javax.swing.JList;

public class BridgeMainUI {

	private JFrame frame;
	private JPanel panel_bottom;
	private JTextPane panel_devices;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BridgeMainUI window = new BridgeMainUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BridgeMainUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1036, 707);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		panel_bottom = new JPanel();
		panel_bottom.setBounds(10, 10, 242, 168);
		frame.getContentPane().add(panel_bottom);
		panel_bottom.setLayout(null);

		panel_devices = new JTextPane();
		panel_devices.setEditable(false);
		panel_devices.setBounds(0, 0, 240, 168);
		panel_bottom.add(panel_devices);

		startDeviceManager();

	}

	public void startDeviceManager() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DeviceManager.getInstance().start(new RefreshDevicePanel());
			}
		}).start();
	}

	private class RefreshDevicePanel implements RefreshUICallback {

		@Override
		public void Refresh(HashMap<String, String> deviceList) {
			String msg = "";

			Iterator iter = deviceList.keySet().iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				Object val = deviceList.get(key);
				msg += key + "\t" + val;
			}

			panel_devices.setText(msg);
		}
	}
}
