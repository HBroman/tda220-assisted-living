package application;

import java.awt.event.ActionListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Main extends Application {

	Label stepcount, heartrate, lockstatus1, lockstatus2, lockstatus3, holidaystatus;
	TextField logicfield, photofield;
	Dashboard dash;
	AccessToken token; 
	Authentication auth;
	FakeDistributedSystems fds;
	
	public void startSensors() throws MqttException {
		fds = new FakeDistributedSystems();
	}

	@Override
	public void start(Stage primaryStage) {
		auth = new Authentication();
		try {

			dash = new Dashboard(this);

			startSensors();
			MedicalDataStorage healthStorage = new MedicalDataStorage(); //the data from the medical device
			TabPane tabPane = new TabPane();

			// login window
			Platform.setImplicitExit(false); // make sure application doesn't exit when we close popup
			Stage popup = new Stage();
			VBox loginvBox = new VBox();
			Scene popupscene = new Scene(loginvBox);

			TextField usernamefield = new TextField("username");
			TextField passwordfield = new TextField("password");
			Button loginbutton = new Button("Log In");
			loginvBox.getChildren().add(usernamefield);
			loginvBox.getChildren().add(passwordfield);
			loginvBox.getChildren().add(loginbutton);
			
			loginbutton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					if (logIn(usernamefield.getCharacters(), passwordfield.getCharacters())) {
						popup.close();
						Platform.setImplicitExit(true); // make sure application doesn't exit when we close popup
						primaryStage.show();
					}
					else
						System.exit(0);
				}
			});
			
			Button refreshbutton = new Button("refresh");

			
			refreshbutton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					updateAll();
				}
			});
			
			// security tab
			Tab tab1 = new Tab("Security", new Label("Security"));

			Button lockbutton1 = new Button("lock1");
			Button lockbutton2 = new Button("lock2");
			Button lockbutton3 = new Button("lock3");
			Button holidaybutton = new Button("H-Mode");
			
			lockstatus1 = new Label("N/A");
			lockstatus2 = new Label("N/A");
			lockstatus3 = new Label("N/A");
			holidaystatus = new Label("N/A");
			
			HBox securityhbox1 = new HBox();
			securityhbox1.getChildren().add(lockbutton1);
			securityhbox1.getChildren().add(lockstatus1);
			HBox securityhbox2 = new HBox();
			securityhbox2.getChildren().add(lockbutton2);
			securityhbox2.getChildren().add(lockstatus2);
			HBox securityhbox3 = new HBox();
			securityhbox3.getChildren().add(lockbutton3);
			securityhbox3.getChildren().add(lockstatus3);
			HBox securityhbox4 = new HBox();
			securityhbox4.getChildren().add(holidaybutton);
			securityhbox4.getChildren().add(holidaystatus);
			VBox securityvbox = new VBox();
			tab1.setContent(securityvbox);
			
			securityvbox.getChildren().add(securityhbox1);
			securityvbox.getChildren().add(securityhbox2);
			securityvbox.getChildren().add(securityhbox3);
			securityvbox.getChildren().add(securityhbox4);


			lockbutton1.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					dash.toggleLock(1);
					updateAll();
				}
			});
			
			lockbutton2.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					dash.toggleLock(2);
					updateAll();
				}
			});
			
			lockbutton3.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					dash.toggleLock(3);
					updateAll();
				}
			});

			holidaybutton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					dash.toggleHoliday();
					updateAll();
				}
			});

			// Health tab
			
			refreshbutton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					updateAll();
				}
			});
			
			Tab tab2 = new Tab("Health", new Label("Health"));
			
			Label steplabel = new Label("Steps taken:");
			Label heartlabel = new Label("Current Heartrate:");
			stepcount = new Label("N/A");
			heartrate = new Label("N/A");
			
			HBox healthhbox1 = new HBox();
			healthhbox1.getChildren().add(steplabel);
			healthhbox1.getChildren().add(stepcount);
			HBox healthhbox2 = new HBox();
			healthhbox2.getChildren().add(heartlabel);
			healthhbox2.getChildren().add(heartrate);
			HBox healthhbox3 = new HBox();
			HBox healthhbox4 = new HBox();
			VBox healthvbox = new VBox();
			tab2.setContent(healthvbox);
			
			healthvbox.getChildren().add(refreshbutton);
			healthvbox.getChildren().add(healthhbox1);
			healthvbox.getChildren().add(healthhbox2);
			healthvbox.getChildren().add(healthhbox3);
			healthvbox.getChildren().add(healthhbox4);
			
			
			Tab tab3 = new Tab("CompLog", new Label("CompLog"));
			
			Button clogbutton = new Button("Add Logic");
			logicfield = new TextField();
			HBox cloghbox = new HBox();
			cloghbox.getChildren().add(clogbutton);
			cloghbox.getChildren().add(logicfield);
			tab3.setContent(cloghbox);
			
			Tab tab4 = new Tab("Photos", new Label("Photos"));
			
			Button photobutton = new Button("Upload Photo");
			photofield = new TextField();
			HBox photohbox = new HBox();
			photohbox.getChildren().add(photobutton);
			photohbox.getChildren().add(photofield);
			tab4.setContent(photohbox);

			tabPane.getTabs().add(tab1);
			tabPane.getTabs().add(tab2);
			tabPane.getTabs().add(tab3);
			tabPane.getTabs().add(tab4);

			VBox vBox = new VBox(tabPane);
			Scene scene = new Scene(vBox);

			popup.setScene(popupscene);
			popup.setTitle("Log in");
			primaryStage.setScene(scene);
			primaryStage.setTitle("Assisted Living Dashboard");
			
			// service will refresh data every 1 sec
			/*
			ScheduledService<Object> service = new ScheduledService<Object>() {
			     protected Task<Object> createTask() {
			         return new Task<Object>() {
			             protected Object call() {
			                 updateAll();
			                 return null; 
			             }
			         };
			     }
			 };
			 service.setPeriod(Duration.seconds(1)); // refresh interval
			 service.start();
			*/
			
			popup.show();
			

			 
			/*
			 * BorderPane root = new BorderPane(); Scene scene = new Scene(root,400,400);
			 * scene.getStylesheets().add(getClass().getResource("application.css").
			 * toExternalForm()); primaryStage.setScene(scene); primaryStage.show();
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private boolean logIn(CharSequence user, CharSequence pass) {
		String username = user.toString();
		System.out.println(username);
		String password = pass.toString();
		System.out.println(password);
		token = auth.authenticate(username, password);
		dash.setAcessToken(token);
		return true;
	}
	
	private void updateAll() {
		heartrate.setText(String.valueOf(dash.getHeartRate()));
		stepcount.setText(String.valueOf(dash.getSteps()));
		lockstatus1.setText(dash.getIsUnlocked(1) ? "unlocked" : "locked");
		lockstatus2.setText(dash.getIsUnlocked(2) ? "unlocked" : "locked");
		lockstatus3.setText(dash.getIsUnlocked(3) ? "unlocked" : "locked");
		holidaystatus.setText(dash.getIsHoliday() ? "on" : "off");
	}


	public static void main(String[] args) {
		launch(args);

	}
}
