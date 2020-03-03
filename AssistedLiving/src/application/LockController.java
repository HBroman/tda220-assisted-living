package application;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class LockController implements MqttCallback, Runnable {

	String broker, clientId;
	int qos;
	MqttClient securityClient;
	MemoryPersistence persistence;
	String lockid;
	boolean isunlocked;

	public LockController(String lockid) {
		this.lockid = lockid;
		this.isunlocked = false;
		
		int qos = 2;
		String broker = "tcp://localhost:1883"; // "tcp://mqtt.eclipse.org:1883";
		String clientId = lockid;
		persistence = new MemoryPersistence();

		try {
			securityClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			securityClient.setCallback(this);
			System.out.println("Locksensor connecting to broker: " + broker);
			securityClient.connect(connOpts);
			System.out.println("Locksensor connected");
			securityClient.subscribe("Lock"+lockid.toString()); // sub to security channel

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep((long)(Math.random() * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// sometimes randomly unlock a door
			
		}
	}
	
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String[] msgarray = message.toString().split("/");
		switch (msgarray[0]) {
		case: "unlock":
			unlock();
		break;
		case: "lock":
			lock();
			break;
		case: "toggle":
			toggle();
			break;
		}
		
	}
	
	public void unlock() {
		isunlocked = true;
		sendUpdate();
	}
	
	public void lock() {
		isunlocked = false;
		sendUpdate();
	}
	
	public void toggleLock() {
		if (isunlocked)
			isunlocked = false;
		else
			isunlocked = true;
		sendUpdate();
	}

	public void sendUpdate() {
		publishString("security", "update/lock"+lockid.toString()+"/"+((isunlocked == true) ? "unlocked" :"locked"));
	}
	
	private void publishString(String topic, String strmessage) {
		MqttMessage message = new MqttMessage(strmessage.getBytes());
		message.setQos(qos);
		try {
			securityClient.publish(topic, message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection() {
		try {
			securityClient.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		System.out.println("Disconnected");
		System.exit(0);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub

	}


}
