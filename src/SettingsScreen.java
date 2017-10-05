import javax.microedition.lcdui.*;
public class SettingsScreen extends GenericScreen implements Runnable {
	static final String[] SettingsMenu = {"Lanuage",	//0
						"English",	//1  DEFAULT LANGUAGE
						"Greek",	//2
						"Italiano",	//3
						"Espaniol",	//4
						"Deutsch",	//5
						"Francais",	//6
					      "Difficulty",	//7
						"Rooky",	//8  DEFAULT DIFFICULTY
						"Average",	//9
						"Expert",	//10
					      "Sound",		//11
						"On",		//12 DEFAULT SOUND
						"Off"};		//13
	private Eliminator midlet;
	private int LngInd;
	private int DifInd;
	private int SndInd;
	private int MnuSett;
	private int spc = LargeBold.getHeight();
	private int Language;
	private int Difficulty;
	private int Sound;
	// Thread
	Thread SettingsThread;
	// Constructor
	public SettingsScreen(Eliminator midlet,int Language,int Difficulty,int Sound) {
		this.midlet = midlet;
		this.Language = Language;
		this.Difficulty = Difficulty;
		this.Sound = Sound;
		LngInd = Language;
		DifInd = Difficulty;
		SndInd = Sound;
		MnuSett = 0;
		// Create Thread and Start
		SettingsThread = new Thread(this);
		SettingsThread.start();
	}
	public void run() {while(true) {repaint();}}
	public void paint(Graphics g) {
		g.setColor(Black);
		g.fillRect(0, 0, getWidth(), getHeight()); 
		g.drawImage(BackGroundImage, getWidth() / 2, getHeight() / 2, 3);
		g.setFont(LargeBold);
		if (MnuSett == 0){
			g.setColor(DarkRed);
			g.drawString(SettingsMenu[0], getWidth()/2, getHeight()/5, 16|1);
		}else{
			g.setColor(White);
			g.drawString(SettingsMenu[0], getWidth()/2, getHeight()/5, 16|1);
		}
		if (MnuSett == 7){
			g.setColor(DarkRed);
			g.drawString(SettingsMenu[7], getWidth()/2, 2*spc+getHeight()/5, 16|1);
		}else{
			g.setColor(White);
			g.drawString(SettingsMenu[7], getWidth()/2, 2*spc+getHeight()/5, 16|1);
		}
		if (MnuSett == 11){
			g.setColor(DarkRed);
			g.drawString(SettingsMenu[11], getWidth()/2, 4*spc+getHeight()/5, 16|1);
		}else{
			g.setColor(White);
			g.drawString(SettingsMenu[11], getWidth()/2, 4*spc+getHeight()/5, 16|1);
		}
		g.setFont(SmallBold);
		g.setColor(White);
			g.drawString(SettingsMenu[LngInd], getWidth()/2, spc+getHeight()/5, 16|1);
			g.drawString(SettingsMenu[DifInd], getWidth()/2, 3*spc+getHeight()/5, 16|1);
			g.drawString(SettingsMenu[SndInd], getWidth()/2, 5*spc+getHeight()/5, 16|1);
	}
	protected void keyPressed (int code) {
		if (midlet.Sound == 12){beep();}
		if (getGameAction(code) == Canvas.UP){
			if (MnuSett == 0){
				MnuSett = 11;
				return;
			}
			if (MnuSett == 11){
				MnuSett = 7;
				return;
			}
			if (MnuSett == 7){
				MnuSett = 0;
				return;
			}
		}
		if (getGameAction(code) == Canvas.DOWN){
			if (MnuSett == 0){
				MnuSett = 7;
				return;
			}
			if (MnuSett == 7){
				MnuSett = 11;
				return;
			}
			if (MnuSett == 11){
				MnuSett = 0;
				return;
			}
		}
		if (getGameAction(code) == Canvas.LEFT){
			if (MnuSett == 0){
				if (LngInd == 1){LngInd = 6;}else{LngInd--;}
			}
			if (MnuSett == 7){
				if (DifInd == 8){DifInd = 10;}else{DifInd--;}
			}
			if (MnuSett == 11){
				if (SndInd == 12){SndInd = 13;}else{SndInd--;}
			}
			midlet.setAllSettings(LngInd, DifInd, SndInd);
		}
		if (getGameAction(code) == Canvas.RIGHT){
			if (MnuSett == 0){
				if (LngInd == 6){LngInd = 1;}else{LngInd++;}
			}
			if (MnuSett == 7){
				if (DifInd == 10){DifInd = 8;}else{DifInd++;}
			}
			if (MnuSett == 11){
				if (SndInd == 13){SndInd = 12;}else{SndInd++;}
			}
			midlet.setAllSettings(LngInd, DifInd, SndInd);
		}
		if (getGameAction(code) == Canvas.FIRE ) {
			midlet.mainMenuScreenShow();
		}

	}
}
