import javax.microedition.rms.*;
import java.util.*;
import java.io.*;

public class SavedGame extends BaseRMS {
private int GameSaved;
private int SavedMatrix[][] = new int [7][7];
public SavedGame(String rmsName) {
super(rmsName);
initValues();
}
public void initValues() {
GameSaved = 1;   // NO GAME IS SAVED
for (int y = 0; y < 7; ++y){for (int x = 0; x < 7; ++x){SavedMatrix[x][y] = 0;}}
}
public int getGameSaved(){return this.GameSaved;}
public int[][] getSavedMatrix(){return this.SavedMatrix;}
public void loadSavedGame() throws Exception {
    try {
        this.open();
        if (this.getRecordStore() != null)
        this.close();
    } catch (Exception e) {throw new Exception("Error loading SavedGame" + e);}
}
public void reset() throws Exception {
this.open();
initValues();
updateData();
if (this.getRecordStore() != null){this.close();}
}

public void updateSavedGame(int GameSaved,int SavedMatrix[][]) throws Exception {
try {
	this.open();
	this.GameSaved = GameSaved;
	this.SavedMatrix = SavedMatrix;
	updateData();
	// close
	if (this.getRecordStore() != null){this.close();}
} catch (Exception e) {throw new Exception(this.getRMSName() + "::updateScores::" + e);}
}
protected void loadData() throws Exception {
	try {
		byte[] record = this.getRecordStore().getRecord(1);
		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(record,0,record.length));
		GameSaved = istream.readInt();
		for (int y = 0; y < 7; ++y){for (int x = 0; x < 7; ++x){SavedMatrix[x][y] = istream.readInt();}}
	} catch (Exception e) {throw new Exception (this.getRMSName() + "::loadData::" + e);}
}
protected void createDefaultData() throws Exception {
	try {
		ByteArrayOutputStream bstream = new ByteArrayOutputStream(220);
		DataOutputStream ostream = new DataOutputStream(bstream);
		ostream.writeInt(GameSaved);
		for (int y = 0; y < 7; ++y){for (int x = 0; x < 7; ++x){ostream.writeInt(SavedMatrix[x][y]);}}
		ostream.flush();
		ostream.close();
		byte[] record = bstream.toByteArray();
		this.getRecordStore().addRecord(record, 0, record.length);
	} catch (Exception e) {throw new Exception(this.getRMSName() + "::createDefaultData::" + e);}
}
protected void updateData() throws Exception {
	try {
		ByteArrayOutputStream bstream = new ByteArrayOutputStream(220);
		DataOutputStream ostream = new DataOutputStream(bstream);
		ostream.writeInt(GameSaved);
		for (int y = 0; y < 7; ++y){for (int x = 0; x < 7; ++x){ostream.writeInt(SavedMatrix[x][y]);}}
		ostream.flush();
		ostream.close();
		byte[] record = bstream.toByteArray();
		this.getRecordStore().setRecord(1, record, 0, record.length);
	} catch(Exception e) {throw new Exception(this.getRMSName() + "::updateData::" + e);}
}
}
