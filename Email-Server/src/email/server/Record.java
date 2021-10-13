/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package email.server;

/**
 *
 * @author Mohamed_El_Laithy
 */
import java.io.RandomAccessFile;
import java.io.IOException;

/********************************************************************/
/*
/* Record structure for messages file
/*
/* textPortion: MESSAGE_TEXT_PORTION_LEN allocated for text
/* linkToNextPortion: 4 byte integer determines where the message continues in the file
/*                    initialized to 0
/*
/********************************************************************/

public class Record  {
    private byte[] data = new byte[Globals.RECORD_DATA_LEN];
    private int next = Globals.END_OF_MESSAGE;
    
    public Record() {
	for (int i = 0; i < Globals.RECORD_DATA_LEN; i++) {
	    data[i] = (byte) Globals.BLANK;
	}
	next = Globals.END_OF_MESSAGE;
    }
    
    public Record(String s, int nextRecord) {
	setData(s, nextRecord);
    }
    
    public void setData(String txt, int nextRecord) {
	int i = 0;
	for (i = 0; i < txt.length(); i++) {
	    data[i] = (byte) txt.charAt(i);
	}
	for (; i < Globals.RECORD_DATA_LEN; i++) {
	    data[i] = (byte) Globals.BLANK;
	}
	next = nextRecord;
    }
    
    public String getData() {
	String s = "";
	for (int i = 0; i < Globals.RECORD_DATA_LEN; i++) {
	    s = s + (char) ((data[i] + 256) % 256);     // byte is signed in Java [-128, 127]; we need to shift to [0, 255]; it affects the dateTime
	}
	return s;
    }
    
    public int getNext() {
	return next;
    }
    
    public int readFromMessagesFile(int recordNumber) {
	try {
	    Globals.msg.seek(recordNumber * Globals.RECORD_LEN);
	    int bytes = Globals.msg.read(data);
	    next = Globals.msg.readInt();        
	    return Globals.PROCESS_OK;
	}
	catch(IOException err) {
	    return Globals.PROCESS_ERROR;
	}
    }
    
    public int writeToMessagesFile(int recordNumber, int mode) {
	try {
	    Globals.msg.seek(recordNumber * Globals.RECORD_LEN);
	    Globals.msg.write(data);
	    Globals.msg.writeInt(next);
	    if (mode == Globals.APPEND) {
		Globals.totalRecordsInMessageFile++;
	    }
	    return Globals.PROCESS_OK;
	}
	catch(IOException err) {
	    return Globals.PROCESS_ERROR;
	}
    }
    
    // Delete one record by appending the record to available list
    // A DELETED marker is written at the first byte of the record
    // in case we need to rebuild the available list from
    // the messages file
    
    public void deleteFromMessagesFile(int recordNumber) {
	int error = readFromMessagesFile(recordNumber);
        if (error == Globals.PROCESS_OK) {
            data[0] = (byte) Globals.DELETED;
            error = writeToMessagesFile(recordNumber, Globals.MODIFY);
            if (error == Globals.PROCESS_OK) {
                Globals.availableList.addRecord(recordNumber);
            }
            else {
                System.out.println("Error writing to messages file. In method deleteFromMessagesFile");
            }
        }
        else {
            System.out.println("Error reading from messages file. In method deleteFromMessageFile");
        }
    }
    
    public String toString() {
	return getData() + next;
    }
    
    
public void dumpData() {
    for (int i = 0; i < data.length; i++) {
        System.out.println("byte " + i + " $" + (char) data[i] + "$");
    }
}
    
    
}
