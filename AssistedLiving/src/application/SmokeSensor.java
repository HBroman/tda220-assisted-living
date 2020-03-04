package application;

import java.util.Random;

public class SmokeSensor implements SmokeSensorInterface{

    String sensorName;
    public SmokeSensor(String name){
        sensorName = name;
    }

    public int getSensorData(){
        int smoke = 0;
        Random rand = new Random();
        smoke = rand.nextInt(100);
        return smoke;

    }
}
