package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CompositeLogicSub extends Application{

	GridPane gp;
    TextField tf1;
    Button btnStart, btnClear;
    Label lbl1,lbl2, lbl3, lbl4;
	public CompositeLogicSub() {
		  
	}
	
	public static void main(String[] args) {
	      Application.launch(args);
	   }
	@Override
	   public void start(Stage stage) throws Exception {
	      BorderPane root=new BorderPane();
	      root.setCenter(createInterface());
	      Scene scene = new Scene(root, 500, 300);
	      stage.setTitle("Composite Logic UI");
	      stage.setScene(scene);
	      stage.show();
	   }
	
	private GridPane createInterface() {
		CompositeLogicPub clp = new CompositeLogicPub();
		
		gp = new GridPane();
	    TextField tf1 = new TextField("If <event> {(and/or) <eve\r\n" + 
	    		"nt>}\r\n" + 
	    		"*\r\n" + 
	    		" then {<action>}\r\n" + 
	    		"+");
	    btnStart = new Button("Add composite logic");
	    btnClear = new Button("Clear");
	    lbl1 = new Label();
	    lbl2 = new Label();
	    lbl3 = new Label();
	    lbl4 = new Label();
	      
		btnStart.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	            String inputLogic;
	            inputLogic = tf1.getText();
	            
	            clp.toggleLock(inputLogic);
	         }
	      });
		
		btnClear.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	            tf1.clear();
	         }
	      });
		
		  gp.add(new Label("Enter composite logic: "), 0, 0);
	      gp.add(tf1, 1, 0);
	      gp.add(lbl1, 2, 0);
	      gp.add(btnStart, 0, 1);
	      gp.add(btnClear, 2, 1);
	      gp.add(lbl2, 1, 1);
	      gp.add(lbl3, 0, 2);
	      gp.add(lbl4, 1, 2);
	      gp.setPadding(new Insets(10.0,10.0,10.0,10.0));
	      
	      return gp;
	}
}
