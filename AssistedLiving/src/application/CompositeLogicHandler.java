package application;

import java.util.Random;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javafx.application.Application;
import javafx.stage.Stage;

public class CompositeLogicHandler  extends Application{
	    private Random rnd;

	    public CompositeLogicHandler() {
	        
	    }

	    public static void main(String[] args) {
		      Application.launch(args);
		   }
	
	  @Override public void start(Stage stage) throws Exception { 
		  CompositeLogicPub pub = new CompositeLogicPub();
		  pub.toggleLock();
		  }
	 
	    
	    /**
	     * This method simulates reading the engine temperature
	     * @return
	     */
	    public MqttMessage doorbellRinging() { 
	    	rnd = new Random();
	    	String[] doorbell = {"yes", "no"};
	    	int randomNumber=rnd.nextInt(doorbell.length)+0;        
	        byte[] payload = doorbell[randomNumber].getBytes();  
	        MqttMessage msg = new MqttMessage(payload); 
	        return msg;
	    }
	    
	    public MqttMessage doorcamface() {          
	    	rnd = new Random();
	    	String[] face = {"daughter","son","father","mother"};
	    	int randomNumber=rnd.nextInt(face.length)+0;        
	        byte[] payload = face[randomNumber].getBytes();   
	        MqttMessage msg = new MqttMessage(payload); 
	        return msg;
	    }
}
