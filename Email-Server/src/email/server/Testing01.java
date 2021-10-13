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
public class Testing01 {
    
/* First test    
    public static void main(String[] args) {
        int code = FileIO.openMessagesFile("_messages.txt");
        
        if (code == Globals.PROCESS_OK) {
            String message = "Hello how are you?";
            Record rec = new Record(message, GLOBALS.END_OF_MESSAGE);
            
            rec.dumpData();
                      
            code = FileIO.closeMessagesFile();            
            if (code == Globals.PROCESS_OK) {
                System.out.println("File closed ok");
            }
        }
    }
}
*/
    
/* Second test  
    public static void main(String[] args) {
        int code = FileIO.openMessagesFile("_messages.txt");
        
        if (code == Globals.PROCESS_OK) {
            String message = "Hello how are you?";
            Record rec = new Record(message, Globals.END_OF_MESSAGE);
            
            code = rec.writeToMessagesFile(0, Globals.APPEND);
            code = FileIO.closeMessagesFile();        
            if (code == Globals.PROCESS_OK) {
                System.out.println("File closed ok");
            }
        }
    }
*/
    
/* Third test

    public static void main(String[] args) {
        int code = FileIO.openMessagesFile("_messages.txt");
        
        if (code == Globals.PROCESS_OK) {
            Record rec = new Record();
            
            code = rec.readFromMessagesFile(0);
            System.out.println(rec);
            code = FileIO.closeMessagesFile();        
            if (code == Globals.PROCESS_OK) {
                System.out.println("File closed ok");
            }
        }
    }
*/
    
/* Quiz 01
    public static void main(String[] args) {
        int code = FileIO.openMessagesFile("_messages.txt");
        if (code == Globals.PROCESS_OK) {
            Record rec = null;

            for (int id = 0; id < 1000; id++) {
                String text = Utils.leftPad("" + id, Globals.RECORD_DATA_LEN, '0');
		rec = new Record(text, Globals.END_OF_MESSAGE);
		rec.writeToMessagesFile(id, Globals.APPEND);
            }
            long fileSize = -1;
            try {
                fileSize = Globals.msg.length();
            }
            catch(IOException e) {
                System.out.println("Error obtaining file size");
            }
            System.out.println("File is " + fileSize + " bytes");
            System.out.println("Size of file divisible by " + Globals.RECORD_LEN + ": " + (fileSize % Globals.RECORD_LEN == 0));
            FileIO.closeMessagesFile();
        }
        else {
            System.out.println("File open eror");
        }
    }
    */
    
    public static void main(String[] args) {
        int code = FileIO.openMessagesFile("messages.txt");
        if (code == Globals.PROCESS_OK){
            Record rec = new Record();
            rec.readFromMessagesFile (65);
            System.out.println(rec);
            FileIO.closeMessagesFile();
        }
        else {
            System.out.println("Error opening file");
        }
    }
    
}

