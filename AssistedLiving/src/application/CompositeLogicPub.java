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
		
		String brokerCom, clientIdCom;
		int qosCom;
		MqttClient logicClient;
		MemoryPersistence persistenceCom;
		ArrayList<String> composelogic = new ArrayList<String>();
		
		public CompositeLogicPub() {
		    qosCom            = 2;
		    brokerCom      = "tcp://localhost:1883"; //"tcp://mqtt.eclipse.org:1883";
		    clientIdCom     = "Composite Logic";
		    persistenceCom = new MemoryPersistence();
		    
			try {
				logicClient = new MqttClient(brokerCom, clientIdCom, persistenceCom);
				MqttConnectOptions connOptsCom = new MqttConnectOptions();
		        connOptsCom.setCleanSession(true);
		        logicClient.setCallback(this);
		        System.out.println("Connecting to broker: "+brokerCom);
		        logicClient.connect(connOptsCom);
		        System.out.println("Connected");
		        logicClient.subscribe("Logic"); // sub to lock channel
			}catch(MqttException meCom) {
	            System.out.println("reason "+meCom.getReasonCode());
	            System.out.println("msg "+meCom.getMessage());
	            System.out.println("loc "+meCom.getLocalizedMessage());
	            System.out.println("cause "+meCom.getCause());
	            System.out.println("excep "+meCom);
	            meCom.printStackTrace();
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
			System.out.println("logic!");
	        try {
	        	MqttMessage msgCom = hand.doorbellRinging();
	        	MqttMessage msgCom2 = hand.doorcamface();
	        	if(msgCom.toString().equals("yes") && msgCom2.toString().equals("daughter")) {
	        		msgCom.setQos(qosCom);
	        		msgCom2.setQos(qosCom);
	        		msgCom.setRetained(true);
	        		msgCom2.setRetained(true);
		            logicClient.publish(topicms,msgCom);      
		            logicClient.publish(topicms, msgCom2);
		            System.out.println("Composite Logic Executed");
	        	}
	        } catch(Exception mesCom) {
	            System.out.println("msg "+mesCom.getMessage());
	            System.out.println("loc "+mesCom.getLocalizedMessage());
	            System.out.println("cause "+mesCom.getCause());
	            System.out.println("excep "+mesCom);
	            mesCom.printStackTrace();
	        }
	        
		}
		
		public void closeConnection() {
	        try {
	        	logicClient.disconnect();
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
