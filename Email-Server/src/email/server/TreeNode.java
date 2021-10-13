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
public class TreeNode {
    private String id = "";
    private int recordNumber = -1;
    private TreeNode left   = null;
    private TreeNode right  = null;
    private TreeNode parent = null;
    
    public TreeNode() {
        id    = "";
        recordNumber = -1;
        left   = null;
        right  = null;
        parent = null;
    }
    
    public TreeNode(String k, int rn, TreeNode l, TreeNode r, TreeNode p) {
        id    = k;
        recordNumber = rn;
        left   = l;
        right  = r;
        parent = p;
    }
    
    public String getId() {
        return id;
    }
    
    public int getRecordNumber() {
        return recordNumber;
    }
    
    public TreeNode getLeft() {
        return left;
    }
    
    public TreeNode getRight() {
        return right;
    }
    
    public TreeNode getParent() {
        return parent;
    }
    
    public void setId(String k) {
        id = k;
    }
    
    public void setRecordNumber(int rn) {
        recordNumber = rn;
    }
    
    public void setLeft(TreeNode l) {
        left = l;
    }
    
    public void setRight(TreeNode r) {
        right = r;
    }
    
    public void setParent(TreeNode p) {
        parent = p;
    }
    
    public String toString() {
        if (this == null)
            return "null";
        else
            return "Id: " + id + " Record number: " + recordNumber;
    }
}
