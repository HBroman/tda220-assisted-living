package application;

import org.eclipse.paho.client.mqttv3.*;

public class AlarmCommunication implements MqttCallback {

    public MqttClient SUBmqttClient;
    public AlarmCommunication(){
        try {
            SUBmqttClient = new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());
            subscribe();
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void subscribe() throws MqttException {
        SUBmqttClient.setCallback(this);
        SUBmqttClient.connect();

        SUBmqttClient.subscribe(Topics.ALARM);

        System.out.println("Subscriber is now listening to "+ Topics.ALARM);
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

        System.out.println("Message received: " + mqttMessage);
        if(mqttMessage.toString().equals("alarm/movement")){
            System.out.println("Contacting closest Health Service contact now!");
        }else{
            System.out.println("Contacting closest care giver now!");
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
