package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PhotoSlidShow {

	// File representing the folder that you select using a FileChooser
    static final File dir = new File("C:\\Users\\TechB\\Desktop\\repos\\AssistedLiving\\images\\");
   // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
        "jpg", "jpeg", "gif", "png", "bmp" // and other formats you need
    };
   Scene scene1;
   BorderPane imageViewWrapper;
   Stage stg;
   HBox hb;
    public PhotoSlidShow() {
    	
    }
    
	// filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
    
    public void imageSlideShow() throws InterruptedException {
    	
    	ArrayList<ImageView> img = new ArrayList<ImageView>();
    	//System.out.println("temp"+wrtemp.length);
    	if (dir.isDirectory()) { // make sure it's a directory
            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                BufferedImage bufferedImage = null;
                WritableImage wr = null;
                System.out.println("yes");
                try {
                	bufferedImage = ImageIO.read(f);
                	
                    if (bufferedImage != null) {
                        wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
                        PixelWriter pw = wr.getPixelWriter();
                        for (int x = 0; x < bufferedImage.getWidth(); x++) {
                            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                                pw.setArgb(x, y, bufferedImage.getRGB(x, y));
                            }
                        }
                    }
                    img.add(new ImageView(wr));
                    //imgView.setImage(wr);
                    
                    
                } catch (final IOException e) {
                    // handle errors here
                }
            }
        }
    	
    	stg = new Stage();
 	    hb = new HBox();
    	scene1 = new Scene(hb,500,300);
    	imageViewWrapper = new BorderPane();
        imageViewWrapper.setBorder(new Border(new BorderStroke(Color.BLACK, 
		            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        hb.getChildren().add(imageViewWrapper);
    	for(ImageView im : img) {
    		imageViewWrapper.getChildren().add(im);
                      
          //Setting the pause transition  
            PauseTransition pause = new PauseTransition(Duration.millis(1000));  
            FadeTransition ft = new FadeTransition();
            //imgView[count].setImage(wr);
           
            ft.setNode(hb);
            ft.setDuration(new Duration(2000));
             ft.setFromValue(1.0);
             ft.setToValue(0.0);
             ft.setCycleCount(0);
             ft.setAutoReverse(true);
             
           //Setting Translate transition  
             TranslateTransition translate = new TranslateTransition(Duration.millis(1000)); 
             translate.setAutoReverse(true);  
             
             SequentialTransition seqT = new SequentialTransition (pause, ft, translate); 
             seqT.play();
             
             stg.setScene(scene1);
             stg.setTitle("Photo Display");
             stg.show();
    	}

    }
    
}
