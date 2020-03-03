package application;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Collector {

    private MqttClient mqttClient;
    public static final String BROKER_URL = "tcp://localhost:1883";
    public static final String TOPIC = "medicaldevice/data";

    public Collector() {

        try {
            mqttClient = new MqttClient(BROKER_URL, MqttClient.generateClientId());

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void start() {
        try {

            mqttClient.setCallback(new SubscribeCallback());
            mqttClient.connect();

            mqttClient.subscribe(TOPIC);

            System.out.println("Subscriber is now listening to "+ TOPIC);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
