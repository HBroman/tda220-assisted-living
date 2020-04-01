package application;

import java.awt.List;
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

public class CompositeLogic implements MqttCallback {

	String broker, clientId;
	int qos;
	MqttClient publishclient, receiveclient;
	MemoryPersistence persistence;
	ArrayList<String> logiclist;

	public CompositeLogic() {
		int qos = 2;

		String clientId = "PhotoCarousel";
		persistence = new MemoryPersistence();
		logiclist = new ArrayList<String>();

		try {
			receiveclient = new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			receiveclient.setCallback(this);
			System.out.println("Connecting to broker: " + broker);
			receiveclient.connect(connOpts);
			System.out.println("Connected");
			receiveclient.subscribe("addcompositelogic");
			receiveclient.subscribe("security");

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();

		}

		try {
			publishclient = new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(false);
			publishclient.setCallback(this);
			System.out.println("Connecting to broker: " + broker);
			publishclient.connect(connOpts);
			System.out.println("Connected");

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
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if (topic.equals("addcompositelogic")) {
			logiclist.add(message.toString());
			System.out.println("ADDED LOGIC  " + message.toString());
		} else
			trylogic(topic, message);
	}

	private void trylogic(String topic, MqttMessage message) {
		if (logiclist.contains("synchronizelocks")) {
			String[] msgarray = message.toString().split("/");
			if (msgarray[0].equals("toggle")) {
				switch (msgarray[1]) {
				case "lock1":
					publishString("lock2", "toggle");
					publishString("lock3", "toggle");
					break;
				case "lock2":
					publishString("lock1", "toggle");
					publishString("lock3", "toggle");
					break;
				case "lock3":
					publishString("lock1", "toggle");
					publishString("lock2", "toggle");
					break;
				}
			}
		}
	}

	private void publishString(String topic, String strmessage) {
		try {
			MqttMessage message = new MqttMessage(strmessage.getBytes());
			message.setQos(qos);
			publishclient.publish(topic, message);

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
			publishclient.disconnect();
			receiveclient.disconnect();
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