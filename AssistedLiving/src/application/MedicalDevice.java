package application;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;


public class MedicalDevice  {



    private MqttClient client;

    public MedicalDevice() throws MqttException

    {
        //We have to generate a unique Client id.
        String clientId = MqttClient.generateClientId();

        try {
            client = new MqttClient(Topics.BROKER_URL, clientId);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void start() {

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill(Topics.MEDICAL_DEVICE, "Something has happened".getBytes(), 0, false);

            client.connect(options);

            while (true) {


                publishData();

                Thread.sleep(5000);

            }
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void publishData(){
        final MqttTopic dataTopic = client.getTopic(Topics.MEDICAL_DEVICE);

        int steps = generateSteps();
        int pulse = generatePulse();

        String msg1 = String.valueOf(steps);
        String msg2 = String.valueOf(pulse);

        String msg = msg1 + "/" + msg2;


        Thread thread = new Thread() {
            public void run() {
                try {
                    dataTopic.publish(new MqttMessage(msg.getBytes()));
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        //dataTopic.publish(new MqttMessage(msg.getBytes()));

        //System.out.println("Published data. Topic: " + dataTopic.getName() + "  Message: " + msg);
    }

    public int generateSteps(){ //generates number of steps the inhabitant has taken since the last sending of data
        int steps = 0;
        Random rand = new Random();
        steps = rand.nextInt(20);

        return steps;
    }

    public int generatePulse(){
        int pulse = 0;

        Random r = new Random();
        int low = 50;
        int high = 120;
        pulse = r.nextInt(high-low) + low;

        return pulse;
    }


}
