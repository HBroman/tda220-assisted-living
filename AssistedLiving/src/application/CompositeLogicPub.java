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
		
		String broker, clientId;
		int qos;
		MqttClient logicClient;
		MemoryPersistence persistence;
		CompositeLogicSub main;
		ArrayList<String> composelogic = new ArrayList<String>();
		
		public CompositeLogicPub() {
		    int qos             = 2;
		    String broker       = "tcp://localhost:1883"; //"tcp://mqtt.eclipse.org:1883";
		    String clientId     = "Composite Logic";
		    persistence = new MemoryPersistence();
		    
			try {
				logicClient = new MqttClient(broker, clientId, persistence);
				MqttConnectOptions connOpts = new MqttConnectOptions();
		        connOpts.setCleanSession(true);
		        logicClient.setCallback(this);
		        System.out.println("Connecting to broker: "+broker);
		        logicClient.connect(connOpts);
		        System.out.println("Connected");
		        logicClient.subscribe("Logic"); // sub to lock channel
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
	    	System.out.println("Composite Logic Entered: " + message); 
	    	//main.updateLockInfo();
	    	
	    	
	    }
		
		public void toggleLock(String comlogic) {
			String topic = "Composite logic";
	        try {
	        	composelogic.add(comlogic);
	        	System.out.println("Publishing message: "+comlogic);
	            MqttMessage message = new MqttMessage(comlogic.getBytes());
	            message.setQos(qos);
	            logicClient.publish(topic, message);
	            System.out.println("Composite Logic Entered");       

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
