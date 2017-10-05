import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class PowerUps extends Sprite {

public int PowerUpX;
public int PowerUpY;
public int PowerUpType;
public PowerUps(Image image, int frameWidth, int frameHeight, int X, int Y, int PowerUpType) {
super(image, frameWidth, frameHeight);
this.PowerUpType = PowerUpType;
PowerUpX = X;
PowerUpY = Y;
if (PowerUpType == 20){setFrame(0);}
if (PowerUpType == 30){setFrame(1);}
if (PowerUpType == 40){setFrame(2);}
}

public void display(Graphics g) {this.paint(g);}
}
