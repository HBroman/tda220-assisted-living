package application;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class LockController implements MqttCallback {

	String broker, clientId;
	int qos;
	MqttClient publishclient, receiveclient;
	MemoryPersistence persistence;
	String lockid;
	boolean isunlocked;

	public LockController(String lockid) {
		this.lockid = lockid;
		this.isunlocked = false;
		
		int qos = 2;
		String clientId = lockid;
		persistence = new MemoryPersistence();

		try {
			receiveclient = new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			receiveclient.setCallback(this);
			receiveclient.connect(connOpts);
			System.out.println("Locksensor"+lockid+" In Connected");
			String substring = "lock"+lockid;
			receiveclient.subscribe(substring); //

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
			publishclient.connect(connOpts);
			System.out.println("Locksensor"+lockid+" Out Connected");

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
		String[] msgarray = message.toString().split("/");
		switch (msgarray[0]) {
		case "unlock":
			unlock();
		break;
		case "lock":
			lock();
			break;
		case "toggle":
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
	
	public void toggle() {
		if (isunlocked)
			isunlocked = false;
		else
			isunlocked = true;
		sendUpdate();
	}

	public void sendUpdate() {
		
		String msgstring = "update/lock"+lockid+"/"+((isunlocked == true) ? "unlocked" :"locked");
		
        try {
            MqttMessage message = new MqttMessage(msgstring.getBytes());
            message.setQos(qos);
            publishclient.publish("security", message);
            

        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
	}
	
	public void closeConnection() {
		try {
			receiveclient.disconnect();
			publishclient.disconnect();
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
