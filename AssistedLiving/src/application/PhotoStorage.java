package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PhotoStorage {
	
	public PhotoStorage() {
		
	}
	
	public void saveToFile(BufferedImage image, String filname, String filextension) {
	    File outputFile = new File("C:\\Users\\TechB\\Desktop\\repos\\AssistedLiving\\images\\"+filname+"."+filextension);
	    BufferedImage bImage = image;
	    try {
	      ImageIO.write(bImage, filextension, outputFile);
	      System.out.println("Photo uploaded!");
	    } catch (IOException e) {
	      throw new RuntimeException(e);
	    }
	  }
}

class Filename {
	  private String fullPath;
	  private char pathSeparator, extensionSeparator;

	  public Filename(String str, char sep, char ext) {
	    fullPath = str;
	    pathSeparator = sep;
	    extensionSeparator = ext;
	  }

	  public String extension() {
	    int dot = fullPath.lastIndexOf(extensionSeparator);
	    return fullPath.substring(dot + 1);
	  }

	  public String filename() { // gets filename without extension
	    int dot = fullPath.lastIndexOf(extensionSeparator);
	    int sep = fullPath.lastIndexOf(pathSeparator);
	    return fullPath.substring(sep + 1, dot);
	  }

	  public String path() {
	    int sep = fullPath.lastIndexOf(pathSeparator);
	    return fullPath.substring(0, sep);
	  }
}