package application;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Date;

public class SubscribeCallback implements MqttCallback {
    public MedicalDataStorage storage = new MedicalDataStorage();
    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("Message arrived. Topic: " + s + "  Message: " + mqttMessage.toString());


        String result = mqttMessage.toString();

        String[] splitarray = result.split("/");

        int steps = Integer.parseInt(splitarray[0]);
        int pulse = Integer.parseInt(splitarray[1]);

        Date date = new Date();

        storage.addData(steps, pulse, date);
        System.out.println("Storing data in MedicalDataStorage");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
