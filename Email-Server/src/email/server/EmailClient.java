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
public class EmailClient {
    public static void main(String[] args) {
	// initialize boxMessages list

	for (int i = 0; i < Globals.boxMessages.length; i++) {
	    Globals.boxMessages[i] = Globals.EMPTY_CLIENT_MESSAGE;
	}
		
	int error = MailTransfers.EmailClientRequestAllMail(Globals.RECEIVER_ID);
	//System.out.println("Done");
	if (error == Globals.PROCESS_OK) {
	    EmailClientGUI gui = new EmailClientGUI();
	}
    }
}
