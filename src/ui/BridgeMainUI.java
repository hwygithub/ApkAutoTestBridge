package ui;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import device.AdbManager;
import device.DeviceManager;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Button;
import java.awt.Panel;
import java.awt.List;
import java.awt.Checkbox;
import java.awt.TextField;
import java.awt.Label;
import java.awt.FlowLayout;
import java.awt.TextArea;

public class BridgeMainUI {

	private JFrame frmApkautotestbridge;
	private TextArea txt_area_log;
	private TextField panel_devices;

	private AdbManager mAdbManager;
	private Checkbox cbx_action;
	private Checkbox cbx_game;
	private TextField txt_id;
	private Panel panel;
	private Button btn_pull_resource;
	private Button btn_change_resource;
	private Label label_1;
	private Label label_2;
	private TextField txt_field_remote;
	private TextField txt_field_local;

	private String localUri;
	private static String REMOTE_URI = "/sdcard/tencent/MobileQQ/.apollo";
	private Button btn_open_local;

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
		mAdbManager = new AdbManager(new RefreshLogPanel());
		frmApkautotestbridge = new JFrame();
		frmApkautotestbridge.setTitle("ApkAutoTestBridge");
		frmApkautotestbridge.setBounds(100, 100, 838, 800);
		frmApkautotestbridge.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmApkautotestbridge.getContentPane().setLayout(null);

		Panel panel_control = new Panel();
		panel_control.setBackground(Color.WHITE);
		panel_control.setBounds(260, 10, 232, 45);
		frmApkautotestbridge.getContentPane().add(panel_control);
		panel_control.setLayout(null);

		Checkbox cbx_isclear = new Checkbox("\u662F\u5426\u6E05\u9664\u6570\u636E");
		cbx_isclear.setBounds(10, 10, 101, 23);
		panel_control.add(cbx_isclear);

		Button button = new Button("\u91CD\u542F\u624BQ");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mAdbManager.runCommand("adb shell am force-stop com.tencent.mobileqq");
				if (cbx_isclear.getState()) {
					mAdbManager.runCommand("adb shell pm clear com.tencent.mobileqq");
				}
				mAdbManager.runCommand(
						"adb shell am start -n com.tencent.mobileqq/com.tencent.mobileqq.activity.SplashActivity");
			}
		});
		button.setBounds(117, 10, 76, 23);
		panel_control.add(button);

		panel = new Panel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(260, 61, 232, 263);
		frmApkautotestbridge.getContentPane().add(panel);
		panel.setLayout(null);

		cbx_action = new Checkbox("action");
		cbx_action.setBounds(10, 5, 58, 23);
		panel.add(cbx_action);

		cbx_game = new Checkbox("game");
		cbx_game.setBounds(74, 5, 58, 23);
		panel.add(cbx_game);

		txt_id = new TextField();
		txt_id.setBounds(138, 5, 31, 23);
		panel.add(txt_id);

		Label label = new Label("\u8D44\u6E90ID");
		label.setBounds(175, 5, 50, 23);
		panel.add(label);

		Button btn_del_resource = new Button("\u5220\u9664\u8D44\u6E90");
		btn_del_resource.setBounds(116, 165, 93, 41);
		panel.add(btn_del_resource);

		Button btn_del_json = new Button("\u5220\u9664\u914D\u7F6E");
		btn_del_json.setBounds(10, 165, 93, 41);
		panel.add(btn_del_json);

		btn_pull_resource = new Button("\u62C9\u53D6\u8D44\u6E90");
		btn_pull_resource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String resID = txt_id.getText();

				if (txt_id.getText().equals("0") && cbx_action.getState()) {
					mAdbManager.runCommand("adb pull " + REMOTE_URI + "/action/action_v725.json" + " " + localUri
							+ "/action/action_v725.json");
				}
				if (!txt_id.getText().equals("0") && cbx_action.getState()) {
					mAdbManager.runCommand(
							"adb pull " + REMOTE_URI + "/action/" + resID + " " + localUri + "/action/" + resID);
				}
				if (txt_id.getText().equals("0") && cbx_game.getState()) {
					mAdbManager.runCommand(
							"adb pull " + REMOTE_URI + "/game/game.json" + " " + localUri + "/game/game.json");
				}
				if (!txt_id.getText().equals("0") && cbx_game.getState()) {
					mAdbManager.runCommand(
							"adb pull " + REMOTE_URI + "/game/" + resID + " " + localUri + "/game/" + resID + "/.");
				}
			}
		});
		btn_pull_resource.setBounds(10, 212, 93, 41);
		panel.add(btn_pull_resource);

		btn_change_resource = new Button("\u66FF\u6362\u8D44\u6E90");
		btn_change_resource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String resID = txt_id.getText();

				if (txt_id.getText().equals("0") && cbx_action.getState()) {
					mAdbManager.runCommand(
							"adb push " + localUri + "game/game.json  " + REMOTE_URI + "/action/action_v725.json");
				}
				if (!txt_id.getText().equals("0") && cbx_action.getState()) {
					mAdbManager.runCommand("adb shell rm -r /sdcard/tencent/MobileQQ/.apollo/action/" + resID);
					mAdbManager.runCommand(
							"adb push " + localUri + "/action/" + resID + "/. " + REMOTE_URI + "/action/" + resID);
				}
				if (txt_id.getText().equals("0") && cbx_game.getState()) {
					mAdbManager
							.runCommand("adb push " + localUri + "game/game.json  " + REMOTE_URI + "/game/game.json");
				}
				if (!txt_id.getText().equals("0") && cbx_game.getState()) {
					mAdbManager.runCommand("adb shell rm -r " + localUri + "/game/" + resID);
					mAdbManager.runCommand(
							"adb push " + localUri + "/game/" + resID + " " + REMOTE_URI + "/game/" + resID);
				}

			}
		});
		btn_change_resource.setBounds(116, 212, 93, 41);
		panel.add(btn_change_resource);

		Button btn_choose_local = new Button("\u9009\u62E9");
		btn_choose_local.setBounds(101, 103, 50, 23);
		btn_choose_local.setVisible(true);
		btn_choose_local.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jfc.showDialog(new JLabel(), "选择");
				File file = jfc.getSelectedFile();
				if (null == file) {
					return;
				}
				if (file.isDirectory()) {
					System.out.println("文件夹:" + file.getAbsolutePath());
				} else if (file.isFile()) {
					System.out.println("文件:" + file.getAbsolutePath());
				}

				txt_field_local.setText(file.getAbsolutePath());
				localUri = file.getAbsolutePath();

				System.out.println(jfc.getSelectedFile().getName());

			}
		});
		panel.add(btn_choose_local);

		label_1 = new Label("\u8FDC\u7AEF\u8D44\u6E90\u8DEF\u5F84");
		label_1.setBounds(10, 45, 93, 23);
		panel.add(label_1);

		label_2 = new Label("\u672C\u5730\u8D44\u6E90\u8DEF\u5F84");
		label_2.setBounds(10, 103, 93, 23);
		panel.add(label_2);

		txt_field_remote = new TextField();
		txt_field_remote.setEditable(false);
		txt_field_remote.setText("/sdcard/tencent/MobileQQ/.apollo");
		txt_field_remote.setBounds(10, 74, 199, 23);
		panel.add(txt_field_remote);

		txt_field_local = new TextField();
		txt_field_local.setEditable(false);
		txt_field_local.setBounds(10, 132, 199, 23);
		panel.add(txt_field_local);

		btn_open_local = new Button("\u6253\u5F00");
		btn_open_local.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (null != localUri)
					mAdbManager.runCommand("cmd /c start " + localUri);
			}
		});
		btn_open_local.setBounds(159, 103, 50, 23);
		panel.add(btn_open_local);

		panel_devices = new TextField();
		panel_devices.setBackground(Color.WHITE);
		panel_devices.setEditable(false);
		panel_devices.setBounds(10, 10, 244, 314);
		frmApkautotestbridge.getContentPane().add(panel_devices);

		txt_area_log = new TextArea();
		txt_area_log.setBackground(Color.WHITE);
		txt_area_log.setBounds(10, 332, 815, 420);
		frmApkautotestbridge.getContentPane().add(txt_area_log);
		btn_del_json.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbx_action.getState()) {
					mAdbManager.runCommand("adb shell rm -r /sdcard/tencent/MobileQQ/.apollo/action/action_v725.json");
				}
				if (cbx_game.getState()) {
					mAdbManager.runCommand("adb shell rm -r /sdcard/tencent/MobileQQ/.apollo/game/game.json");
				}
			}
		});
		btn_del_resource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = txt_id.getText();

				if (cbx_action.getState()) {
					mAdbManager.runCommand("adb shell rm -r /sdcard/tencent/MobileQQ/.apollo/action/" + id);
				}
				if (cbx_game.getState()) {
					mAdbManager.runCommand("adb shell rm -r /sdcard/tencent/MobileQQ/.apollo/game/" + id);
				}

			}
		});

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
		public void refresh(HashMap<String, String> deviceList) {
			String msg = "";

			Iterator iter = deviceList.keySet().iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				Object val = deviceList.get(key);
				msg += key + "\t" + val;
			}

			panel_devices.setText(msg);
		}

		@Override
		public void append(String append) {

		}

	}

	private class RefreshLogPanel implements RefreshUICallback {

		@Override
		public void refresh(HashMap<String, String> deviceList) {
		}

		@Override
		public void append(String append) {
			txt_area_log.append(append);
			txt_area_log.append("\n");
		}
	}
}
