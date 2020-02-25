package application;

import java.awt.event.ActionListener;

import javafx.application.Application;
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

public class Main extends Application {

	TextField lock_status;

	@Override
	public void start(Stage primaryStage) {
		try {

			Dashboard dash = new Dashboard(this);
			TabPane tabPane = new TabPane();

			Authentication auth = new Authentication();
			auth.authenticate("Friend","5678");

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

			Tab tab2 = new Tab("Health", new Label("Health"));
			Tab tab3 = new Tab("Basic", new Label("Basic"));

			tabPane.getTabs().add(tab1);
			tabPane.getTabs().add(tab2);
			tabPane.getTabs().add(tab3);

			VBox vBox = new VBox(tabPane);
			Scene scene = new Scene(vBox);

			primaryStage.setScene(scene);
			primaryStage.setTitle("Assisted Living Dashboard");

			primaryStage.show();
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

	public static void main(String[] args) {
		launch(args);

	}
}
