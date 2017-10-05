import javax.microedition.lcdui.*;
public class HelpScreen extends GenericScreen implements Runnable {
	private Eliminator midlet;
	Thread HelpThread;
	public HelpScreen(Eliminator midlet) {
		this.midlet = midlet;
		HelpThread = new Thread(this);
		HelpThread.start();
	}
	public void run() {while(true) {repaint();}}
	public void paint(Graphics g) {
		g.drawImage(BackGroundImage, getWidth() / 2, getHeight() / 2, 3);
		g.setFont(SmallBold);
		g.setColor(White);
		g.drawString("Use 5 to select and", getWidth()/2, SmallBold.getHeight(), 16|1);
		g.drawString("diselect a donut.", getWidth()/2, 2*SmallBold.getHeight(), 16|1);
		g.drawString("2,4,6,8 to move around.", getWidth()/2, 3*SmallBold.getHeight(), 16|1);
		g.drawString("You eat donuts when you", getWidth()/2, 4*SmallBold.getHeight(), 16|1);
		g.drawString("align them by four", getWidth()/2, 5*SmallBold.getHeight(), 16|1);
		g.drawString("...Horizontally", getWidth()/2, 6*SmallBold.getHeight(), 16|1);
		g.drawString("...Vertically", getWidth()/2, 7*SmallBold.getHeight(), 16|1);
		g.drawString("...or Diagonally.", getWidth()/2, 8*SmallBold.getHeight(), 16|1);
		g.drawString("!!! GOOD LUCK!!!", getWidth()/2, 9*SmallBold.getHeight(), 16|1);
	}
	protected void keyPressed (int code) {
	if (midlet.Sound == 12){beep();}
	midlet.mainMenuScreenShow();
	}
}
