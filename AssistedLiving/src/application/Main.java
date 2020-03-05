package application;

import java.awt.event.ActionListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Main extends Application {

	TextField lock_status;
	AccessToken token; 
	Authentication auth;
	public void startSensors() throws MqttException {
		BluetoothHub hub = new BluetoothHub();
		MedicalDevice medDev = new MedicalDevice();


		Thread thread1 = new Thread() {
			public void run() {
				medDev.start();

			}
		};
		thread1.start();

		Thread thread2 = new Thread() {
			public void run() {
				try {
					hub.start();
				} catch (MqttException e) {
					e.printStackTrace();
				}

			}
		};
		thread2.start();
	}
	@Override
	public void start(Stage primaryStage) {
		auth = new Authentication();
		try {

			Dashboard dash = new Dashboard(this);

			startSensors();
			MedicalDataStorage healthStorage = new MedicalDataStorage(); //the data from the medical device

			MovementDetection move = new MovementDetection();
			move.main();
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

			// security tab
			Tab tab1 = new Tab("Security", new Label("Security"));

			HBox hbox = new HBox();
			Button lock_button = new Button("Toggle Lock");
			hbox.getChildren().add(lock_button);
			lock_status = new TextField("Unknown");
			hbox.getChildren().add(lock_status);
			tab1.setContent(hbox);

			lock_button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					dash.toggleLock();
				}
			});

			loginbutton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					if (logIn(usernamefield.getCharacters(), passwordfield.getCharacters())) {
						popup.close();
						Platform.setImplicitExit(true); // make sure application doesn't exit when we close popup
					}
				}
			});
			Tab tab2 = new Tab("Health", new Label("Health"));
			Tab tab3 = new Tab("Basic", new Label("Basic"));

			tabPane.getTabs().add(tab1);
			tabPane.getTabs().add(tab2);
			tabPane.getTabs().add(tab3);

			VBox vBox = new VBox(tabPane);
			Scene scene = new Scene(vBox);

			popup.setScene(popupscene);
			popup.setTitle("Log in");
			primaryStage.setScene(scene);
			primaryStage.setTitle("Assisted Living Dashboard");

			primaryStage.show();
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

	public void updateLockInfo() {
		if (lock_status.getText().contentEquals("Locked"))
			lock_status.setText("Unlocked");
		else
			lock_status.setText("Locked");
	}

	private boolean logIn(CharSequence user, CharSequence pass) {
		String username = user.toString();
		System.out.println(username);
		String password = pass.toString();
		System.out.println(password);
		token = auth.authenticate(username, password);
		return true;
	}

	public static void main(String[] args) {
		launch(args);

	}
}
