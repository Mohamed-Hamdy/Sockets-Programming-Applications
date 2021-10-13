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
import java.io.*;
public class FileIO {
    public static int openMessagesFile(String fileName) {
        try {
            Globals.msg = new RandomAccessFile(fileName, "rw");
            Globals.totalRecordsInMessageFile = (int) (Globals.msg.length() / Globals.RECORD_LEN);
            return Globals.PROCESS_OK;
        }
        catch(IOException err) {
            return Globals.PROCESS_ERROR;
        }
    }
    
    public static int closeMessagesFile() {
        try {
            Globals.msg.close();
            return Globals.PROCESS_OK;
        }
        catch(IOException err) {
            return Globals.PROCESS_ERROR;
        }
    }
    
    public static int retrieveAvailableList(String fileName) {
        RandomAccessFile f;
        try {
            f = new RandomAccessFile(fileName, "rw");
            
            long totalDeletedRecords = f.length() / Globals.AVAILABLE_NODE_RECORD_NUMBER_LEN;
            
            for (int node = 0; node < totalDeletedRecords; node++) {
                Globals.availableList.addRecord(f.readInt());
            }
            f.close();
            
            return Globals.PROCESS_OK;
        }
        catch(IOException e) {
            return Globals.PROCESS_ERROR;
        }
    }
    
    public static int saveAvailableList(String fileName) {
        RandomAccessFile f;
        try {
            f = new RandomAccessFile(fileName, "rw");
            
            Available p = Globals.availableList.getHead();
            while (p != null) {
                f.writeInt(p.getRecordNumber());
                p = p.getNext();
            }
            f.close();
            
            return Globals.PROCESS_OK;
        }
        catch(IOException e) {
            return Globals.PROCESS_ERROR;
        }
    }
    
    // returns an array filled with the accounts; the array is the exact size
    public static String[] retrieveAccounts(String fileName) {
        String[] list = null;
        try {
            RandomAccessFile f = new RandomAccessFile(fileName, "r");
            int totalAccounts = 0;
            String line = f.readLine();
            while (line != null) {
                totalAccounts++;
                line = f.readLine();
            }
            if (totalAccounts > 0) {
                list = new String[totalAccounts];
                f.seek(0);
                for (int i = 0; i < totalAccounts; i++) {
                    list[i] = f.readLine();
                }
            }
        }
        catch(IOException err) {
            return null;
        }
        
        return list;
    }
}
