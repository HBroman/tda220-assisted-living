package application;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class CompositeLogicPub implements MqttCallback{
		
		String brokerComPub, clientIdComPub;
		int qosComPub;
		MqttClient logicClientComPub;
		MemoryPersistence persistenceComPub;
		ArrayList<String> composelogicPub = new ArrayList<String>();
		
		public CompositeLogicPub() {
			qosComPub            = 2;
			brokerComPub      = "tcp://localhost:1883"; //"tcp://mqtt.eclipse.org:1883";
			clientIdComPub     = "Composite Logic";
			persistenceComPub = new MemoryPersistence();
		    
			try {
				logicClientComPub = new MqttClient(brokerComPub, clientIdComPub, persistenceComPub);
				MqttConnectOptions connOptsCom = new MqttConnectOptions();
		        connOptsCom.setCleanSession(true);
		        logicClientComPub.setCallback(this);
		        System.out.println("Connecting to broker: "+brokerComPub);
		        logicClientComPub.connect(connOptsCom);
		        System.out.println("Connected");
		        logicClientComPub.subscribe("Logic"); // sub to lock channel
			}catch(MqttException meComPub) {
	            System.out.println("reason "+meComPub.getReasonCode());
	            System.out.println("msg "+meComPub.getMessage());
	            System.out.println("loc "+meComPub.getLocalizedMessage());
	            System.out.println("cause "+meComPub.getCause());
	            System.out.println("excep "+meComPub);
	            meComPub.printStackTrace();
	        }
		    
		}
		
		@Override
	    public void messageArrived(String topicCom, MqttMessage messageCom)
	            throws Exception {
	    	System.out.println("Composite Logic Entered: " + messageCom); 
	    	//main.updateLockInfo();
	    	
	    	
	    }
		
		public void toggleLock() {
			String topicms = "Composite logic";
			CompositeLogicHandler hand = new CompositeLogicHandler();
	        try {
	        	MqttMessage msgCom = hand.doorbellRinging();
	        	MqttMessage msgCom2 = hand.doorcamface();
	        	if(msgCom.toString().equals("yes") && msgCom2.toString().equals("daughter")) {
	        		msgCom.setQos(qosComPub);
	        		msgCom2.setQos(qosComPub);
	        		msgCom.setRetained(true);
	        		msgCom2.setRetained(true);
	        		logicClientComPub.publish(topicms,msgCom);      
	        		logicClientComPub.publish(topicms, msgCom2);
		            System.out.println("Composite Logic Executed");
	        	}
	        } catch(Exception mesComUp) {
	            System.out.println("msg "+mesComUp.getMessage());
	            System.out.println("loc "+mesComUp.getLocalizedMessage());
	            System.out.println("cause "+mesComUp.getCause());
	            System.out.println("excep "+mesComUp);
	            mesComUp.printStackTrace();
	        }
	        
		}
		
		public void closeConnection() {
	        try {
	        	logicClientComPub.disconnect();
			} catch (MqttException e) {
				e.printStackTrace();
			}
	        System.out.println("Disconnected From Composite Logic");
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
