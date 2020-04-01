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
	AccessToken token;

	double heartrate = 100.0;
	int steps = 5;
	boolean isunlocked1, isunlocked2, isunlocked3, isholiday;
	String alarm;

	public Dashboard(Main main) throws MqttException {
		int qos = 2;
		persistence = new MemoryPersistence();

		try {
			receiveclient = new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			receiveclient.setCallback(this);
			receiveclient.connect(connOpts);
			receiveclient.subscribe(Topics.DASHBOARD);
			receiveclient.subscribe(Topics.HEALTH_INFO);
			receiveclient.subscribe(Topics.ALARM);
			System.out.println("Dash In Connected");
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
			System.out.println("Dash Out Connected");

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}

	public String getAlarm(){
		String string = alarm;
		alarm = "No alarm now";
		return string;
	}
	public void setAcessToken(AccessToken token) {
		this.token = token;
	}

	public double getHeartRate() {
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

		switch (topic) {
			case Topics.ALARM:
				if(message.toString().equals("alarm/movement")){
					alarm = "There is no movement in any room, contacting closest Health Service Now!";
				}else{
					alarm = "Smoke sensor in " + msgarray[1] + " has gone off, contacting Care Giver now!";
				}
				break;
		case Topics.HEALTH_INFO:
			steps = Integer.parseInt(msgarray[0]);
			heartrate = Double.parseDouble(msgarray[1]);
			break;
		case Topics.DASHBOARD:
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
	
	public void saveLogic(String logic) {
		publishString("addcompositelogic", logic);
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
	
	private void publishString(String topic, String strmessage) {
        try {
            MqttMessage message = new MqttMessage(strmessage.getBytes());
            message.setQos(qos);
            publishclient.publish(topic, message);
            

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
