package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicalDataStorage implements MedicalDataStorageInterface {

    public List<Obj> database = new ArrayList<>();

    public void addData(int steps, int pulse, Date date){

        Obj newObj = new Obj(steps, pulse, date);

        database.add(newObj);
    }

    private class Obj {

        public int step;
        public Date curDate;
        public int pulses;

        public Obj(int steps, int pulse, Date date){

            step = steps;
            curDate = date;
            pulses = pulse;

        }
    }
}


