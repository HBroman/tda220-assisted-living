package application;

import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;

public class HomeSafety implements MqttCallback {

    public MqttClient SUBmqttClient;
    public MqttClient PUBSmokemqttClient;
    public MqttClient PUBMovemqttClient;
    public List<SmokeSensor> allSensors = new ArrayList<>();

    public HomeSafety(){
        SmokeSensor ss1 = new SmokeSensor("Kitchen");
        SmokeSensor ss2 = new SmokeSensor("Livingroom");

        allSensors.add(ss1);
        allSensors.add(ss2);

        try {
            SUBmqttClient = new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());
            PUBSmokemqttClient = new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());
            PUBMovemqttClient = new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void start() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);

        PUBSmokemqttClient.connect(options);
        PUBMovemqttClient.connect(options);

        subscribe();
        sendSmokeAlarm();
    }

    private void sendSmokeAlarm() throws MqttException {

        final MqttTopic dataTopic = PUBSmokemqttClient.getTopic(Topics.ALARM);


        Thread thread = new Thread() {
            public void run() {
                try {
                    while(true) {
                        String msg = "";
                        for(int i = 0; i <allSensors.size(); i++){
                            Thread.sleep(5000);
                            int sensorData = allSensors.get(i).getSensorData();
                                msg = "alarm/" + allSensors.get(i).sensorName;
                                String finalMsg = msg;
                                dataTopic.publish(new MqttMessage(finalMsg.getBytes()));

                        }
                    }
                } catch (MqttException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }

    public void sendMovementAlarm() throws MqttException {

        final MqttTopic dataTopic = PUBMovemqttClient.getTopic(Topics.ALARM);
        String finalMsg = "alarm/movement";

        Thread thread = new Thread() {
            public void run() {
                try {
                    dataTopic.publish(new MqttMessage(finalMsg.getBytes()));
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        System.out.println("Published data. Topic: " + dataTopic.getName() + "  Message: " + finalMsg);
    }

    public void subscribe() throws MqttException {
        SUBmqttClient.setCallback(this);
        SUBmqttClient.connect();

        SUBmqttClient.subscribe(Topics.MOVEMENT_SAFETY);

        System.out.println("Subscriber is now listening to "+ Topics.MOVEMENT_SAFETY);
    }


    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

        System.out.println("Message of no movement received at Home Safety. Will send alarm now.");
        sendMovementAlarm();


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
