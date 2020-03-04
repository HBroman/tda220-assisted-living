package application;

import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;

public class MovementDetection {

    public MqttClient PUBSecuritymqttClient;
    public MqttClient PUBSafetymqttClient;
    public List<MovementSensor> allSensors = new ArrayList<>();
    public String currentRoom;

    public MovementDetection() {

        try {

            PUBSafetymqttClient =  new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());
            PUBSecuritymqttClient =  new MqttClient(Topics.BROKER_URL, MqttClient.generateClientId());

            MovementSensor ms1 = new MovementSensor("Livingroom");
            MovementSensor ms2 = new MovementSensor("Bedroom");
            MovementSensor ms3 = new MovementSensor("Bathroom");
            MovementSensor ms4 = new MovementSensor("Kitchen");

            allSensors.add(ms1);
            allSensors.add(ms2);
            allSensors.add(ms3);
            allSensors.add(ms4);

            start();

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }



    public void start() {
        try {

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);

            PUBSecuritymqttClient.connect(options);
            PUBSafetymqttClient.connect(options);

            while (true) {

                int noMove = 0;

                for (int i = 0; i < allSensors.size(); i++) {
                    int sensorData = allSensors.get(i).getSensorData();
                    //System.out.println("Number of movement (or none): " + noMove);
                    Thread.sleep(100);
                    if (sensorData == 0) {
                        noMove = noMove + 1;
                    } else if (sensorData == 1) {
                        currentRoom = allSensors.get(i).sensorName;
                    }
                }

                if (noMove == allSensors.size()) {
                    sendSafety();
                }
                sendSecurity(currentRoom);

            }
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }


    }

    public void sendSecurity(String room) throws InterruptedException, MqttException {

        final MqttTopic security = PUBSecuritymqttClient.getTopic(Topics.MOVEMENT_SECURITY);

        String finalSafeMsg = room + "/true";
        Thread thread = new Thread() {
            public void run() {
                try {
                    security.publish(new MqttMessage(finalSafeMsg.getBytes()));
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        //System.out.println("Published data. Topic: " + security.getName() + "  Message: " + finalSafeMsg);

        thread.sleep(500);

    }

    public void sendSafety() throws InterruptedException, MqttException {
        final MqttTopic safety = PUBSafetymqttClient.getTopic(Topics.MOVEMENT_SAFETY);

        String finalSafeMsg = "movement/false"; //movement/false;
        Thread thread = new Thread() {
            public void run() {
                try {
                    safety.publish(new MqttMessage(finalSafeMsg.getBytes()));
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        System.out.println("Published data. Topic: " + safety.getName() + "  Message: " + finalSafeMsg);

        thread.sleep(1000);

    }
    }

