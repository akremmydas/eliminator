import javax.microedition.rms.*;
import java.util.*;
import java.io.*;

public class Score extends BaseRMS {
private String[] names = new String[5];
private int[] values = new int[5];

public Score(String rmsName) {
super(rmsName);
initValues();
}

public void initValues() {
names[0] = "EMPTY..";
names[1] = "EMPTY..";
names[2] = "EMPTY..";
names[3] = "EMPTY..";
names[4] = "EMPTY..";

values[0] = 0;
values[1] = 0;
values[2] = 0;
values[3] = 0;
values[4] = 0;

}

public void loadScores() throws Exception {
    try {
        // Will call either loadData() or createDefaultData()
        this.open();
        // Add any other neccessary processing, in this case there is none
        // Close
        if (this.getRecordStore() != null)
        this.close();
    } catch (Exception e) {throw new Exception("Error loading Scores" + e);}
}

public int[] getValues() {
return this.values;
}

public String[] getNames() {
return this.names;
}

public void reset() throws Exception {
    this.open();
    initValues();
    updateData();
    if (this.getRecordStore() != null)
        this.close();
}

public void updateScores(int score, String name) throws Exception {
try {
	for (int i = 0; i < names.length; i++) {
		if (score > values[i]) {
			// load current scores
			this.open();
			// Shift the score table.
			for (int j = names.length - 1; j > i; j--) {
				values[j] = values[j - 1];
				names[j] = names[j - 1];
			}
			// Insert the new score.
			this.values[i] = score;
			this.names[i] = name;
			// Update High Scores
			updateData();
			// close
			if (this.getRecordStore() != null)
				this.close();
			break;
		}
	}
	} catch (Exception e) {throw new Exception(this.getRMSName() + "::updateScores::" + e);}
}

public int isHighScore(int score) throws Exception {
	try {
		for (int i = 0; i < names.length; i++) {
			if (score > values[i])
                                return i;
		}
	} catch (Exception e) {throw new Exception(this.getRMSName() + "::isHighScore::" + e);}
        return -1;
}

protected void loadData() throws Exception {
	try {
		for (int i = 0; i < names.length; i++) {
			byte[] record = this.getRecordStore().getRecord(i + 1);
			DataInputStream istream = new DataInputStream(new ByteArrayInputStream(record,0,record.length));
			values[i] = istream.readInt();
			names[i] = istream.readUTF();
		}
	} catch (Exception e) {throw new Exception (this.getRMSName() + "::loadData::" + e);}
}

protected void createDefaultData() throws Exception {
	try {
		for (int i = 0; i < names.length; i++) {
			ByteArrayOutputStream bstream = new ByteArrayOutputStream(80);
			DataOutputStream ostream = new DataOutputStream(bstream);
			ostream.writeInt(values[i]);
			ostream.writeUTF(names[i]);
			ostream.flush();
			ostream.close();
			byte[] record = bstream.toByteArray();
			this.getRecordStore().addRecord(record, 0, record.length);
		}
	} catch (Exception e) {throw new Exception(this.getRMSName() + "::createDefaultData::" + e);}
}

protected void updateData() throws Exception {
	try {
		for (int i = 0; i < names.length; i++) {
			ByteArrayOutputStream bstream = new ByteArrayOutputStream(80);
			DataOutputStream ostream = new DataOutputStream(bstream);
			ostream.writeInt(values[i]);
			ostream.writeUTF(names[i]);
			ostream.flush();
			ostream.close();
			byte[] record = bstream.toByteArray();
			this.getRecordStore().setRecord(i + 1, record, 0, record.length);
		}
	} catch(Exception e) {throw new Exception(this.getRMSName() + "::updateData::" + e);}
}

}
