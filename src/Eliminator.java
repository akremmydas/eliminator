import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class Eliminator extends MIDlet {	
	protected Display display;
	private Image splashLogo;
	private int Language;
	private int Difficulty;
	public int Sound;
	public int GameSaved;
	public int SavedMatrix[][] = new int [7][7];
	private boolean isSplash = true;
	// DECLARE ALL SCREENS
	private MainMenuScreen mainMenuScreen;
	private SettingsScreen settingsScreen;
	private HelpScreen helpScreen;
	//private AboutScreen aboutScreen;
	public KeyBoardScreen keyboardScreen;
	public HighScoreScreen highScoreScreen;
	public GameScreen gameScreen;
	// RMS STUFF
	public static Score score;
	public static Settings settings;
	public static SavedGame savedGame;
	private static final String scoreRMSName = "TesseraScore";
	private static final String settingsRMSName = "TesseraSettings";
	private static final String saveRMSName = "TesseraSaved";

	public Eliminator() {}
	public void startApp() {
		display = Display.getDisplay(this);
		// INITIALIZE SCREENS
		keyboardScreen = new KeyBoardScreen(this);
		helpScreen = new HelpScreen(this);
		//aboutScreen = new AboutScreen(this);
		if(isSplash) {
			isSplash = false;
			try {
				score = new Score(scoreRMSName);
				settings = new Settings(settingsRMSName);
				savedGame = new SavedGame(saveRMSName);
				settings.loadSettings();
				savedGame.loadSavedGame();
				getAllSettings();
				getSavedGame();
				//System.out.println("::RMS MATRIX::");
				//printM(SavedMatrix);
				mainMenuScreen = new MainMenuScreen(this);
				settingsScreen = new SettingsScreen(this,Language,Difficulty,Sound);
				highScoreScreen = new HighScoreScreen(this,score);
				splashLogo = Image.createImage("/splash.jpg");
				new SplashScreen(display, mainMenuScreen, splashLogo,3000);
			} catch(Exception ex) {mainMenuScreenShow();}
		} else {
			mainMenuScreenShow();
		}
	}
	public Display getDisplay() {return display;}
	public void pauseApp() {}
	public void destroyApp(boolean unconditional) {
		System.gc();
		notifyDestroyed();
	}
	private Image createImage(String filename) {
		Image image = null;
		try {
			image = Image.createImage(filename);
			} catch (Exception e) {System.out.println("::Create Image:: "+e);}
		return image;
	}
	public void mainMenuScreenShow() {display.setCurrent(mainMenuScreen);}
	public void settingsScreenShow() {display.setCurrent(settingsScreen);}
	protected void highScoreScreenShow() {
		try {
			highScoreScreen.init();
			display.setCurrent(highScoreScreen);
		} catch (Exception e) {System.out.println("HighScoreScreenShow:: "+e);}
	}
	public void helpScreenShow() {display.setCurrent(helpScreen);}
	//public void aboutScreenShow() {display.setCurrent(aboutScreen);}
	public void KeyBoardScreenShow(){display.setCurrent(keyboardScreen);}
	public void mainMenuScreenQuit() {destroyApp(true);}
	public void startPuzzles() {
		try {
			GameScreen gameScreen = new GameScreen(this);
			gameScreen.start();
			display.setCurrent(gameScreen);
		} catch (Exception ex) {System.out.println(ex);}
	}
	public void Resume() {
		try {
			GameScreen gameScreen = new GameScreen(this);
			gameScreen.ResumeGame();
			gameScreen.start();
			display.setCurrent(gameScreen);
		} catch (Exception ex) {System.out.println(ex);}
	}
	public void getAllSettings(){
		Language = settings.getLanguage();
		Difficulty = settings.getDifficulty();
		Sound = settings.getSound();
	}
	public void getSavedGame(){
		GameSaved = savedGame.getGameSaved();
		SavedMatrix = savedGame.getSavedMatrix();
		//System.out.println("::RMS GameSaved::"+this.GameSaved);
	}
	public void setAllSettings(int newLanguage,int newDifficulty, int newSound){
		Language = newLanguage;
		Difficulty = newDifficulty;
		Sound = newSound;
		try{
			settings.updateSettings(newLanguage,newDifficulty,newSound);
		}catch(Exception ex){System.out.println("::setAllSettings::"+ex);}
	}
	public void setSavedGame(int GameSaved,int SavedMatrix[][]){
		this.GameSaved = GameSaved;
		this.SavedMatrix = SavedMatrix;
		//System.out.println("::setSavedGame GameSaved::"+this.GameSaved);
		//System.out.println("::setSavedGame Matrix::");
		//printM(SavedMatrix);
		try{
			savedGame.updateSavedGame(GameSaved,SavedMatrix);
		}catch(Exception ex){System.out.println("::setSavedGame::"+ex);}
	}
	private void printM(int SavedMatrix[][]){//DEBUG
	for (int y = 0; y < 7; ++y){
		for (int x = 0; x < 7; ++x){System.out.print(SavedMatrix[x][y]);}System.out.println();}//END DEBUG
	}
}
