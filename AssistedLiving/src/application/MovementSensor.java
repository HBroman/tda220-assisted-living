package application;

import java.util.Random;

public class MovementSensor implements MovementSensorInterface {

    String sensorName;

    public MovementSensor(String name){
        sensorName = name;

    }


    public int getSensorData(){
        int movement = 0;
        Random rand = new Random();
        movement = rand.nextInt(2);
        return movement;

    }
}
