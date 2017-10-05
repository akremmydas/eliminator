import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class PlayerSprite extends Sprite {
	private int x,y;
	private int scnWidth;
	public int player_positionX;
	public int player_positionY;
	public static int STEP;
	public PlayerSprite(Image image, int frameWidth, int frameHeight, int scnWidth) throws Exception {
		super(image, frameWidth, frameHeight);
		this.scnWidth = scnWidth;
		STEP = scnWidth/7;
		x = STEP - frameWidth;
		y = STEP - frameWidth;
		player_positionX = 0;
		player_positionY = 0;
	}
	public void startPosition() {setPosition(x,y);}
	public void moveLeft() {
		getXY();
		if (x - STEP > 0){
			x = x - STEP;
			setPosition(x,y);
			player_positionX = player_positionX - 1;
		}
	}
	public void moveRight() {
		getXY();
		if (x + STEP < scnWidth){
			x = x + STEP;
			setPosition(x,y);
			player_positionX = player_positionX + 1;
		}
	}
	public void moveUp() {
		getXY();
		if (y - STEP > 0){
			y = y - STEP;
			setPosition(x,y);
			player_positionY = player_positionY - 1;
		}
	}
	public void moveDown() {
		getXY();
		if (y + STEP < scnWidth){
			y = y + STEP;
			setPosition(x,y);
			player_positionY = player_positionY + 1;
		}
	}
	public void display(Graphics g) {this.paint(g);}
	private void getXY() {
		x = getX();
		y = getY();
	}
}