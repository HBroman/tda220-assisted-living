package application;

public class Topics {

    public static final String MEDICAL_DEVICE = "medicaldevice/data"; //data from the medical device, goes to bluetooth hub (int numbersteps/ int currentpulse)
    public static final String HEALTH_INFO = "health/info"; //data from bluetooth hub to dashboard (int totalstepstoday/ double averagepulsetoday)
    public static final String LOCK = "lock";
    public static final String BROKER_URL = "tcp://mqtt.eclipse.org:1883"; //"tcp://localhost:1883";
    public static final String MOVEMENT_SECURITY = "security"; // (movement/name/boolean)
    public static final String MOVEMENT_SAFETY = "movement/safety";
    public static final String SMOKE_SAFETY = "smoke/safety";
    public static final String HOME_SAFETY = "home/safety";
    public static final String ALARM = "aalsalarm";
    public static final String SECURITY = "security";
    public static final String DASHBOARD = "aalsdash";
    
}
