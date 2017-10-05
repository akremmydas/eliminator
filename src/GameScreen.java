import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.media.control.*;
import java.util.*;

public class GameScreen extends GameCanvas implements Runnable{
private static final int MILLIS_PER_TICK = 200;
// Routing Stuff
private static final int GO_EMPTY = 4;
private static final int GO_A = 5;
private static final int GO_B = 6;
private static final int GO_BALL = 7;

private int width;
private int InRow;
private int pf[][] = new int [7][7];
private int Route[][] = new int [49][2];
private int routeIndex = 0;
// Animation of Selected object
private int jump = 0;
private int TheMatrix[][] = new int [7][7];
private int RandomX;
private int RandomY;
private int SelectedObjectX;
private int SelectedObjectY;
private int ObjectType;
private int ObjectNum;

private Eliminator midlet;
private Thread gameThread = null;
private LayerManager layerManager;
private Random random;

// Flags
private boolean MoveFlag = false;
private boolean FoundLine;
private boolean isPlay;
private boolean Pause;
private boolean ObjectSelected;
private boolean gameInterupted = true;
private boolean ResumingGame = false;
//private int GameSaved = 0;
// STEP Equals width/7 
public static int STEP;
//Score related variables
public int TotalScore = 0;
// Sprites
private PlayerSprite player;
private ObjectSprite object;
private Sprite menuSprite;
private Sprite GameOverSprite;;
// Variables to hold object info
private Vector Objects;
private Image ObjectsImages;

// Constructor and initialization
public GameScreen(Eliminator midlet) throws Exception {
super(true);
this.midlet = midlet;
width = getWidth();
isPlay = true;
Pause = false;
ObjectSelected = false;
STEP = width/7;
InRow = 1;
ObjectNum = 4;
setFullScreenMode(true);
random = new Random();
/*
 Setup Algorithm Matrixes
 Numbering in the Matrix mean
 
 0 - Empty
 1 - Green Circle
 2 - Red Circle
 3 - Yellow Triangle
 4 - blue Circle
 5 - Cowlike Square
 9 - To be removed 
 */
// We initialize the Matrix with all positions empty
for (int y = 0; y < 7; ++y){
	for (int x = 0; x < 7; ++x){TheMatrix[x][y] = 0;}
}
//Setup Route Matrixes
for(int z = 0; z < 49; z++){
	Route[z][0] = 8;
	Route[z][1] = 8;
}
//Setup Menu Sprite
Image MenuImage = Image.createImage("/menu.png");
menuSprite = new Sprite (MenuImage);
menuSprite.setPosition(getWidth()-MenuImage.getWidth()-1,getHeight()-MenuImage.getHeight()-1);
//setup player
Image image = Image.createImage("/index.png");
player = new PlayerSprite (image,8,8,width); 
player.startPosition();
//setup Objects
Objects = new Vector();
ObjectsImages = Image.createImage("/objects.png");
//Setup Layer Manager
layerManager = new LayerManager();
layerManager.append(player);
layerManager.append(menuSprite);
}
// Start thread for game loop
public void start() {
gameThread = new Thread(this);
gameThread.start();
}
// Stop thread for game loop
public void stop() {
gameThread = null;
}
// Pause Game
public void pause(){
Pause = true;
stop();
}
//Resume Game
public void resume(){
Pause = false;
start();
}
// Main Game Loop
public void run() {
Graphics g = getGraphics();
Thread currentThread = Thread.currentThread();
MoreObjects(ObjectNum);
try {
    while (currentThread == gameThread) {
        long startTime = System.currentTimeMillis();
        if (isShown()) {
            if (isPlay) {
		tick();
		AnimateSelectedObject();
		if(MoveFlag == true){MoveSelectedObject();}
                RemoveObjects();
	}
        render(g);
        }
	long timeTake = System.currentTimeMillis() - startTime;
	if (timeTake < MILLIS_PER_TICK) {
		synchronized (this) {
		wait(MILLIS_PER_TICK - timeTake);
		}
	} else {currentThread.yield();}
    }
} catch (InterruptedException ex) {System.out.println("Got Busted");}
}
private boolean DifficultyFactor(int X,int Y,int Type){
	if(X+1 <= 6){
		if (TheMatrix[X+1][Y] == Type){return false;}
	}
	if(X-1 >= 0){
		if (TheMatrix[X-1][Y] == Type){return false;}
	}
	if(Y+1 <= 6){
		if (TheMatrix[X][Y+1] == Type){return false;}
	}
	if(Y-1 >= 0){
		if (TheMatrix[X][Y-1] == Type){return false;}
	}
	/*if(Y-1 >= 0 && X-1 >= 0){
		if (TheMatrix[X-1][Y-1] == Type){return false;}
	}
	if(Y-1 >= 0 && X+1 <= 6){
		if (TheMatrix[X+1][Y-1] == Type){return false;}
	}
	if(Y+1 <= 6 && X-1 >= 0){
		if (TheMatrix[X-1][Y+1] == Type){return false;}
	}
	if(Y+1 <= 6 && X+1 <= 6){
		if (TheMatrix[X+1][Y+1] == Type){return false;}
	}*/
	return true;
}
private void MoreObjects(int HowMany){
if (ResumingGame == true){
	ResumingGame = false;
	return;
}
for (int index = 1; index <= HowMany; index++){
    do {
        RandomX = produceRandom(7);
	RandomY = produceRandom(7);
    } while (TheMatrix[RandomX][RandomY] != 0);
    do{
	    ObjectType = produceRandom(5);
    } while (DifficultyFactor(RandomX,RandomY,ObjectType+1) == false);
    //Create the new object
    object = new ObjectSprite(ObjectsImages,20,20,RandomX,RandomY,ObjectType);
    //Next Follow the list of things that need to be done for the new Object
    if (object != null) {
    	TheMatrix[RandomX][RandomY] = object.ObjectType;
        object.setPosition(RandomX*STEP+(STEP-20)/2,RandomY*STEP+(STEP-20)/2);
        Objects.addElement(object);
        layerManager.insert(object,1);
    }
}
}

public void ResumeGame(){
TheMatrix = midlet.SavedMatrix;
//System.out.println("::Inside ResumeGame TheMatrix::");
//printM();
for (int y = 0; y < 7; ++y){
	for (int x = 0; x < 7; ++x){
		if(TheMatrix[x][y] != 0){
			object = new ObjectSprite(ObjectsImages,20,20,x,y,TheMatrix[x][y]-1);
			if (object != null) {	
				object.setPosition(x*STEP+(STEP-20)/2,y*STEP+(STEP-20)/2);
				Objects.addElement(object);
				layerManager.insert(object,1);
			}
		}
	}
}
ResumingGame = true;
}

private boolean IsGameOver(){
	if (Objects.size() == 46){
		ObjectNum = 3;
	}
	if (Objects.size() == 47){
		ObjectNum = 2;
	}
	if (Objects.size() == 48){
		ObjectNum = 1;
	}
	if (Objects.size() >= 49){
		// Remove all Objects
		for (int j = 0; j < Objects.size(); ++j) {
			ObjectSprite object = (ObjectSprite)(Objects.elementAt(j));
			layerManager.remove(object);
		}
		isPlay = false;
		// G A M E  O V E R
		// LOAD GAME OVER IMAGE
		try{
			Image GameOverImage = Image.createImage("/gameOver.png");
			GameOverSprite = new Sprite (GameOverImage);
			GameOverSprite.setPosition(getWidth()/2-GameOverImage.getWidth()/2,getHeight()/2-GameOverImage.getHeight()/2);
			layerManager.append(GameOverSprite);
		}catch (Exception e){System.out.println(":: IsHighScoreImage ::"+e);}
		// STOP THE THREAD
		stop();
		// SET THE FLAG TO NOT SAVE THE GAME
		//midlet.GameSaved = 1;
		// SAVE TO RMS
		midlet.setSavedGame(1,TheMatrix);
		gameInterupted = false;
		// IS THIS A HIGH SCORE?
		try{
			if (midlet.score.isHighScore(TotalScore) != -1){
				midlet.keyboardScreen.TotalScore = this.TotalScore;
				midlet.KeyBoardScreenShow();
			}
        	}catch (Exception e){System.out.println(":: IsHighScore :: "+e);}
	}
	return true;
}

private int produceRandom(int n){	
int bits, val;
do {
	bits = random.nextInt();
	val = bits % n;
} while(bits - val + (n-1) < 0);
return val;
}

public void tick() {	
int keyStates = getKeyStates();
// Player Moves
if ( (keyStates & LEFT_PRESSED) != 0) {
    player.moveLeft();
} else if ((keyStates & RIGHT_PRESSED) !=0 ) {
    player.moveRight();
} else if ((keyStates & UP_PRESSED) != 0) {
    player.moveUp();
} else if ((keyStates & DOWN_PRESSED) != 0) {
    player.moveDown();
}
// Deal With Objects
if ((keyStates & FIRE_PRESSED) != 0) {
    if (IsPositionOccupied(player.player_positionX,player.player_positionY) == false && ObjectSelected == true){
	    if (routeObject(player.player_positionX,player.player_positionY,SelectedObjectX,SelectedObjectY) == true){MoveFlag = true;}
    return;
    }
    if (IsPositionOccupied(player.player_positionX,player.player_positionY) == true && ObjectSelected == true){
	SelectObject(player.player_positionX,player.player_positionY);
    return;
    }
    if (IsPositionOccupied(player.player_positionX,player.player_positionY) == false && ObjectSelected == false){
    return;
    }
    if (IsPositionOccupied(player.player_positionX,player.player_positionY) == true && ObjectSelected == false){	 
        SelectObject(player.player_positionX,player.player_positionY);
    return;
    }
}
}

private void render(Graphics g) {
// Background
g.setColor(0x808080);
g.fillRect(0,0,width,getHeight());
// Lines
g.setColor(0x000000);
for (int i=0; i<8; i++){g.drawLine(0, i*STEP, width, i*STEP);}
for (int i=0; i<8; i++){g.drawLine(i*STEP, 0, i*STEP, width);}
// Layer Manager
layerManager.paint(g,0,0);
// Score
g.drawString(""+TotalScore, 105, getHeight()-5, 16|4); //TEMP
flushGraphics(); //REMINDER....
}

private boolean IsPositionOccupied(int X, int Y){
if (TheMatrix[X][Y] != 0){return true;}
return false;
}

private void SelectObject(int X, int Y){
    // User cannot select an object that is marked for deletion
    if (TheMatrix[X][Y] == 9) {return;}
    // Stop the Animation of previously selected Object if there was one
    for (int i = 0; i < Objects.size(); ++i) {
    ObjectSprite object = (ObjectSprite)(Objects.elementAt(i));
    if (object.currentlySelected == true) {
        object.currentlySelected = false;
        object.setPosition(SelectedObjectX*STEP+(STEP-20)/2, SelectedObjectY*STEP+(STEP-20)/2);
        break;
    }
    }
    //Select New Object
    for (int i = 0; i < Objects.size(); ++i) {
    ObjectSprite object = (ObjectSprite)(Objects.elementAt(i));
    if ((object.objectX == X) && (object.objectY == Y)) {
        SelectedObjectX = X;
	SelectedObjectY = Y;
        ObjectSelected = true;
        object.currentlySelected = true;
        break;
    }
    }
}

private void AnimateSelectedObject(){
	// A selected Object wiggles up n down
	if (ObjectSelected == true){
		for (int i = 0; i < Objects.size(); ++i) {
			ObjectSprite object = (ObjectSprite)(Objects.elementAt(i));
			if (object.currentlySelected == true) {
				object.setPosition(object.getX(),object.getY()+jump);
				jump++;
				if (jump >= 3){jump = -2;}
				break;
			}
		}
	}
}

private void MoveSelectedObject(){
	if (ObjectSelected == true){
		for (int i = 0; i < Objects.size(); ++i) {
			ObjectSprite object = (ObjectSprite)(Objects.elementAt(i));
			if (object.currentlySelected == true) {
				object.setPosition(Route[routeIndex][0]*STEP+(STEP-20)/2,Route[routeIndex][1]*STEP+(STEP-20)/2);
				routeIndex++;
				if (Route[routeIndex][0] == 8){
					object.objectX = Route[routeIndex-1][0];
					object.objectY = Route[routeIndex-1][1];
					TheMatrix[Route[routeIndex-1][0]][Route[routeIndex-1][1]] = object.ObjectType;
					TheMatrix[Route[0][0]][Route[0][1]] = 0;
					MoveFlag = false;
					object.currentlySelected = false;
					ObjectSelected = false;
					if (Check4ObjectLines() == false){
						MoreObjects(ObjectNum);
						Check4ObjectLines();
					}
					// Initialize Route again
					for(int z = 0; z < 49; z++){
						Route[z][0] = 8;
						Route[z][1] = 8;
					}
					routeIndex = 0;
				}
				break;
			}
		}
	}
}


private boolean routeObject(int ax,int ay,int bx,int by){
	int dx[] = {1,-1, 0, 0};
	int dy[] = {0, 0, 1,-1};
	for(int y=0; y<7; y++){
		for(int x=0; x<7; x++){
			if (TheMatrix[x][y] == 0){
				pf[x][y] = GO_EMPTY;
			}else{pf[x][y] = GO_BALL;}
		}
	}
	int lastchanged[][][] = new int [4][98][4];
	int lastChangedCount[] = new int [4];
	int currentchanged = 0;
	int nextchanged = 1;
	lastchanged[currentchanged][0][0] = ax;
	lastchanged[currentchanged][0][1] = ay;
	lastChangedCount[currentchanged] = 1;
	pf[ax][ay]=GO_A;
	pf[bx][by]=GO_B;
	boolean B_reached = false;
	do{
		lastChangedCount[nextchanged] = 0;
		for(int dir=0; dir<4; dir++){
			for ( int i = 0 ; i < lastChangedCount[currentchanged]; i++ ){
				int nx = lastchanged[currentchanged][i][0] + dx[dir];
				int ny = lastchanged[currentchanged][i][1] + dy[dir];
				if( (nx>=0) && (nx<7) && (ny>=0) && (ny<7) ){
					if( pf[nx][ny]==GO_EMPTY ||( nx==bx && ny==by )){
						pf[nx][ny] = dir;
						lastchanged[nextchanged][lastChangedCount[nextchanged]][0] = nx;
						lastchanged[nextchanged][lastChangedCount[nextchanged]][1] = ny;
						lastChangedCount[nextchanged]++;
					}
					if( (nx==bx) && (ny==by) ){
						B_reached = true;
						break;
					}
				}
			}
		}
		nextchanged = nextchanged ^ 1;
		currentchanged = nextchanged ^ 1;
		if (lastChangedCount[currentchanged] == 0){
			// No Route Found
			return false;
		}
	} while(!B_reached && lastChangedCount[currentchanged] != 0);
	if (B_reached) {
		int animmax = 0;
		int x, y,dir;
		for( x = bx, y = by; (dir = pf[x][y]) != GO_A;x -=dx[dir],y -= dy[dir]) {
			Route[animmax][0] = x;
			Route[animmax][1] = y;
			animmax++;
		}
		Route[animmax][0] = x;
		Route[animmax][1] = y;
		return true;
	}
	return false;
}

public void RemoveObjects(){
	for (int y = 0; y < 7; y++){
		for (int x = 0; x < 7; x++){
			if (TheMatrix[x][y] == 9){
				for (int j = 0; j < Objects.size(); ++j) {
					ObjectSprite object = (ObjectSprite)(Objects.elementAt(j));
					if ((object.objectX == x) && (object.objectY == y)){
						object.nextFrame();
						if (object.getFrame() == 4 || object.getFrame() == 9 || object.getFrame() == 14 || object.getFrame() == 19 || object.getFrame() == 24){
							Objects.removeElementAt(j);
							layerManager.remove(object);
							TheMatrix[x][y] = 0;
						}
					}
				}
			}
		}
	}
	IsGameOver();
}

private boolean Check4ObjectLines(){
	// Horizontal
	FoundLine = false;
	for (int y = 0; y < 7; y++){
		for (int x = 0; x < 7; x++){
			if ( x == 6){
				if (InRow >=4){
					FoundLine = true;
					for (int j = 0; j < InRow; j++){
						//Let's mark this one for deletion
						TheMatrix[x-j][y] = 9;
						TotalScore++;
					}
					InRow = 1;
				} else {InRow = 1;}
				break;
			}
			if (TheMatrix[x][y] != 0){
				if (TheMatrix[x][y] == TheMatrix[x+1][y]){InRow++;}
				if (TheMatrix[x][y] != TheMatrix[x+1][y]){
					if (InRow >= 4) {
						FoundLine = true;
						for (int j = 0; j < InRow; j++){
							//Let's mark this one for deletion
							TheMatrix[x-j][y] = 9;
							TotalScore++;
						}
						InRow = 1;
					} else {InRow = 1;}
				}
			}
		}
	}
	// Vertical
	for (int x = 0; x < 7; x++){
		for (int y = 0; y < 7; y++){
			if ( y == 6){
				if (InRow >=4){
					FoundLine = true;
					for (int j = 0; j < InRow; j++){
						//Let's mark this one for deletion
						TheMatrix[x][y-j] = 9;
						TotalScore++;
					}
					InRow = 1;
				} else {InRow = 1;}
				break;
			}
			if (TheMatrix[x][y] != 0){
				if (TheMatrix[x][y] == TheMatrix[x][y+1]){InRow++;}
				if (TheMatrix[x][y] != TheMatrix[x][y+1]){
					if (InRow >= 4) {
						FoundLine = true;
						for (int j = 0; j < InRow; j++){
							//Let's mark this one for deletion
							TheMatrix[x][y-j] = 9;
							TotalScore++;
						}
						InRow = 1;
					} else {InRow = 1;}
				}
			}
		}
	}
	// Diagonal Right
	for ( int xs = 6/*7-1*/,ys = 3;/*7-4*/ xs >= 3;/*4-1*/ ) {
		for ( int x = xs, y = ys; x >= 0 && y < 7; x--, y++ ) {
			if ((y == 6) || (x == 0)){
				if (InRow >=4){
					FoundLine = true;
					for (int j = 0; j < InRow; j++){
						//Let's mark this one for deletion
						TheMatrix[x+j][y-j] = 9;
						TotalScore++;
					}
					InRow = 1;
				} else {InRow = 1;}
				break;
			}
			if (TheMatrix[x][y] != 0){
				if (TheMatrix[x][y] == TheMatrix[x-1][y+1]){InRow++;}
				if (TheMatrix[x][y] != TheMatrix[x-1][y+1]){
					if (InRow >= 4) {
						FoundLine = true;
						for (int j = 0; j < InRow; j++){
							//Let's mark this one for deletion
							TheMatrix[x+j][y-j] = 9;
							TotalScore++;
						}
						InRow = 1;
					} else {InRow = 1;}
				}
			}
		}
		if ( ys > 0 ) ys--; else xs--;
	}
	// Diagonal Left
	for ( int xs = 0,ys = 3;/*7-4*/ xs <= 3;/*7-4*/ ){
		for ( int x = xs, y = ys; x < 7 && y < 7; x++, y++ ){
			if ((y == 6) || (x == 6)){
				if (InRow >=4){
					FoundLine = true;
					for (int j = 0; j < InRow; j++){
						//Let's mark this one for deletion
						TheMatrix[x-j][y-j] = 9;
						TotalScore++;
					}
					InRow = 1;
				} else {InRow = 1;}
				break;
			}
			if (TheMatrix[x][y] != 0){
				if (TheMatrix[x][y] == TheMatrix[x+1][y+1]){InRow++;}
				if (TheMatrix[x][y] != TheMatrix[x+1][y+1]){
					if (InRow >= 4) {
						FoundLine = true;
						for (int j = 0; j < InRow; j++){
							//Let's mark this one for deletion
							TheMatrix[x-j][y-j] = 9;
							TotalScore++;
						}
						InRow = 1;
					} else {InRow = 1;}
				}
			}
		}
		if ( ys > 0 ) ys--; else xs++;
	}
return FoundLine;
}

protected void keyPressed (int code) {
	if ((code == -7) ||(code == -6)) {
		if (gameInterupted == true){
			midlet.setSavedGame(0,TheMatrix);
			//System.out.println("::SetSavedGame Matrix::");
			//printM();
			midlet.mainMenuScreenShow();
		}else{midlet.mainMenuScreenShow();}
	}
}
	private void printM(){//DEBUG
	for (int y = 0; y < 7; ++y){
		for (int x = 0; x < 7; ++x){System.out.print(TheMatrix[x][y]);}System.out.println();}//END DEBUG
	}
}
