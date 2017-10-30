package ui;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;

import device.AdbManager;
import device.DeviceManager;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import java.awt.Color;

public class BridgeMainUI {

	private JFrame frmApkautotestbridge;
	private JPanel panel_bottom;
	private JTextPane panel_devices;

	private AdbManager mAdbManager;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BridgeMainUI window = new BridgeMainUI();
					window.frmApkautotestbridge.setVisible(true);
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
		mAdbManager = new AdbManager();
		frmApkautotestbridge = new JFrame();
		frmApkautotestbridge.setTitle("ApkAutoTestBridge");
		frmApkautotestbridge.setBounds(100, 100, 851, 550);
		frmApkautotestbridge.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmApkautotestbridge.getContentPane().setLayout(null);

		panel_bottom = new JPanel();
		panel_bottom.setBounds(10, 10, 242, 168);
		frmApkautotestbridge.getContentPane().add(panel_bottom);
		panel_bottom.setLayout(null);

		panel_devices = new JTextPane();
		panel_devices.setBounds(0, 0, 240, 168);
		panel_bottom.add(panel_devices);
		panel_devices.setEditable(false);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.DARK_GRAY);
		panel.setBounds(10, 188, 242, 471);
		frmApkautotestbridge.getContentPane().add(panel);

		
		JCheckBox cbox_isclear = new JCheckBox("\u662F\u5426\u6E05\u9664\u6570\u636E");
		cbox_isclear.setBounds(6, 10, 103, 23);
		panel.add(cbox_isclear);
		
		JButton btnNewButton = new JButton("\u91CD\u542F\u624BQ");
		btnNewButton.setBounds(139, 10, 93, 51);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mAdbManager.runCommand("adb shell am force-stop com.tencent.mobileqq");
				
				if(cbox_isclear.isSelected()) {
					mAdbManager.runCommand("adb shell pm clear com.tencent.mobileqq");
				}
				mAdbManager.runCommand(
						"adb shell am start -n com.tencent.mobileqq/com.tencent.mobileqq.activity.SplashActivity");
			}
		});
		panel.setLayout(null);
		panel.add(btnNewButton);
	

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
