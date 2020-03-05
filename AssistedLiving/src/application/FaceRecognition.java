package application;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class FaceRecognition implements Runnable{

	String broker, clientId;
	int qos;
	MqttClient securityClient;
	MemoryPersistence persistence;
	String lockid;

	public FaceRecognition(String lockid) {
		
		this.lockid = lockid;
		int qos = 2;
		String broker = "tcp://localhost:1883"; // "tcp://mqtt.eclipse.org:1883";
		String clientId = lockid;
		persistence = new MemoryPersistence();

		try {
			securityClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Locksensor connecting to broker: " + broker);
			securityClient.connect(connOpts);
			System.out.println("Locksensor connected");
			securityClient.subscribe("lock_in"); // sub to security channel

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
			
			ArrayList<String> facelist = new ArrayList<String>();
			facelist.add("daughter");
			facelist.add("son");
			facelist.add("friend");
			facelist.add("intruder");
			facelist.add("intruder");
			facelist.add("intruder");
			facelist.add("intruder");
			facelist.add("intruder");
			
			String scannedface = facelist.get((int)(System.currentTimeMillis() % facelist.size()));
			// sometimes randomly simulate that someone 
			MqttMessage message = new MqttMessage(("facescanned/"+scannedface+"/"+lockid.toString()).getBytes());
			message.setQos(qos);
			try {
				securityClient.publish("Security", message);
			} catch (MqttPersistenceException e) {
				e.printStackTrace();
			} catch (MqttException e) {
				e.printStackTrace();
			}
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

}