import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class ObjectSprite extends Sprite {
	public boolean currentlySelected;
	public int objectX;
	public int objectY;
	public int ObjectType;
	public ObjectSprite(Image image, int frameWidth, int frameHeight, int X, int Y, int ObjectType) {//throws Exception {
		super(image, frameWidth, frameHeight);
		objectX = X;
		objectY = Y;
		// Random Generates 0s we need 1-5
		// Add one to the generator
		this.ObjectType = ObjectType + 1;
		if (this.ObjectType == 1){setFrame(0);}
		if (this.ObjectType == 3){setFrame(5);}
		if (this.ObjectType == 2){setFrame(10);}
		if (this.ObjectType == 4){setFrame(15);}
		if (this.ObjectType == 5){setFrame(20);}
		currentlySelected = false;
	}
	public void display(Graphics g) {this.paint(g);}
}
