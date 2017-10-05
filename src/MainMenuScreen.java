import javax.microedition.lcdui.*;
import javax.microedition.media.control.*;

public class MainMenuScreen extends GenericScreen implements Runnable {
	private Eliminator midlet;
	static final String[] mainMenu = {"Resume","New Game","HighScore","Settings","Help","Exit"};
	static int menuIdx=1;
	Thread menuThread;
	//public int GameSaved;
	public MainMenuScreen(Eliminator midlet) {
		this.midlet = midlet;
		//GameSaved = midlet.GameSaved;
		//menuIdx = midlet.GameSaved;
		menuThread = new Thread(this);
		menuThread.start();
	}
	public void run() {while(true) {repaint();}}
	public void paint(Graphics g) {
		g.setColor(Black);
		g.fillRect(0,0,getWidth(),getHeight());
		g.drawImage(BackGroundImage, getWidth() / 2, getHeight() / 2, 3);
		for (int i=midlet.GameSaved; i<mainMenu.length; i++) {
			if (i==menuIdx) {
				// CURRENT SELECTION
				g.setFont(LargeBold);
				g.setColor(DarkRed);
				g.drawString(mainMenu[i],getWidth()/2,getHeight()/10 + (i*LargeBold.getHeight()),16|1);
			} else {
				// REST OF MENU ITEMS
				g.setFont(SmallItalic);
				g.setColor(White);
				g.drawString(mainMenu[i],getWidth()/2,getHeight()/10 + (i*LargeBold.getHeight()),16|1);
			}
		}
	}
	protected void keyPressed (int code) {
		//System.out.println("::midlet.GameSaved::"+midlet.GameSaved);
		// RMS SOUND 12 = ON, 13 = OFF
		if (midlet.Sound == 12){beep();}
		// USER MOVES THROUGHT THE MENU WITH UP (KEYNUM 2) AND DOWN (KEYNUM 8)
		if (getGameAction(code) == Canvas.UP){
			// IF A GAME WAS SAVED THE RESUME OPTION MUST APPEAR AT THE MENU
			if (midlet.GameSaved == 0){
				if (menuIdx == 0){menuIdx = 5;}else{menuIdx--;}
			}else{
				if (menuIdx == 1){menuIdx = 5;}else{menuIdx--;}
			}
		}
		if (getGameAction(code) == Canvas.DOWN){
			if (midlet.GameSaved == 0){
				if (menuIdx == 5){menuIdx = 0;}else{menuIdx++;}
			}else{
				if (menuIdx == 5){menuIdx = 1;}else{menuIdx++;}
			}
		}
		// USER SELECTS WITH FIRE (KEYNUM 5)
		// HERE WE MUST CALL THE APPROPRIATE MIDLET METHODS
		if (getGameAction(code) == Canvas.FIRE ) {
			if (menuIdx == 1) {midlet.startPuzzles();}
			if (menuIdx == 2) {midlet.highScoreScreenShow();}
			if (menuIdx == 3) {midlet.settingsScreenShow();}
			if (menuIdx == 4) {midlet.helpScreenShow();}
			if (menuIdx == 5) {midlet.mainMenuScreenQuit();}
			if (menuIdx == 0) {
				midlet.Resume();
				menuIdx++;
			}
		}
	}
}
