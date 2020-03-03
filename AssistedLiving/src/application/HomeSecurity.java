package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class HomeSecurity extends Thread implements MqttCallback {

	String broker, clientId;
	int qos;
	MqttClient securityClient;
	MemoryPersistence persistence;
	String fromlocks = "lock_out";
	String fromdash = "security_in";
	String todash = "security_out";
	ArrayList<String> facedb;
	boolean holidaymode = false;

	public HomeSecurity() {
		facedb = new ArrayList<String>();
		facedb.add("daughter");
		facedb.add("son");
		facedb.add("friend");
		int qos = 2;
		String broker = "tcp://localhost:1883"; // "tcp://mqtt.eclipse.org:1883";
		String clientId = "HomeSecurity";
		persistence = new MemoryPersistence();

		try {
			securityClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			securityClient.setCallback(this);
			System.out.println("Connecting to broker: " + broker);
			securityClient.connect(connOpts);
			System.out.println("Connected");
			securityClient.subscribe("Security"); // sub to security channel

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
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String[] msgarray = message.toString().split("/");
		switch (msgarray[0]) {
		case "lock":
			publishString("Lock"+msgarray[1], "lock");
			break;
		case "unlock":
			publishString("Lock"+msgarray[1], "unlock");
			break;
		case "toggle":
			publishString("Lock"+msgarray[1], "toggle");
			break;
		case "facescanned":
			if (facedb.contains(msgarray[1]))
				publishString("Lock"+msgarray[2], "unlock");
			break;
		case "update":
			publishString("Dashboard", "update/"+msgarray[1]+"/"+msgarray[2]);
			break;
		case "holidaymode":
			if (msgarray[1].equals("true"))
				holidaymode = true;
			break;
		case "movement":
			if (msgarray[2].equals("true") && holidaymode == true)
				publishString("SendAlarm", "alarm");
			break;
				
		}
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
	
	private void lockLock(String lockid) {
		publishString("Lock"+lockid, "lock");
	}

	private void unlockLock(String lockid) {
		publishString("Lock"+lockid, "unlock");
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