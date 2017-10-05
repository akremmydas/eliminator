import javax.microedition.lcdui.*;
import javax.microedition.media.control.*;
import java.lang.String;

public class HighScoreScreen extends GenericScreen implements Runnable {
	private Eliminator midlet;
	private Score score;
	private int spc;
	private String[] names;
	private int[] values;
	Thread HighScoreThread;
	public HighScoreScreen (Eliminator midlet,Score score) throws Exception {
		this.midlet = midlet;
		this.score = score;
		spc = LargeBold.getHeight();// TODO CAN BE IMPLEMENTED IN GENERIC SCREEN
		HighScoreThread = new Thread(this);
		HighScoreThread.start();
	}
	public void run() {while(true){repaint();}}
	public void paint(Graphics g) {
		g.drawImage(BackGroundImage, getWidth() / 2, getHeight() / 2, 3);
		g.setFont(LargeBold);
		g.setColor(White);
		g.drawString("MENU", getWidth()/2, getHeight(), 32|1);
		for(int i=0; i<names.length; i++){
			g.drawString((i+1)+"."+names[i], getWidth()/4, i*spc, 16|4);
			g.drawString("  "+values[i], 2*getWidth()/3, i*spc, 16|4);
		}
	}
	public void init() throws Exception {
		score.loadScores();
		buildHighScore();
	}
	private void buildHighScore() {
		names = score.getNames();
		values = score.getValues();
		return;
	}
	protected void keyPressed (int code) {
		if (midlet.Sound == 12){beep();}
		if (getGameAction(code) == Canvas.FIRE){midlet.mainMenuScreenShow();}
	}
}
