import javax.microedition.rms.*;
import java.util.*;
import java.io.*;

public class Settings extends BaseRMS {
private int Language;
private int Difficulty;
private int Sound;
public Settings(String rmsName) {
super(rmsName);
initValues();
}
public void initValues() {
Language = 1;    // ENGLISH
Difficulty = 8;  // ROOKY
Sound = 12;	 // ON
}
public void loadSettings() throws Exception {
    try {
        // Will call either loadData() or createDefaultData()
        this.open();
        // Add any other neccessary processing, in this case there is none
        // Close
        if (this.getRecordStore() != null)
        this.close();
    } catch (Exception e) {throw new Exception("Error loading Scores" + e);}
}
public int getLanguage() {return this.Language;}
public int getDifficulty() {return this.Difficulty;}
public int getSound() {return this.Sound;}
public void reset() throws Exception {
this.open();
initValues();
updateData();
if (this.getRecordStore() != null){this.close();}
}

public void updateSettings(int Language, int Difficulty, int Sound) throws Exception {
try {
	this.open();
	// Insert new settings
	this.Language = Language;
	this.Difficulty = Difficulty;
	this.Sound = Sound;
	// Update Settings
	updateData();
	// close
	if (this.getRecordStore() != null){this.close();}
} catch (Exception e) {throw new Exception(this.getRMSName() + "::updateScores::" + e);}
}
protected void loadData() throws Exception {
	try {
		byte[] record = this.getRecordStore().getRecord(1);
		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(record,0,record.length));
		Language = istream.readInt();
		Difficulty = istream.readInt();
		Sound = istream.readInt();
	} catch (Exception e) {throw new Exception (this.getRMSName() + "::loadData::" + e);}
}
protected void createDefaultData() throws Exception {
	try {
		ByteArrayOutputStream bstream = new ByteArrayOutputStream(220);
		DataOutputStream ostream = new DataOutputStream(bstream);
		ostream.writeInt(Language);
		ostream.writeInt(Difficulty);
		ostream.writeInt(Sound);
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
		ostream.writeInt(Language);
		ostream.writeInt(Difficulty);
		ostream.writeInt(Sound);
		ostream.flush();
		ostream.close();
		byte[] record = bstream.toByteArray();
		this.getRecordStore().setRecord(1, record, 0, record.length);
	} catch(Exception e) {throw new Exception(this.getRMSName() + "::updateData::" + e);}
}
}
