package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Main extends Application {

	TextField lock_status;
	AccessToken token; 
	Authentication auth;
	public static ImageView imgView;
	public static HBox photohBox;
	@Override
	public void start(Stage primaryStage) {
		auth = new Authentication();
		try {

			Dashboard dash = new Dashboard(this);
			PhotoSystem photodash = new PhotoSystem(this);
//			photodash.toggleLock();
			UploadPhoto uploaddash = new UploadPhoto(this);
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
			
			//photo upload window --------------------------------------------------
			Stage photoPopup = new Stage();
			photohBox = new HBox();
			Scene photoscene = new Scene(photohBox, 500, 300);
			
			//photovBox.getChildren().add(imgView);
	        
	        //Store Photo
			Button uploadButton = new Button("Upload Photo");
			photohBox.getChildren().add(uploadButton);
			//++++++++++++++++++++++++++++++++++++++++
	        
	        photoPopup.setScene(photoscene);
	        photoPopup.setTitle("Photo System");
	        photoPopup.show();
	        
	        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent e) {
	             Platform.exit();
	             System.exit(0);
	            }
	          });
	        
	        uploadButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					uploaddash.toggleLock();
				}
			});
	        //----------------------------------------------------------------------
			
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
			//photodash.toggleLock();
			
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
