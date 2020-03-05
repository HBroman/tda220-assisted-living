package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UploadPhoto implements MqttCallback{
	
	String broker, clientId;
	int qos;
	MqttClient photoClient;
	MemoryPersistence persistence;
	Main main;
	ImageView myImageView;
	private BufferedImage bufferedImage;
	private FileChooser openFileChooser;
	private Filename filedetails;
	private PhotoStorage phstore;
	private String filname, filextension;
	
	public UploadPhoto(Main main) {
		
		this.main = main;
	    int qos             = 2;
	    String broker       = "tcp://localhost:1883"; //"tcp://mqtt.eclipse.org:1883";
	    String clientId     = "PhotoSystem";
	    persistence = new MemoryPersistence();
	    
		try {
			photoClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
	        connOpts.setCleanSession(true);
	        photoClient.setCallback(this);
	        System.out.println("Connecting to broker: "+broker);
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
        try {
        	openFileChooser = new FileChooser();
			
            //Set extension filter
        	FileChooser.ExtensionFilter imageFilter
            = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        	openFileChooser.getExtensionFilters().addAll(imageFilter);
              
            //Show open file dialog
            File file = openFileChooser.showOpenDialog(null);
           
            if(file != null) {
            	try {
            		filedetails = new Filename(file.toString(), '\\', '.');
                    bufferedImage = ImageIO.read(file);
                    filname = filedetails.filename();
                    filextension = filedetails.extension();
                    phstore = new PhotoStorage();
                    phstore.saveToFile(bufferedImage, filname, filextension);
                } catch (IOException ex) {
                    Logger.getLogger(UploadPhoto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
            	Stage dialogStage = new Stage();
            	dialogStage.initModality(Modality.WINDOW_MODAL);
            	Button okBtn = new Button("OK!");
            	VBox vbox = new VBox(new Text("No file chosen!"), okBtn);
            	vbox.setAlignment(Pos.CENTER);
            	vbox.setPadding(new Insets(15));

            	dialogStage.setScene(new Scene(vbox));
            	dialogStage.show();
            	
            	okBtn.setOnAction(new EventHandler<ActionEvent>() {
    				@Override
    				public void handle(ActionEvent e) {
    					dialogStage.close();
    				}
    			});
            }
            

      

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


