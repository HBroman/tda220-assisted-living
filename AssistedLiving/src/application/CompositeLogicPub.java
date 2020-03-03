package application;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class CompositeLogicPub implements MqttCallback{
		
		int qosComp;
		MqttClient logicClient;
		MemoryPersistence persistenceCom;
		
		public CompositeLogicPub() {
			qosComp             = 2;
		    String brokerCom       = "tcp://localhost:1883"; //"tcp://mqtt.eclipse.org:1883";
		    String clientIdCom     = "Composite Logic";
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
			}catch(MqttException mes) {
	            System.out.println("reason "+mes.getReasonCode());
	            System.out.println("msg "+mes.getMessage());
	            System.out.println("loc "+mes.getLocalizedMessage());
	            System.out.println("cause "+mes.getCause());
	            System.out.println("excep "+mes);
	            mes.printStackTrace();
	        }
		    
		}
		
		@Override
	    public void messageArrived(String topicms, MqttMessage messag)
	            throws Exception {
	    	System.out.println("Composite Logic Entered: " + messag); 
	    	//main.updateLockInfo();
	    	
	    	
	    }
		
		public void toggleLock() {
			String topicms = "Composite logic";
			CompositeLogicHandler hand = new CompositeLogicHandler();
	        try {
	        	MqttMessage msg = hand.doorbellRinging();
	        	MqttMessage msg2 = hand.doorcamface();
	        	if(msg.toString().equals("yes") && msg2.toString().equals("daughter")) {
	        		msg.setQos(qosComp);
		            msg.setRetained(true);
		            logicClient.publish(topicms,msg);      
		            logicClient.publish(topicms, msg2);
		            System.out.println("Composite Logic Executed");
	        	}
	                   

	        } catch(Exception mes) {
	            System.out.println("msg "+mes.getMessage());
	            System.out.println("loc "+mes.getLocalizedMessage());
	            System.out.println("cause "+mes.getCause());
	            System.out.println("excep "+mes);
	            mes.printStackTrace();
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
