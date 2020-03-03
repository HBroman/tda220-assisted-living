package application;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class FaceRecognition implements MqttCallback, Runnable{

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
			securityClient.setCallback(this);
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
			// sometimes randomly simulate that someone scanned their face
			
		}
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {

		// cases to act on
		System.out.println("Message received: " + message);
	}

	public void toggleLock() {
		String topic = "lock";
		String content = "toggle";
		try {
			System.out.println("Publishing message: " + content);
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);
			securityClient.publish(topic, message);
			System.out.println("Message published");

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
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