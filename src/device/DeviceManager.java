package device;

import java.util.HashMap;
import java.util.Map;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

import ui.RefreshUICallback;

/**
 * 设备管理类 Created by wzj on 2017/8/21.
 */
public class DeviceManager {
	/*
	 * 单例
	 */
	private static DeviceManager INSTANCE = null;

	/**
	 * 包装类
	 */
	private AndroidDebugBridgeWrapper androidDebugBridgeWrapper;

	/**
	 * 设备监听器
	 */
	private DeviceChangeListener deviceChangeListener;
	private RefreshUICallback callback;
	public HashMap<String, String> mDeviceList;

	/**
	 * 私有构造函数
	 */
	private DeviceManager() {
		mDeviceList = new HashMap<String, String>();
	}

	/**
	 * 获取单例类
	 * 
	 * @return DeviceManager
	 */
	public static DeviceManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DeviceManager();
		}

		return INSTANCE;
	}

	/**
	 * 启动方法
	 */
	public void start(RefreshUICallback callback) {
		androidDebugBridgeWrapper = new AndroidDebugBridgeWrapper();
		deviceChangeListener = new DeviceChangeListener();

		androidDebugBridgeWrapper.addDeviceChangeListener(deviceChangeListener);
		androidDebugBridgeWrapper.init(false);

		this.callback = callback;

		System.out.println("Device manager start successful.");
	}

	/**
	 * 销毁方法
	 */
	public void destory() {
		if (androidDebugBridgeWrapper == null) {
			return;
		}

		androidDebugBridgeWrapper.removeDeviceChangeListener(deviceChangeListener);
		androidDebugBridgeWrapper.terminate();
	}

	/**
	 * Created by wzj on 2017/8/21.
	 */
	public class DeviceChangeListener implements AndroidDebugBridge.IDeviceChangeListener {
		/**
		 * Sent when the a device is connected to the {@link AndroidDebugBridge}.
		 * <p>
		 * This is sent from a non UI thread.
		 *
		 * @param device
		 *            the new device.
		 */
		@Override
		public void deviceConnected(IDevice device) {
			String msg = "Device connect " + device.getSerialNumber();
			System.out.println(msg);

			mDeviceList.put(device.getSerialNumber(), "connected");
			callback.refresh(mDeviceList);
		}

		/**
		 * Sent when the a device is connected to the {@link AndroidDebugBridge}.
		 * <p>
		 * This is sent from a non UI thread.
		 *
		 * @param device
		 *            the new device.
		 */
		@Override
		public void deviceDisconnected(IDevice device) {
			String msg = "Device disconnect " + device.getSerialNumber();
			System.out.println(msg);

			mDeviceList.remove(device.getSerialNumber());
			callback.refresh(mDeviceList);
		}

		/**
		 * Sent when a device data changed, or when clients are started/terminated on
		 * the device.
		 * <p>
		 * This is sent from a non UI thread.
		 *
		 * @param device
		 *            the device that was updated.
		 * @param changeMask
		 *            the mask describing what changed. It can contain any of the
		 *            following values: {@link IDevice#CHANGE_BUILD_INFO},
		 *            {@link IDevice#CHANGE_STATE}, {@link IDevice#CHANGE_CLIENT_LIST}
		 */
		@Override
		public void deviceChanged(IDevice device, int changeMask) {
			if (device.isOnline()) {
				String msg = "Device change online " + device.getSerialNumber();
				System.out.println(msg);

				mDeviceList.put(device.getSerialNumber(), "online");
				callback.refresh(mDeviceList);
			} else {
				String msg = "Device change offline " + device.getSerialNumber();
				System.out.println(msg);

				mDeviceList.put(device.getSerialNumber(), "offline");
				callback.refresh(mDeviceList);
			}
		}

	}
}