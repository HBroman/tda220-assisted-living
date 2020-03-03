	package application;
    
	import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
    import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
    import org.eclipse.paho.client.mqttv3.MqttException;
    import org.eclipse.paho.client.mqttv3.MqttMessage;
    import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Dashboard implements MqttCallback {
	String broker, clientId;
	int qos;
	MqttClient dashClient;
	MemoryPersistence persistence;
	Main main;
	
	public Dashboard(Main main) {
		this.main = main;
	    int qos             = 2;
	    String broker       = "tcp://localhost:1883"; //"tcp://mqtt.eclipse.org:1883";
	    String clientId     = "Dashboard";
	    persistence = new MemoryPersistence();
	    
	    try {
        dashClient = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        dashClient.setCallback(this);
        System.out.println("Connecting to broker: "+broker);
        dashClient.connect(connOpts);
        System.out.println("Connected");
        dashClient.subscribe("lock"); // sub to lock channel
        } catch(MqttException me) {
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
    	System.out.println("Message received: " + message); 
    	main.updateLockInfo();
    	
    	
    }
    
	public void toggleLock() {
		String topic = "lock";
		String content = "toggle";
        try {
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            dashClient.publish(topic, message);
            System.out.println("Message published");
            

        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
        
	}
	
	public void closeConnection() {
        try {
			dashClient.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
        System.out.println("Disconnected");
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

