import javax.microedition.lcdui.*;
public class AboutScreen extends GenericScreen implements Runnable {
	private Eliminator midlet;
	// Thread
	Thread AboutThread;
	// Constructor
	public AboutScreen(Eliminator midlet) {
		this.midlet = midlet;
		// Create Thread and Start
		AboutThread = new Thread(this);
		AboutThread.start();
	}
	public void run() {while(true) {repaint();}}
	public void paint(Graphics g) {
		g.drawImage(BackGroundImage, getWidth() / 2, getHeight() / 2, 3);
		g.setFont(SmallBold);
		g.setColor(White);
		g.drawString("About...", getWidth()/2, getHeight()/2, 16|1);
	}
	protected void keyPressed (int code) {
		if (midlet.Sound == 12){beep();}
		midlet.mainMenuScreenShow();
	}
}
