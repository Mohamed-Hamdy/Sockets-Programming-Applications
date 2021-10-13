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

public class Tree {
    private TreeNode root = null;
    
    public Tree() {
        root = null;
    }
    
    public Tree(TreeNode r) {
        root = r;
    }
    
    public TreeNode getRoot() {
        return root;
    }
    
    public void setRoot(TreeNode r) {
        root = r;
    }
    
    public TreeNode findNode(String id) {
        if (root == null) {
            return null;
        }
        else if (id.equals(root.getId())) {
            return root;
        }
        else if (id.compareTo(root.getId()) < 0) {
            Tree tree = new Tree(root.getLeft());
            return tree.findNode(id);
        }
        else if (id.compareTo(root.getId()) > 0) {
            Tree tree = new Tree(root.getRight());
            return tree.findNode(id);
        }
        else {
            System.out.println("Fatal error in findNode(String)");
            return null;
        }
    }
    
    // returns the node that starts with partialKey 
    // if parameter 'where' is 0, findNode returns the node with least alphabetical node in the tree
    // if parameter 'where' is 1, findNode returns the node with the most alphabetical node in the tree
    // for example findNode("13", 0) in a tree with ids of length 4 will return the node that has id "1300"
    //             findNode("13", 1) in the same tree will return "1399"
    //             if the partial key is not present, findNode() will return null
    // these two positions can be used to print or generate a sequential list of
    // all nodes between two given nodes
    // null is returned if not found
    
    public TreeNode findNode(String partialKey, int where) {
        if (partialKey.length() == Globals.KEY_LEN) {                           // if full key
            return findNode(partialKey);
        }
        else if (root == null) {                                                // not found
            return null;
        }        
        else {
            int partialKeyLen = partialKey.length();

            if (partialKey.compareTo(root.getId().substring(0, partialKeyLen)) < 0) {
                Tree lTree = new Tree(root.getLeft());
                return lTree.findNode(partialKey, where);
            }
            else if (partialKey.compareTo(root.getId().substring(0, partialKeyLen)) > 0) {
                Tree rTree = new Tree(root.getRight());
                return rTree.findNode(partialKey, where);
            }
            else if (partialKey.equals(root.getId().substring(0, partialKeyLen))) { 
                TreeNode p = root;      // find the smallest or largest from partialKey depending on 'where'                     

                if (where == 0) {
                    TreeNode q = p.getLeft();
                    while (q != null) {
                        if (partialKey.equals(q.getId().substring(0, partialKeyLen))) {
                            p = q;
                            q = q.getLeft();
                        }
                        else {
                            q = q.getRight();
                        }
                    }
                }
                else {
                    TreeNode q = p.getRight();
                    while (q != null) {
                        if (partialKey.equals(q.getId().substring(0, partialKeyLen))) {
                            p = q;
                            q = q.getRight();
                        }
                        else {
                            q = q.getLeft();
                        }
                    }
                }
                return p;
            }
            else {
                System.out.println("Fatal error in findNode(String, int)");
                return null;                                                   
            }
        }
    }
    
    private void insertNodeRec(TreeNode p) {
        if (root == null) {
            root = p;
        }
        else if (p.getId().compareTo(root.getId()) < 0) {
            if (root.getLeft() == null) {
                root.setLeft(p);
                p.setParent(root);
            }
            else {
                Tree tree = new Tree(root.getLeft());
                tree.insertNodeRec(p);
            }
        }
        else if (p.getId().compareTo(root.getId()) > 0){
            if (root.getRight() == null) {
                root.setRight(p);
                p.setParent(root);
            }
            else {
                Tree tree = new Tree(root.getRight());
                tree.insertNodeRec(p);
            }
        }
        else {
            System.out.println("Error inserting a node. Id already exists");
        }
    }
    
    // call needs to be here because it is not possible to
    // put the balance() within the recursive method; this is because
    // the recursive call is made with insantiations of subtrees so
    // the balancing would be for the subtree. It is true that the
    // subtrees have parents and the moving up of the ancestor would
    // eventually reach the root but I don't want to risk it now (April 4 2016)
    
    public void insertNode(TreeNode p) {
        insertNodeRec(p);
        balance(p);
    }

    private void setParentsChildLink(TreeNode p, TreeNode q) {
        if (p.getId().compareTo(p.getParent().getId()) < 0) p.getParent().setLeft(q);
        else if (p.getId().compareTo(p.getParent().getId()) > 0) p.getParent().setRight(q);
        else System.out.println("error in deleting root setting a parents child link");
    }
        
    public void deleteNode(TreeNode p) {
        if (p != null) {  
            TreeNode q = null;
            TreeNode r = null;
            if (p.getLeft() == null && p.getRight() == null) {          // case 1: p is leaf
                r = p.getParent();                                      // there is no node shift
                if (p == root) root = null;                             // link that points to p
                else setParentsChildLink(p, null);                      // becomes null
            }
            else if (p.getLeft() != null && p.getRight() == null ||     // cases 2, 5: p has a left subtree
                     p.getLeft() == null && p.getRight() != null) {     // cases 3, 6: p has a right subtree 
                                
                q = (p.getLeft() != null) ? p.getLeft() : p.getRight(); // determine which side p is in
                
                if (p == root) root = q;                                // q becomes the child of p's parent
                else setParentsChildLink(p, q);                    
                                        
                q.setParent(p.getParent());                             // p's parent becomes q's parent 
                r = q.getParent();                                      // there has been node shift
            }
            else if (p.getLeft() != null && p.getRight() != null) {     // cases 4, 7: p has two subtrees; p's left child has no right child
                q = p.getLeft();
                if (p.getLeft().getRight() == null) {   // case 4, 7a
                    if (p == root) root = q;                            // q becomes the child of p's parent
                    else setParentsChildLink(p, q);
                    
                    q.setParent(p.getParent());                         // p's parent becomes q's parent
                    q.setRight(p.getRight());                           // q's right subtree becomes p's right subtree
                    q.getRight().setParent(q);                          // q becomes p's right child's parent
                    
                    r = q;                                              // there has been a node shift
                }
                else {  // cases 7b, 7c
                    while (q.getRight() != null) q = q.getRight();      // find right most node of left subtree of p
                    
                    r = q.getParent();                                  // there has been a node shift
                    
                    q.getParent().setRight(q.getLeft());                // move the left subtree of q up one level
                    if (q.getLeft() != null) q.getLeft().setParent(q.getParent());
                    
                    if (p == root) root = q;        // q becomes the child of p's parent
                    else setParentsChildLink(p, q); // setParentsChildLink(p, q) but q is always a right child                        
                        
                    q.setParent(p.getParent());     // p's parent becomes q's parent
                    q.setLeft(p.getLeft());         // p's left child becomes q's left child
                    p.getLeft().setParent(q);       // q becomes the parent of p's left child
                    q.setRight(p.getRight());       // p's right child becomes q's right child
                    p.getRight().setParent(q);      // q becomes the parent of p's right child
                } 
            }
            balance(r);
            
            p.setLeft(null);
            p.setRight(null);
            p.setParent(null);
            p = null;           
        }
    }
    
    // count the number of nodes of tree t
    public int nodeCount() {
        if (this.getRoot() == null) {
            return 0;
        }
        else {
            Tree lTree = new Tree(this.getRoot().getLeft());
            Tree rTree = new Tree(this.getRoot().getRight());
            
            return 1 + lTree.nodeCount() + rTree.nodeCount();
        }
    }

    // rotate the tree towards the right with respect to this
    private TreeNode rightRotate() {
	TreeNode p = root.getLeft();
        p.setParent(root.getParent());
        
        root.setLeft(p.getRight());
	
	if (root.getLeft() != null)
            root.getLeft().setParent(root);

        p.setRight(root);
        p.getRight().setParent(p);
	
	return p;
    }
 
    // rotate the tree towards the left with respect to this
    private TreeNode leftRotate() {
        TreeNode p = root.getRight();
        p.setParent(root.getParent());

        root.setRight(p.getLeft());
        
	if (root.getRight() != null)
	    root.getRight().setParent(root);
	
        p.setLeft(root);
        p.getLeft().setParent(p);
	
	return p;
    }

    // calculate the height of the tree: one node is a height of 1
    public int height() {
        int treeHeight = 0;
        if (this.getRoot() != null) {
            Tree lTree = new Tree(this.getRoot().getLeft());
            int lHeight = 1 + lTree.height();
            
            Tree rTree = new Tree(this.getRoot().getRight());
            int rHeight = 1 + rTree.height();
            
            treeHeight = lHeight > rHeight ? lHeight : rHeight;
        }
        return treeHeight;
    }

     // calculate the AVL balance factor: height of left child of this - height of right child of this
    private int balanceFactor() {
	Tree lTree = new Tree(this.getRoot().getLeft());
        Tree rTree = new Tree(this.getRoot().getRight());
        return lTree.height() - rTree.height();
    }
 
    // balance the tree starting at this according to AVL self-balancing algorithm
    // if called after insertion, p is the node that has just been inserted
    // if called after deletion, p will have to be one of these two:
    //
    //  1) a leaf that has been deleted: In the calling method, the passed 
    //     parameter will still have the link information even if the node has been
    //     deleted in deleteNode(); thus it can be safely passed into here
    //  2) the node that has been shifted up and has taken q's place since the parent of this node may
    //     experience unbalancing
    //
    //  It might be best to do the call to balance() from within the insertNode() and
    //  the deleteNode() so that all this node information is available

    private void balance(TreeNode p) { 
        if (p != null) {
            TreeNode ancestor = p;

            while (ancestor != null) {
                Tree ancestorTree = new Tree(ancestor);            

                if (ancestorTree.balanceFactor() == -2) {
                    Tree rTree = new Tree(ancestorTree.root.getRight());                

                    int rTreeBalanceFactor = rTree.balanceFactor();
                    if (rTreeBalanceFactor == -1 || rTreeBalanceFactor == 0) { //0 happens in delete case 7a
                        if (ancestor == root)
                            root = ancestorTree.leftRotate();
                        else { 

                            // determine if the ancestor is a left or a right child

                            if (ancestor.getParent().getLeft() == ancestor)
                                ancestor.getParent().setLeft(ancestorTree.leftRotate()); 
                            else
                                ancestor.getParent().setRight(ancestorTree.leftRotate());
                        }
                    }
                    else if (rTreeBalanceFactor == 1 || rTreeBalanceFactor == 0) {
                        ancestor.setRight(rTree.rightRotate());
                        if (ancestor == root)
                            root = ancestorTree.leftRotate();
                        else {

                            // determine if the ancestor is a left or a right child

                            if (ancestor.getParent().getLeft() == ancestor)
                                ancestor.getParent().setLeft(ancestorTree.leftRotate());
                            else
                                ancestor.getParent().setRight(ancestorTree.leftRotate());
                        }
                    }
                }
                else if (ancestorTree.balanceFactor() == 2) {
                    Tree lTree = new Tree(ancestorTree.root.getLeft());

                    int lTreeBalanceFactor = lTree.balanceFactor();
                    if (lTreeBalanceFactor == 1 || lTreeBalanceFactor == 0) { // 0 this symmetrical case of 7a does not happen. it's here to make the method symmetric and possible future optimization
                       if (ancestor == root)
                           root = ancestorTree.rightRotate();
                       else {

                           // determine if the ancestor is a left or a right child

                           if (ancestor.getParent().getLeft() == ancestor)
                               ancestor.getParent().setLeft(ancestorTree.rightRotate());
                           else
                               ancestor.getParent().setRight(ancestorTree.rightRotate());
                       }
                    }
                    else if (lTreeBalanceFactor == -1 || lTreeBalanceFactor == 0) {
                        ancestor.setLeft(lTree.leftRotate());
                        if (ancestor == root)
                            root = ancestorTree.rightRotate();
                        else {

                            // determine if the ancestor is a left or a right child

                            if (ancestor.getParent().getLeft() == ancestor)
                                ancestor.getParent().setLeft(ancestorTree.rightRotate());
                            else
                                ancestor.getParent().setRight(ancestorTree.rightRotate());
                        }
                    }
                }
                ancestor = ancestor.getParent(); // ancestor may have changed in rotation. ancestor keeps moving up anyway and will reach the null of the root's parent
            }
        }
    }
    
    public void printTree() {
        if (root != null) {
            Tree tree = null;
            
            tree = new Tree(root.getLeft());
            tree.printTree();
            
            System.out.println(root);
                    
            tree = new Tree(root.getRight());
            tree.printTree();
        }
    }
    
    public void prepareTransmissionString() {
        if (root != null) {
            Tree tree = null;
            
            tree = new Tree(root.getLeft());
            tree.prepareTransmissionString();
            
            System.out.println(root);
            NetIO.addToTransmissionString(root.getRecordNumber());
                    
            tree = new Tree(root.getRight());
            tree.prepareTransmissionString();
        }
    }
    // prints in sequence all the node information in the range [start.getId(), end.getId()]
    // neither start nor end can be null; otherwise method crashes; 
    // start.getId() must be <= end.getId()
    //
    // to find the nodes, use the findNode(String, int, int) method that finds nodes using partial keys
    // Method does nothing if start.id > end.id
    public void printTree(TreeNode start, TreeNode end) {
        if (start != null && end != null) {
            if (start.getId().compareTo(end.getId()) <= 0) {
                if (start.getId().compareTo(root.getId()) <= 0 && root.getId().compareTo(end.getId()) <= 0) {            
                    TreeNode p = start;
                    while (p != null && p != root) {
                        System.out.println(p);

                        Tree rTree = new Tree(p.getRight());
                        rTree.printTree();

                        do {
                            p = p.getParent();
                        } while (p != null && p.getId().compareTo(start.getId()) < 0);  //move up to ancestor that is within range                
                    }
                    System.out.println(root);

                    p = root.getRight();
                    while (p != null) {
                        if (p.getId().compareTo(end.getId()) <= 0) {
                            Tree lTree = new Tree(p.getLeft());
                            lTree.printTree();
                            System.out.println(p);
                            p = p.getRight();
                        }
                        else {
                            p = p.getLeft();
                        }               
                    }
                }
                else if (end.getId().compareTo(root.getId()) < 0) {         // start and end are in left subtree
                    Tree lTree = new Tree(root.getLeft());
                    lTree.printTree(start, end);
                }
                else if (start.getId().compareTo(root.getId()) > 0) {       // start and end are in right subtree
                    Tree rTree = new Tree(root.getRight());
                    rTree.printTree(start, end);
                }
            }
        }
    }
    
    // sends to clientIPAddress in sequence all the node information in the range [start.getId(), end.getId()]
    // works based on printTree(TreeNode, TreeNode) but sends instead of printing 
    public void prepareTransmissionString(TreeNode start, TreeNode end) {
        if (start != null && end != null) {
            if (start.getId().compareTo(end.getId()) <= 0) {
                if (start.getId().compareTo(root.getId()) <= 0 && root.getId().compareTo(end.getId()) <= 0) {            
                    TreeNode p = start;
                    while (p != null && p != root) {
System.out.println(p);
                        NetIO.addToTransmissionString(p.getRecordNumber());

                        Tree rTree = new Tree(p.getRight());
                        rTree.prepareTransmissionString();

                        do {
                            p = p.getParent();
                        } while (p != null && p.getId().compareTo(start.getId()) < 0);  //move up to ancestor that is within range                
                    }
System.out.println(root);
                    NetIO.addToTransmissionString(p.getRecordNumber());

                    p = root.getRight();
                    while (p != null) {
                        if (p.getId().compareTo(end.getId()) <= 0) {
                            Tree lTree = new Tree(p.getLeft());
                            lTree.prepareTransmissionString();
System.out.println(p);
                            NetIO.addToTransmissionString(p.getRecordNumber());
                            p = p.getRight();
                        }
                        else {
                            p = p.getLeft();
                        }               
                    }
                }
                else if (end.getId().compareTo(root.getId()) < 0) {         // start and end are in left subtree
                    Tree lTree = new Tree(root.getLeft());
                    lTree.prepareTransmissionString(start, end);
                }
                else if (start.getId().compareTo(root.getId()) > 0) {       // start and end are in right subtree
                    Tree rTree = new Tree(root.getRight());
                    rTree.prepareTransmissionString(start, end);
                }
            }
        }
    }
    
    // print all nodes at level n
    private void printNodesOfOneLevel(TreeNode p, int level, int currentLevel) {
        if (p != null) {
            if (currentLevel == level) {
                System.out.print(p.getId() + " in level " + level);
                
                if (p.getParent() == null) {
                    System.out.println(" Root");
                }
                else if (p.getParent().getLeft() == p) {
                    System.out.println(" Left child of " + p.getParent().getId());
                }
                else {
                    System.out.println(" Right child of " + p.getParent().getId());
                }
            }
            else {
                printNodesOfOneLevel(p.getLeft(), level, currentLevel + 1);
                printNodesOfOneLevel(p.getRight(), level, currentLevel + 1);
            }
        }
    }
    
    // breadth-first print: If we save the tree in the order of this print then
    // when the tree is retrieved the nodes will go in by level and the tree will
    // already be balanced (assuming it was balanced when it was saved)
    public void breadthFirstPrint() {
        int treeHeight = height();
        for (int level = 0; level <= treeHeight; level++) {
            printNodesOfOneLevel(root, level, 0);
        }
    }
    
  public void breadthFirstSave(String fileName){
    try
    {
      RandomAccessFile messageTree = new RandomAccessFile (fileName, "rw");

      for(int i = 0; i < height(); i ++)
        writeLevel(i,messageTree);

      messageTree.close();
    }
    catch (IOException e)
    {      
      System.out.println (e);
    }
  }

  public void writeLevel(int level, RandomAccessFile f){
        if(level == 0){ 
            try {
                if(root != null){
                    f.write(root.getId().getBytes());
                    f.writeInt(root.getRecordNumber());
                }
            }
            catch (IOException e) {      
                System.out.println (e);
            }  
        }
        else if(root!=null){
            Tree t = new Tree(root.getLeft());
            t.writeLevel(level-1,f);
            t = new Tree(root.getRight());
            t.writeLevel(level-1,f);
        }
  }
    
    public void breadthFirstRetrieve(String fileName) {
    }
    
    // rebuild the index depending on what index
    public void buildFromMessagesFile(int whatId) {
	int recordNumber = 0;
	Record record = new Record();
	for (recordNumber = 0; recordNumber < Globals.totalRecordsInMessageFile; recordNumber++) {
	    record.readFromMessagesFile(recordNumber);
	    if (record.getData().charAt(Globals.MARKER_POS) == Globals.FIRST_RECORD_OF_MESSAGE) {
                Message message = new Message();
                message.readFromMessagesFile(recordNumber);
                
                String key = Globals.STR_NULL;
                if (whatId == Globals.SENDER_ID) 
                    key = message.getSender() + message.getReceiver() + message.getDateTime();
                else if (whatId == Globals.RECEIVER_ID)
                    key = message.getReceiver() + message.getSender() + message.getDateTime();
                else
                    System.out.println("***invalid whatKey parameter in buildFromMessagesFile()");
                                        
                TreeNode p = new TreeNode(key, recordNumber, null, null, null);
                insertNode(p);                
	    }
	}
    }
    
    public String toString() {
        if (root == null)
            return "Null root";
        else
            return "Root Id: " + root.getId();
    }
}
