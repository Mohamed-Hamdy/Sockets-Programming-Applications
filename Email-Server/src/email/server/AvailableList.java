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
public class AvailableList {
    private Available head = null;
    private Available tail = null;
    
    public AvailableList() {
        head = null;
        tail = null;
    }
    
    public Available getHead() {
        return head;
    }
    
    public Available getTail() {
        return tail;
    }
    
    public void setHead(Available p) {
        head = p;
    }
    
    public void setTail(Available p) {
        tail = p;
    }
    
    public void buildFromMessagesFile() {
	int recordNumber = 0;
	// rebuild the available list; almost same code as delete records method. they could be simplified
	Record record = new Record();
	for (recordNumber = 0; recordNumber < Globals.totalRecordsInMessageFile; recordNumber++) {
	    record.readFromMessagesFile(recordNumber);
	    if (record.getData().charAt(0) == Globals.DELETED) {
                addRecord(recordNumber);
	    }
	}
    }
    
    // return the next available spot or AVAILABLE_LIST_IS_EMPTY (-1)
    // remove from linked list
    
    public int nextAvailableRecord() {
        int record = head.getRecordNumber();
        head = head.getNext();
        return record;
    }
    
    public void addRecord(int recordNumber) {
        if (head == null) {
            head = new Available(recordNumber);
            tail = head;
        }
        else {
            Available p = new Available(recordNumber);
            tail.setNext(p);
            tail = p;
        }
    }            
}
