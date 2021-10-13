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

//import java.util.Scanner;
import java.io.*;
import java.util.Date;
public class EmailServer {
    public static void main(String[] args) {
        //Scanner in = new Scanner(System.in);

        int error = -1;
        int command = -1;
/**/      
        Globals.availableList = new AvailableList();
        error = FileIO.openMessagesFile(Globals.MESSAGES_FILE);
        if (error == Globals.PROCESS_OK) {
            Tree senderIndex   = new Tree();
            Tree receiverIndex = new Tree();
            
            // this code is here temporarily until we can retrieve these trees
            senderIndex.buildFromMessagesFile(Globals.SENDER_ID);
            receiverIndex.buildFromMessagesFile(Globals.RECEIVER_ID);

            // retrieve index with key sender
            // retrieve index with key receiver
            
            Globals.accounts = FileIO.retrieveAccounts(Globals.ACCOUNTS_FILE);
            
            // retrieve the availabe list
            error = FileIO.retrieveAvailableList(Globals.AVAILABLE_LIST_FILE);
            if (error == Globals.PROCESS_OK) {
                Message message  = new Message();
                int recordNumber = 0;
                String identification = Globals.STR_NULL;

                TreeNode p = null;
                TreeNode q = null;

                char serverCommand = 0;
                do {
                    System.out.println("Waiting...");
                    String request = NetIO.receiveRequest();
                    
                    serverCommand = request.charAt(0);
                    switch (serverCommand) {
                        case Globals.SEND_MESSAGE :         // entire email expected
                            if (correctSyntax(request)) {
                                request = Utils.setReceivingTime(request);
                                message.setMessage(request);
                                recordNumber = message.writeToMessagesFile();
                                System.out.println("==============================");
                                System.out.println("Message from: " + Globals.clientIPAddress);
                                message.printFromMessagesFile(recordNumber);
                                System.out.println("==============================");

                                identification = request.substring(Globals.SENDER_POS, Globals.MARKER_POS);
                                p = new TreeNode(identification, recordNumber, null, null, null);
                                senderIndex.insertNode(p);

                                identification = request.substring(Globals.RECEIVER_POS, Globals.DATE_TIME_POS) +
                                                 request.substring(Globals.SENDER_POS, Globals.RECEIVER_POS) +
                                                 request.substring(Globals.DATE_TIME_POS, Globals.MARKER_POS);
                                p = new TreeNode(identification, recordNumber, null, null, null);
                                receiverIndex.insertNode(p);
                            }
                            else {
                                System.out.println("***** Incorrect Syntax from sender");
                            }
                            
                            break;
                            
                        case Globals.IN_BOX :            // command + 9 digit identification expected
                            identification = request.substring(Globals.CLIENT_POS);
                            
                            p = receiverIndex.findNode(identification, 0);
                            if (p != null) {
                               System.out.print("First in tree with this partialKey: ");
                               recordNumber = p.getRecordNumber();
                               System.out.println("Record number: " + recordNumber);
                               message.printFromMessagesFile(recordNumber);
                            }
                            else
                               System.out.println("***identification not found");

                            q = receiverIndex.findNode(identification,  1);
                            if (q != null) {
                               System.out.print("Last in tree with this partialKey: ");
                               recordNumber = q.getRecordNumber();
                               System.out.println("Record number: " + recordNumber);
                               message.printFromMessagesFile(recordNumber);
                            }
                            else
                               System.out.println("***identification not found");

                            System.out.println("============Only printing locally");
                            receiverIndex.printTree(p, q);
                            System.out.println("============Sending over net");
                            receiverIndex.prepareTransmissionString(p, q);
                            Globals.transmissionString = Globals.transmissionString + Utils.intToBytesStr(Globals.END_OF_MESSAGES_TRANSMISSION);
                            NetIO.sendRequest(Globals.transmissionString, Globals.clientIPAddress);
                            Globals.transmissionString = Globals.STR_NULL;
                            break;
                            
                        case Globals.OUT_BOX :            // command + 9 digit identification expected
                            identification = request.substring(Globals.CLIENT_POS);
                            
                            p = senderIndex.findNode(identification, 0);
                            if (p != null) {
                               System.out.print("First in tree with this partialKey: ");
                               recordNumber = p.getRecordNumber();
                               System.out.println("Record number: " + recordNumber);
                               message.printFromMessagesFile(recordNumber);
                            }
                            else
                               System.out.println("***identification not found");

                            q = senderIndex.findNode(identification,  1);
                            if (q != null) {
                               System.out.print("Last in tree with this partialKey: ");
                               recordNumber = q.getRecordNumber();
                               System.out.println("Record number: " + recordNumber);
                               message.printFromMessagesFile(recordNumber);
                            }
                            else
                               System.out.println("***identification not found");

                            System.out.println("============Only printing locally");
                            senderIndex.printTree(p, q);
                            System.out.println("============Sending over net");
                            senderIndex.prepareTransmissionString(p, q);
                            Globals.transmissionString = Globals.transmissionString + Utils.intToBytesStr(Globals.END_OF_MESSAGES_TRANSMISSION);
                            NetIO.sendRequest(Globals.transmissionString, Globals.clientIPAddress);
                            Globals.transmissionString = Globals.STR_NULL;
                            break;
                            
                        case Globals.SERVER_SHUTDOWN :
                            System.out.println("Closing files...server shutdown");
                            break;
                            
                        default :
                            System.out.println("Unknown request: " + request);
                            break;
                    }                    
                } while (serverCommand != Globals.SERVER_SHUTDOWN);	
                
                error = FileIO.saveAvailableList(Globals.AVAILABLE_LIST_FILE);
                
            }
            else {
                System.out.println("Error opening available list file");
            }

            error = FileIO.closeMessagesFile();
        }
        else {
            System.out.println("Error opening messages file " + Globals.MESSAGES_FILE);
        }
        System.out.println("End of program.");
/**/
/*      
        error = FileIO.openMessagesFile(Globals.MESSAGES_FILE);
        if (error == Globals.PROCESS_OK) {
            Message message  = new Message();
            String sender    = "";
            String receiver  = "";
            String timeStamp = "";
            String identification = "";
            String subject   = "";
            String text      = "";
            String entireMessage = "";
            int recordNumber = 0;
        
            Tree senderIndex   = new Tree();
            Tree receiverIndex = new Tree();
            TreeNode p = null;
        
            // this code is here temporarily until we can retrieve these trees
            senderIndex.buildFromMessagesFile(Globals.SENDER_ID);
            receiverIndex.buildFromMessagesFile(Globals.RECEIVER_ID);

            // retrieve index with key sender
            // retrieve index with key receiver
            
            // retrieve the availabe list
            error = FileIO.retrieveAvailableList(Globals.AVAILABLE_LIST_FILE);
            if (error == Globals.PROCESS_OK) {
                System.out.print(Globals.availableList.getHead());
                if (Globals.availableList.getHead() == null) System.out.println();
                System.out.println("Total records at start: " +  Globals.totalRecordsInMessageFile);

                recordNumber = 0;               
                do {
                    System.out.println("Options: ");
                    System.out.println(" 1. add");
                    System.out.println(" 2. delete");
                    System.out.println(" 3. print all messages");
                    System.out.println(" 4. find message from full id sender + receiver + dateTime");
                    System.out.println(" 5. find message from full id receiver + sender + dateTime");
                    System.out.println(" 6. find messages from partial id (sender index)");
                    System.out.println(" 7. rebuild available list");
                    System.out.println(" 8. rebuild indices");
                    System.out.println("50. add lots of messages for testing");
                    System.out.println("51. print breadth first sender and receiver trees");
                    System.out.print  ("99. quit: ");
                    System.out.println();
                    System.out.print  ("Option -> ");
                    String c  = in.nextLine();
                    command = Integer.parseInt(c);

                    switch(command) {
                        case 1 : System.out.print("Subject: ");
                                 subject = in.nextLine();

                                 System.out.print("Message: ");
                                 text = in.nextLine(); 

                                 System.out.print("Sender id (9 bytes): ");
                                 sender = in.nextLine();

                                 System.out.print("Receiver id (9 bytes): ");
                                 receiver = in.nextLine();

                                 System.out.print("Time stamp (8 bytes): ");
                                 timeStamp = in.nextLine();

                                 //String identification = sender + receiver + Utils.longToBytesStr(System.currentTimeMillis());
                                 // then also one with receiver + sender + ...

                                 identification = sender + receiver + timeStamp;
                                 entireMessage = 'C' + 
                                                 identification +
                                                 Globals.FIRST_RECORD_OF_MESSAGE + 
                                                 subject + 
                                                 Globals.END_OF_SUBJECT_MARKER + 
                                                 text;

                                 message.setMessage(entireMessage);
                                 recordNumber = message.writeToMessagesFile();
                                 message.printFromMessagesFile(recordNumber);

                                 p = new TreeNode(identification, recordNumber, null, null, null);
                                 senderIndex.insertNode(p);

                                 identification = receiver + sender + timeStamp;
                                 p = new TreeNode(identification, recordNumber, null, null, null);
                                 receiverIndex.insertNode(p);

                                 break;

                        case 2 : System.out.print("Message identification (sender first): ");
                                 identification = in.nextLine();

                                 p = senderIndex.findNode(identification);
                                 if (p != null) {
                                     recordNumber = p.getRecordNumber();
                                     message.deleteFromMessagesFile(recordNumber);
                                     senderIndex.deleteNode(p);
                                     receiverIndex.deleteNode(p);
                                     System.out.println("Message deleted. Id: " + identification + " at record number " + recordNumber);
                                 }
                                 else {
                                     System.out.println("identification not found");
                                 }
                                 break;

                        case 3 : message.printAllFromMessagesFile();
                                 break;

                        case 4 : System.out.print("Sender message identification: ");
                                 identification = in.nextLine();

                                 p = senderIndex.findNode(identification);
                                 if (p != null) {
                                    recordNumber = p.getRecordNumber();
                                    System.out.println("Record number: " + recordNumber);
                                    message.printFromMessagesFile(recordNumber);
                                 }
                                 else
                                    System.out.println("***identification not found");
                                 break;

                        case 5 : System.out.print("Receiver message identification: ");
                                 identification = in.nextLine();

                                 p = receiverIndex.findNode(identification);
                                 if (p != null) {
                                    recordNumber = p.getRecordNumber();
                                    System.out.println("Record number: " + recordNumber);
                                    message.printFromMessagesFile(recordNumber);
                                 }
                                 else
                                    System.out.println("***identification not found");
                                 break;

                        case 6 : System.out.print("Partial message identification: (sender)");
                                 identification = in.nextLine();

                                 p = senderIndex.findNode(identification, 0);
                                 if (p != null) {
                                    System.out.print("First in tree with this partialKey: ");
                                    recordNumber = p.getRecordNumber();
                                    System.out.println("Record number: " + recordNumber);
                                    message.printFromMessagesFile(recordNumber);
                                 }
                                 else
                                    System.out.println("***identification not found");
                                 
                                 TreeNode q = senderIndex.findNode(identification,  1);
                                 if (q != null) {
                                    System.out.print("Last in tree with this partialKey: ");
                                    recordNumber = q.getRecordNumber();
                                    System.out.println("Record number: " + recordNumber);
                                    message.printFromMessagesFile(recordNumber);
                                 }
                                 else
                                    System.out.println("***identification not found");
                                 
                                 senderIndex.printTree(p, q);
                                 
                                 
                                 break;

                        case 7 : // flush the file here
                                 Globals.availableList = new AvailableList();
                                 Globals.availableList.buildFromMessagesFile();
                                 break;
                            
                        case 8 : senderIndex   = new Tree();
                                 receiverIndex = new Tree();
        
                                 senderIndex.buildFromMessagesFile(Globals.SENDER_ID);
                                 receiverIndex.buildFromMessagesFile(Globals.RECEIVER_ID);
                                 break;

                        case 50: // add lots of messages
                                 for (int messageNumber = 0; messageNumber < 497; messageNumber++) {
                                    // made up of 8 zeroes and the last digit of messageNumber
                                    //sender = "" + messageNumber;
                                    //sender = sender.substring(sender.length() - 1);
                                    //sender = Utils.leftPad(sender, 9, '0');
                                    System.out.println(messageNumber % 3);
                                    
                                    switch (messageNumber % 3) {
                                        case 0 : sender = "Peter Lar";
                                                 break;
                                        case 1 : sender = "Conductor";
                                                 break;
                                        case 2 : sender = "Mr.Portor";
                                                 break;
                                    }
                                    
                                    //receiver = "" + (messageNumber + 1);
                                    //receiver = receiver.substring(receiver.length() - 1);
                                    //receiver = Utils.leftPad(receiver, 9, '0');
                                    
                                    switch (messageNumber % 3) {
                                        case 0 : receiver = "Mr.Portor";
                                                 break;
                                        case 1 : receiver = "Conductor";
                                                 break;
                                        case 2 : receiver = "Peter Lar";
                                                 break;
                                    }
                                    
                                    //Utils.delay(10);
                                    //timeStamp = Utils.longToBytesStr(System.currentTimeMillis());
                                    timeStamp = Utils.leftPad("" + messageNumber, 8, '0');
                                    identification = sender + receiver + timeStamp;
                                                     
                                    subject = "subject " + messageNumber;
                                    text    = "Hello how are you today? This is a longer test so that it will take more than one record. Message:" + messageNumber;
                                    
                                    entireMessage = 'C' + 
                                                    identification +
                                                    Globals.FIRST_RECORD_OF_MESSAGE + 
                                                    subject + 
                                                    Globals.END_OF_SUBJECT_MARKER + 
                                                    text;
                                    
                                    message.setMessage(entireMessage);
                                    recordNumber = message.writeToMessagesFile();

                                    p = new TreeNode(identification, recordNumber, null, null, null);
                                    senderIndex.insertNode(p);
                                    identification = receiver + sender + timeStamp;
                                    p = new TreeNode(identification, recordNumber, null, null, null);
                                    receiverIndex.insertNode(p);
                                 }
                                 break;

                        case 51: senderIndex.breadthFirstPrint(); 
                                 receiverIndex.breadthFirstPrint(); 
                                 break;   

                    }
                    System.out.print(Globals.availableList.getHead());
                    if (Globals.availableList.getHead() == null) System.out.println();

                } while (command != 99);

                error = FileIO.saveAvailableList(Globals.AVAILABLE_LIST_FILE);
            }
            else {
                System.out.println("Error opening available list file");
            }

            error = FileIO.closeMessagesFile();
            
            //if (Globals.senderIndexRoot != null) Globals.senderIndexRoot.save(Globals.SENDER_TREE_FILE);
            //if (Globals.recipientIndexRoot != null) Globals.recipientIndexRoot.save(Globals.RECIPIENT_TREE_FILE);
        }
        else {
            System.out.println("Error opening messages file Messages.bci");
        }
*/
    }
    
    public static boolean correctSyntax(String request) {
        boolean result = false;
        if (request.length() >= Globals.COMMAND_LEN + 
                                Globals.SENDER_LEN +
                                Globals.RECEIVER_LEN +
                                Globals.DATE_TIME_LEN + 
                                Globals.MARKER_LEN +
                                Globals.END_OF_SUBJECT_MARKER_LEN) {
            if (request.indexOf(Globals.FIRST_RECORD_OF_MESSAGE) != -1) {
                if (request.indexOf(Globals.END_OF_SUBJECT_MARKER) != -1) {
                    result = true;
                }
                else {
                    System.out.println("End of subject marker missing");
                }
            }
            else {
                System.out.println("Message marker missing");
            }
            
        }
        else {
            System.out.println("Incorrect request length");
        }
            
        return result;
    }
    
    
}
