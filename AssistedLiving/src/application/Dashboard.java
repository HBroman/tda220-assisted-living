package application;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Dashboard implements MqttCallback {
	String broker, clientId;
	int qos;
	MqttClient receiveclient, publishclient;
	MemoryPersistence persistence;
	Main main;
	AccessToken token;

	int heartrate = 10, steps = 5;
	boolean isunlocked1, isunlocked2, isunlocked3, isholiday;

	public Dashboard(Main main) throws MqttException {
		this.main = main;
		int qos = 2;
		String broker = Topics.BROKER_URL; // "tcp://mqtt.eclipse.org:1883";
		String clientId = "Dashboard";
		persistence = new MemoryPersistence();

		try {
			receiveclient = new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			receiveclient.setCallback(this);
			System.out.println("Connecting to broker: " + broker);
			receiveclient.connect(connOpts);
			System.out.println("Connected");
			receiveclient.subscribe(Topics.LOCK); // sub to lock channel
			receiveclient.subscribe(Topics.MEDICAL_DEVICE);
			receiveclient.subscribe("dashboard");
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
		receiveclient.subscribe(Topics.HEALTH_INFO);

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

	public void setAcessToken(AccessToken token) {
		this.token = token;
	}

	public int getHeartRate() {
		return heartrate;
	}

	public int getSteps() {
		return steps;
	}

	public boolean getIsUnlocked(int lockid) {
		switch (lockid) {
		case 1:
			return isunlocked1;
		case 2:
			return isunlocked2;
		case 3:
			return isunlocked3;
		}
		return false;
	}

	public boolean getIsHoliday() {
		return isholiday;
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Dashboard received: " + topic + "   " + message);
		String[] msgarray = message.toString().split("/");
		System.out.println(msgarray[0] + msgarray[1]);
		switch (topic) {
		case Topics.MEDICAL_DEVICE:
			steps = Integer.valueOf(msgarray[0]);
			heartrate = Integer.valueOf(msgarray[1]);
			break;
		case "dashboard":
			switch (msgarray[0]) {
			case "update":
				switch (msgarray[1]) {
				case "lock1":
					if (msgarray[2].equals("unlocked"))
						isunlocked1 = true;
					else
						isunlocked1 = false;
					break;
				case "lock2":
					if (msgarray[2].equals("unlocked"))
						isunlocked2 = true;
					else
						isunlocked2 = false;
					break;
				case "lock3":
					if (msgarray[2].equals("unlocked"))
						isunlocked3 = true;
					else
						isunlocked3 = false;
					break;
				case "holidaymode":
					if (msgarray[2].equals("true"))
						isholiday = true;
					else
						isholiday = false;
				}
				break;
			}
		}
	}

	public void toggleLock(int lockid) {
		if (token.homesecurity) {
			String topic = "security";
			String content = "toggle/lock" + Integer.toString(lockid);
			try {
				MqttMessage message = new MqttMessage(content.getBytes());
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
		else
			System.out.println("ACCESS DENIED");

	}

	public void toggleHoliday() {
		if (token.homesecurity) {
			String topic = "security";
			String content = "holidaymode/toggle";
			try {
				MqttMessage message = new MqttMessage(content.getBytes());
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
		else
			System.out.println("ACCESS DENIED");
	}

	public void closeConnection() {
		try {
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
