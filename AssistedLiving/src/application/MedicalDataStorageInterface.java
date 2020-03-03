package application;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

public interface MedicalDataStorageInterface {

    void addData(int steps, int pulse, LocalDate date);

    double getAveragePulse() ;

    int getTotalSteps();
}
