package application;

import org.eclipse.paho.client.mqttv3.MqttException;

public class FakeDistributedSystems {
	
	public FakeDistributedSystems() {
		
		BluetoothHub hub = new BluetoothHub();
		
		HomeSecurity hs = new HomeSecurity();
		MedicalDataStorage healthStorage = new MedicalDataStorage(); //the data from the medical device
		CompositeLogic cl = new CompositeLogic();
		LockController lock1 = new LockController("1");
		LockController lock2 = new LockController("2");
		LockController lock3 = new LockController("3");
		
		
		try {
			MedicalDevice medDev = new MedicalDevice();
			Thread thread1 = new Thread() {
				public void run() {
					medDev.start();
				}
			};
			thread1.start();
		} catch (MqttException e1) {
			e1.printStackTrace();
		}

		Thread thread2 = new Thread() {
			public void run() {
				try {
					hub.start();
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		};
		thread2.start();

		Thread thread3 = new Thread() {
			public void run() {
				MovementDetection move = new MovementDetection();
			}
		};
		thread3.start();

		Thread thread4 = new Thread() {
			public void run() {
				try {
					HomeSafety hm = new HomeSafety();
					hm.start();
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		};
		thread4.start();

		Thread thread5 = new Thread() {
			public void run() {
				AlarmCommunication alarm = new AlarmCommunication();
			}
		};
		thread5.start();
		
	}
}
