import javax.microedition.lcdui.*;
public class KeyBoardScreen extends GenericScreen implements Runnable {
	private Eliminator midlet;
	private int spc = LargeBold.getHeight()*3/4;  // Spacing between letters
	private int HC = getWidth()/2 - 6*spc/2;      // Horizontal Center
	private int KBYP = getHeight()*3/5 - 3*spc/2; // Keyboard Y Position
	private int Ind = 0; 			      // Holds current Letter
	private String NewName ="";
	public int TotalScore;
	static final String[] KeyBoard = {"A","B","C","D","E","F","G",
					  "H","I","J","K","L","M","N",
					  "O","P","Q","R","S","T","U",
					  "V","W","X","Y","Z","<"," "};
	Thread KeyBoardThread;
	public KeyBoardScreen(Eliminator midlet) {
		this.midlet = midlet;
		KeyBoardThread = new Thread(this);
		KeyBoardThread.start();
	}
	public void run() {while(true) {repaint();}}
	public void paint(Graphics g) {
		g.setColor(Black); //TEST!!!!!!!!!!!!!!!!!!!!!!!!
		g.fillRect(0, 0, getWidth(), getHeight());// TEST!!!!!!!!!!!!!!!
		g.drawImage(BackGroundImage, getWidth() / 2, getHeight() / 2, 3);
		g.setColor(White);
		g.setFont(LargeBold);
		g.drawString("OK", getWidth(), getHeight(), 32|8);
		g.setColor(DarkRed);
		g.setFont(LargeBold);
		g.drawString(NewName, getWidth()/2, getHeight()/4, 32|1);
		// Draw KeyBoard
		int c = 0;
		for (int i=0; i<4; i++){
			for (int j=0; j<7; j++){
				if (c == Ind){
					g.setFont(LargeBold);
					g.setColor(DarkRed);
				}else{
					g.setFont(SmallBold);
					g.setColor(White);
				}
				g.drawString(KeyBoard[c],HC+j*spc,KBYP+i*spc,16|1);
				c++;
			}
		}
	}
	protected void keyPressed (int code) {
		if (midlet.Sound == 12){beep();}
		if (code == -7){
			try{
				midlet.score.updateScores(TotalScore,NewName);
			}catch(Exception e){System.out.println("::UpdateScore:: "+e);}
			midlet.highScoreScreenShow();
		}
		if (getGameAction(code) == Canvas.UP){
			if (Ind <= 6){Ind+=21;}else{Ind -= 7;}
		}
		if (getGameAction(code) == Canvas.DOWN){
			if (Ind >= 21){Ind-=21;}else{Ind += 7;}
		}
		if (getGameAction(code) == Canvas.LEFT ) {
			if (Ind%7 == 0){Ind+=6;}else{Ind --;}
		}
		if (getGameAction(code) == Canvas.RIGHT ) {
			if ((Ind-6)%7 == 0){Ind-=6;}else{Ind ++;}
		}
		if (getGameAction(code) == Canvas.FIRE ) {
			if (KeyBoard[Ind] == "<"){
				if (NewName.length() == 0){
					return;
				}else{
					// DELETE CHARACTER
					NewName = NewName.substring(0,NewName.length()-1);
					return;
				}
			}
			// ONLY 0-7 CHARACTERS LONG
			if (NewName.length() < 7){NewName = NewName + KeyBoard[Ind];}
		}
	}
}
