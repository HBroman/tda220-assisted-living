package application;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class PhotoSystem implements MqttCallback{

	String broker, clientId;
	int qos;
	MqttClient photoClient;
	MemoryPersistence persistence;
	Main main;
	
	
	public PhotoSystem(Main main) {
		
		this.main = main;
	    int qos             = 2;
	    String broker       = "tcp://localhost:1883"; //"tcp://mqtt.eclipse.org:1883";
	    String clientId     = "PhotoSystem";
	    persistence = new MemoryPersistence();
	    
		System.out.println("== START PUBLISHER ==");
		try {
			photoClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
	        connOpts.setCleanSession(true);
	        photoClient.setCallback(this);
	        System.out.println("Connecting to Photo System: "+broker);
	        photoClient.connect(connOpts);
	        System.out.println("Connected");
	        photoClient.subscribe("Photo"); // sub to lock channel
		}catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
	    
	}
	
	@Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
    	System.out.println("Photo received: " + message); 
    	main.updateLockInfo();
    	
    	
    }
	
	public void toggleLock() {
		PhotoSlidShow sldshow = new PhotoSlidShow();
        try {
        	//for(;;) {
        		sldshow.imageSlideShow();
        	//}

        } catch(Exception me) {
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
        
	}
	
	public void closeConnection() {
        try {
        	photoClient.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
        System.out.println("Disconnected From PhototSystem");
        System.exit(0);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
