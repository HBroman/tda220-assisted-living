package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicalDataStorage implements MedicalDataStorageInterface {

    public List<Obj> database = new ArrayList<>();

    public void addData(int steps, int pulse, LocalDate date){

        Obj newObj = new Obj(steps, pulse, date);

        database.add(newObj);
    }

    public int getTotalSteps(){
        int totalSteps = 0;
        LocalDate ldObj = LocalDate.now();

        if(database.size() != 0)
         for( int i = 0; i < database.size(); i++){

             if(database.get(i).curDate.toString().equals(ldObj.toString())) {
                 totalSteps = totalSteps + database.get(i).step;
             }
         }
         return totalSteps;

    }


    public double getAveragePulse(){
        double average = 0.0;
        int total = 0;
        LocalDate ldObj = LocalDate.now();


        if(database.size() != 0) {
            for(int i = 0; i < database.size()-1; i++){

                if(database.get(i).curDate.toString().equals(ldObj.toString())) {
                    total = total + database.get(i).pulses;
                }
            }
            average = total/database.size();
        }
        return average;

    }

    private class Obj {

        public int step;
        public LocalDate curDate;
        public int pulses;

        public Obj(int steps, int pulse, LocalDate date){

            step = steps;
            curDate = date;
            pulses = pulse;

        }
    }
}


