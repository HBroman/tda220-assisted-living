package application;import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import  java.util.Random;

public class SmokeSensor implements MqttCallback {
    public MqttClient client;

    public int x;
    public boolean smoke;
    public SmokeSensor() {}public static int getRandomNumberInRange(int min, int max) {
        Random rand = new Random();
        //int count=min;
        //int y=0;//Random rand= new Random(System.currentTimeMillis());
//while(rand != )
        {
            //  y = r.nextInt(2);
            // System.out.println("the value is"+rand);
            //count++;
        }
        return  rand.nextInt((max - min) + 1) + min;
    }public static void main(String[] args) {	int randnum =getRandomNumberInRange(2,6);
        System.out.println("Integer between 2 and 6: RandomIntegerNumber = "+randnum);
        if (randnum == 6) {		//this.smoke= true;
            new SmokeSensor().doDemo();
        }
        else{         System.out.println("Please Try again");
            getRandomNumberInRange(2,6);
        }
    }public void doDemo() {
        try {
            client = new MqttClient("tcp://localhost:1883", "Sending");
            client.connect();
            client.setCallback(this);
            client.subscribe("safety");
            MqttMessage message = new MqttMessage();
            message.setPayload("smoke is sensed"
                    .getBytes());
            client.publish("safety", message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println(s);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}