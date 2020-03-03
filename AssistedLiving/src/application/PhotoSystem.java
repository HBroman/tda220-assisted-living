package application;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class PhotoSystem implements MqttCallback{

	String brokerPhoSys, clientIdPhoSys;
	int qosPhoSys;
	MqttClient photoClientPhoSys;
	MemoryPersistence persistencePhoSys;
	Main mainPhoSys;
	
	
	public PhotoSystem(Main mainPhoSys) {
		
		this.mainPhoSys = mainPhoSys;
	    brokerPhoSys       = "tcp://localhost:1883"; //"tcp://mqtt.eclipse.org:1883";
	    clientIdPhoSys     = "PhotoSystem";
	    persistencePhoSys = new MemoryPersistence();
	    
		System.out.println("== START PUBLISHER ==");
		try {
			photoClientPhoSys = new MqttClient(brokerPhoSys, clientIdPhoSys, persistencePhoSys);
			MqttConnectOptions connOpts = new MqttConnectOptions();
	        connOpts.setCleanSession(true);
	        photoClientPhoSys.setCallback(this);
	        System.out.println("Connecting to Photo System: "+brokerPhoSys);
	        photoClientPhoSys.connect(connOpts);
	        System.out.println("Connected");
	        photoClientPhoSys.subscribe("Photo"); // sub to lock channel
		}catch(MqttException mePhoSys) {
            System.out.println("reason "+mePhoSys.getReasonCode());
            System.out.println("msg "+mePhoSys.getMessage());
            System.out.println("loc "+mePhoSys.getLocalizedMessage());
            System.out.println("cause "+mePhoSys.getCause());
            System.out.println("excep "+mePhoSys);
            mePhoSys.printStackTrace();
        }
	    
	}
	
	@Override
    public void messageArrived(String topic, MqttMessage messagePhoSys)
            throws Exception {
    	System.out.println("Photo received: " + messagePhoSys); 
    	mainPhoSys.updateLockInfo();
    	
    	
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
        	photoClientPhoSys.disconnect();
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
