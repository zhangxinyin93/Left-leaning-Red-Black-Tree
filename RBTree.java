import java.util.ArrayList;
import java.util.LinkedList;

/**Implementation of Red-Black tree based on Left-leaning Red-Black Tree(LLRB)
 * @param <Key>
 * @param <Value>
 */
public class RBTree{
	private int amount;//use for calculate the amount of count in inRnange method
	
	/****************************************************************************
	 *Private class RBNode
	 *Only use in RBTree class
	 ****************************************************************************/
	private class RBNode{
		int id;
		int count;
		RBNode left;
		RBNode right;
		boolean isRed;//red=true,black=false;
		
		RBNode(int key, int val){
			this.id=key;
			this.count=val;
			//parent=null;
			left=null;
			right=null;
			isRed=true;
		}
	}
	
	private RBNode root;//new a root for red-black tree
	
	/****************************************************************************
	 * Basic operation for red-black tree when do insertion
	 * left rotate,right rotate,color flip
	 ****************************************************************************/
	private RBNode LeftRotate(RBNode n){
		RBNode tmp=n.right;
		n.right=tmp.left;
		tmp.left=n;
		tmp.isRed=n.isRed;
		n.isRed=true;
		return tmp;
	}
	
	private RBNode RightRotate(RBNode n){
		RBNode tmp=n.left;
		n.left=tmp.right;
		tmp.right=n;
		tmp.isRed=n.isRed;
		n.isRed=true;
		return tmp;
	}
	
	private void ColorFlip(RBNode n){
		n.isRed=!n.isRed;
		n.left.isRed=!n.left.isRed;
		n.right.isRed=!n.right.isRed;
	}
	
	private boolean red(RBNode n){
		if(n==null) return false;//external node will be colored as black
		return n.isRed;
	}
	
	/****************************************************************************
	 * Basic operation for red-black tree when do deletion
	 * moveRedLeft(),moveRedRight(),minNode(),maxNode(),fix()
	 ****************************************************************************/
	//push the red node on the left route
	//LLRB->won't have red on right then check only need to check if there is any red node in the left part of right subtree
	//try to move all red to left
	//actually this is the way to combine
	private RBNode moveRedLeft(RBNode n){
		ColorFlip(n);//push red down
		if(red(n.right.left)){
			n.right=RightRotate(n.right);
			n=LeftRotate(n);
			ColorFlip(n);
		}
		return n;
	}
	
	private RBNode moveRedRight(RBNode n){
		ColorFlip(n);
		if(red(n.left.left)){
			n=RightRotate(n);
			ColorFlip(n);
		}
		return n;
	}
	
	private RBNode minNode(RBNode n){
		while(n.left!=null){
			n=n.left;
		}
		return n;
	}
	
	private RBNode maxNode(RBNode n){
		while(n.right!=null){
			n=n.right;
		}
		return n;
	}
	
	private RBNode fix(RBNode n){
		if(red(n.right) && !red(n.left)) n=LeftRotate(n);
		if(red(n.right) && red(n.left)) ColorFlip(n);
		if(red(n.left) && red(n.left.left)) n=RightRotate(n);
		return n;
	}
	/************************************************************
	 * add()
	*public insertion method that can be used 
	*And because every time this recursion will return the root
	*always change the color of root back to black
	*************************************************************/
	public void add(int id,int count){
		root=add(root,id,count);
		root.isRed=false;
	}
	
	private RBNode add(RBNode n,int id,int count){
		if(n==null) return new RBNode(id,count);
		
		//if parent n is not null then do this recursive fashion
		if(id<n.id) n.left=add(n.left,id,count);
		else if(id>n.id) n.right=add(n.right,id,count);//!!!else if!!!if without else then it will change all count!!
		else n.count=count;
		
		//Fix the balance of red-black tree
		if(red(n.right) && !red(n.left)) n=LeftRotate(n);
		if(red(n.right) && red(n.left)) ColorFlip(n);//No rotation occurs here, just change the color,split a 4 node
		if(red(n.left) && red(n.left.left)) n=RightRotate(n);//for the reason that this is a LLRB, thus all red nodes sit on left
		return n;
	}
	
	/*************************************************************
	 * delete()
	 * Full implementation for delete() method for LLRB
	 * Basic idea is that move red nodes down to bottom, then actually do delete for 3 or 4 node which means always delete the red
	 * finally use fix() to fix the unbalance
	 **************************************************************/
	public void deleteMin(){
		if(!red(root.left) && !red(root.right)) root.isRed=true;
		root=deleteMin(root);
		root.isRed=false;
	}
	
	private RBNode deleteMin(RBNode n){
		if(n.left==null) return null;
		if(!red(n.left) && !red(n.left.left)){
			n=moveRedLeft(n);
		}
		n.left=deleteMin(n.left);
		return fix(n);
	}
	public void delete(int id){
		boolean flag=containsKey(id);
		if(!flag){
			System.out.println("id "+id+" doesn't exist");
			return;
		}
		if(!root.left.isRed && !root.right.isRed) root.isRed=true;
		root=delete(root,id);
		root.isRed=false;
	}
	
	private RBNode delete(RBNode n,int id){
		if(id<n.id){
			if(!red(n.left) && !red(n.left.left))
				n=moveRedLeft(n);
			n.left=delete(n.left,id);
		}
		else{
			if(red(n.left)) 
				n=RightRotate(n);
			//delete a leaf node
			if(id==n.id && n.right==null) return null;
			if(!red(n.right) && !red(n.right.left)) 
				n=moveRedRight(n);
			//delete a internal node, by exchanging internal node with the minNode in right subtree->delete at the bottom
			if(id==n.id){
				RBNode tmp=minNode(n.right);
				n.id=tmp.id;
				n.count=tmp.count;
				n.right=deleteMin(n.right);
			}
			else n.right=delete(n.right,id);
		}
		return fix(n);
	}
	
	/*****************************************************
	 * containsKey()
	 * Search Method for the red-black tree based on id
	 * Search method is the same as standard BST
	 * Color at this time can be ignored
	 *****************************************************/
	public boolean containsKey(int id){
		boolean contains=containsKey(root,id);
		return contains;
	}
	
	private boolean containsKey(RBNode n,int id){
		if(n==null) return false;
		if(id>n.id) return containsKey(n.right,id);
		else if(id<n.id) return containsKey(n.left,id);
		else return true;
	}
	/*****************************************************
	 * Helper function
	 * getNode() to get the node
	 *****************************************************/
	public RBNode getNode(int id){
		RBNode n=getNode(root,id);
		return n;
	}
	
	private RBNode getNode(RBNode n,int id){
		if(id>n.id) return getNode(n.right,id);
		else if(id<n.id) return getNode(n.left,id);
		else return n;
	}
	
//	private boolean isLeftChild(RBNode n,int id){
//		if(id==n.left.id) return true;
//		else if(id==n.right.id) return false;
//		else{
//			if(id>n.id) return isLeftChild(n.right,id);
//			else return isLeftChild(n.left,id);
//		}
//	}
	
	private RBNode getBiggerParent(RBNode n,int id){
		if(n.left!=null || n.right!=null){
			if(id==n.left.id) return n;
			else if(id==n.right.id) return getBiggerParent(root,n.id);
			else{
				if(id>n.id) return getBiggerParent(n.right,id);
				else return getBiggerParent(n.left,id);
			}
		}
		return null;
	}
	
	private RBNode getNext(RBNode n,int id){
		if(id>n.id && n.right==null) return getBiggerParent(root,n.id);
		else if(id<n.id && n.left==null) return n;
		else if(id>n.id) return getNext(n.right,id);
		else return getNext(n.left,id);
	}
	
	private RBNode getSmallerParent(RBNode n,int id){
		if(n.left!=null || n.right!=null){
			if(id==n.left.id) return getSmallerParent(root,n.id);
			else if(id==n.right.id) return n;
			else{
				if(id>n.id) return getSmallerParent(n.right,id);
				else return getSmallerParent(n.left,id);
			}
		}
		return null;
	}
	
	private RBNode getPrevious(RBNode n,int id){
		if(id<n.id && n.left==null) return getSmallerParent(root,n.id);
		else if(id>n.id && n.right==null) return n;
		else if(id>n.id) return getPrevious(n.right,id);
		else return getPrevious(n.left,id);
	}
	/********************************************************
	 * Functions Are Required by Project
	 * increase,reduce,count,in-range,previous,next
	 ********************************************************/
	//Function for Increase
	public void increase(int id,int addon){
		if(containsKey(id)){
			RBNode n=getNode(id);
			n.count=n.count+addon;
			System.out.println(n.count);
		}
		else{
			add(id,addon);
			System.out.println(getNode(id).count);
		}
	}
	
	//Function for Reduce
	public void reduce(int id,int m){
		if(containsKey(id)){
			RBNode n=getNode(id);
			n.count=n.count-m;
			if(n.count<=0){
				delete(id);
				System.out.println("0");
			}
			else System.out.println(n.count);
		}
		else System.out.println("0");
	}
	
	//Function for Count
	public void count(int id){
		if(containsKey(id)){
			System.out.println(getNode(id).count);
		}
		else System.out.println("0");
	}
	
	//Function for InRange
	public void inRange(int id1,int id2){
		amount=0;
		if(containsKey(id1) && containsKey(id2)){
			RBNode subroot=inRange(root,id1,id2);
			printRange(subroot,id1,id2);
			System.out.println(amount);
		}
		else if(containsKey(id1) && !containsKey(id2)){
			RBNode end=getPrevious(root,id2);
			RBNode subroot=inRange(root,id1,end.id);
			printRange(subroot,id1,end.id);
			System.out.println(amount);
		}
		else if(!containsKey(id1) && containsKey(id2)){
			RBNode start=getNext(root,id1);
			RBNode subroot=inRange(root,start.id,id2);
			printRange(subroot,start.id,id2);
			System.out.println(amount);
		}
		else{
			RBNode start=getNext(root,id1);
			RBNode end=getPrevious(root,id2);
			RBNode subroot=inRange(root,start.id,end.id);
			printRange(subroot,start.id,end.id);
			System.out.println(amount);
		}
	}
	
	private RBNode inRange(RBNode n,int id1,int id2){
		if(n==null) return null;
		//System.out.println("visit"+n.id+" "+n.left.id);
		if(id1<n.id && id2<n.id) return inRange(n.left,id1,id2);
		if(id1>n.id && id2>n.id) return inRange(n.right,id1,id2);
		return n;
	}
	
	private void printRange(RBNode n,int id1,int id2){
		if(n==null) return;
		else if((n.id<id1 && n.right==null) || (n.id>id2 && n.left==null)) return;
		//System.out.print(n.left.id);
//		else if(n.id<id1 && n.right!=null) printRange(n.right,id1,id2);
//		else if(n.id>id2 && n.left!=null) printRange(n.left,id1,id2);
		if(n.left!=null) printRange(n.left,id1,id2);
		if(n.id>=id1 && n.id<=id2){
			RBNode tmp=getNode(n.id);
			amount=amount+tmp.count;
			//System.out.println(tmp.id+" "+tmp.count+" "+amount);
			//System.out.print(tmp.id+" ");
		}
		if(n.right!=null) printRange(n.right,id1,id2);
	}
	
	//Function for Next
	public void next(int id){
		if(id>=maxNode(root).id) System.out.println("0 0");
		else if(id<minNode(root).id) System.out.println(minNode(root).id+" "+minNode(root).count);
		else if(containsKey(id)){
			RBNode n=getNode(id);
			if(n.right!=null){
				n=minNode(n.right);
				System.out.println(n.id+" "+n.count);
			}
			else{
				RBNode h=getBiggerParent(root,id);
				System.out.println(h.id+" "+h.count);
			}
		}
		else {
			RBNode tmp=getNext(root,id);
			System.out.println(tmp.id+" "+tmp.count);
		}
		
	}
	
	//Function for Previous
	public void previous(int id){
		if(id<=minNode(root).id) System.out.println("0 0");
		else if(id>maxNode(root).id) System.out.println(maxNode(root).id+" "+maxNode(root).count);
		else if(containsKey(id)){
			RBNode n=getNode(id);
			if(n.left!=null){
				n=maxNode(n.left);
				System.out.println(n.id+" "+n.count);
			}
			else{
				RBNode h=getSmallerParent(root,id);
				System.out.println(h.id+" "+h.count);
			}
		}
		else{
			RBNode tmp=getPrevious(root,id);
			System.out.println(tmp.id+" "+tmp.count);
		}
	}
	/*********************************************************
	 * Print to Check Red-Black Tree
	 *********************************************************/
	//print out the current red-black tree
	public void print(){
		//System.out.println("root is "+root.id);
		print(root);
	}
	
	private void print(RBNode n){//level-order traversal
		LinkedList<RBNode> queue = new LinkedList<RBNode>();
		ArrayList<RBNode> curr = new ArrayList<RBNode>();
		boolean flag=true;
		queue.add(n);
		while(queue.isEmpty()==false){
			if(!flag){
				curr = new ArrayList<RBNode>();
				flag=true;
			}
			while(queue.isEmpty()==false){
				RBNode tmp=queue.remove();
				curr.add(tmp);
			}
			for(int i=0;i<curr.size();i++){
				RBNode node = curr.get(i);
				if(node.left!=null){
					flag=false;
					queue.add(node.left);
				}
				if(node.right!=null){
					flag=false;
					queue.add(node.right);
				}
			}
		}
		for(int j=0;j<curr.size();j++){
			curr.get(j).isRed=true;
		}
	}
	
	public void initialization(int[] ids,int[] counts,int start,int end){
		root=initialize(ids,counts,start,end);
	}
	
	private RBNode initialize(int[] ids,int[] counts,int start,int end){
		if(start==end) return null;
		int mid=(start+end)/2;
		RBNode n = new RBNode(ids[mid],counts[mid]);
		n.isRed=false;
		n.left=initialize(ids,counts,start,mid);
		n.right=initialize(ids,counts,mid+1,end);
		return n;
	}
	
	public void check(){
		check(root);
	}
	
	private void check(RBNode n){
		if(n==null) return;
		check(n.left);
		System.out.print(n.id+" ");
		check(n.right);
	}
}