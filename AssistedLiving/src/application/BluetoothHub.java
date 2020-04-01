package application;

import org.eclipse.paho.client.mqttv3.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

public class BluetoothHub implements MqttCallback {

    private MqttClient SUBmqttClient;
    private MqttClient PUBmqttClient;
    public MedicalDataStorage storage = new MedicalDataStorage();



    public BluetoothHub() {

        LocalDate ld = LocalDate.now();

        storage.addData(0,90,ld);

        try {
            SUBmqttClient = new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());
            PUBmqttClient = new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public void start() throws MqttException {

        subscribe();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        publish();

    }

    //-----------PUBLISH-------------

    public void publish() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setWill(Topics.HEALTH_INFO, "Something has happened".getBytes(), 0, false);

        PUBmqttClient.connect(options);

        while (true) {


            publishData();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void publishData(){
        final MqttTopic dataTopic = PUBmqttClient.getTopic(Topics.HEALTH_INFO);
        int qos = 2;
        int totalSteps = storage.getTotalSteps();
        double averagePulse = storage.getAveragePulse();
        String msg = String.valueOf(totalSteps) + "/" + String.valueOf(averagePulse);

        MqttMessage mqs = new MqttMessage(msg.getBytes());
        mqs.setQos(qos);

        Thread thread = new Thread() {
            public void run() {
                try {
                    System.out.println("Published data. Topic: " + dataTopic.getName() + "  Message: " + msg);
                    dataTopic.publish(mqs);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        //dataTopic.publish(new MqttMessage(msg.getBytes()));
    }

    //-----------SUBSCRIBE-------------
    public void subscribe() throws MqttException {
        SUBmqttClient.setCallback(this);
        SUBmqttClient.connect();

        SUBmqttClient.subscribe(Topics.MEDICAL_DEVICE);

        //System.out.println("Subscriber is now listening to "+ Topics.MEDICAL_DEVICE);
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        //System.out.println("Message arrived. Topic: " + s + "  Message: " + mqttMessage.toString());


        String result = mqttMessage.toString();

        String[] splitarray = result.split("/");

        int steps = Integer.parseInt(splitarray[0]);
        int pulse = Integer.parseInt(splitarray[1]);

        LocalDate ldObj = LocalDate.now();

        storage.addData(steps, pulse, ldObj);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
