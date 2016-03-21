public class RBNode{
	int id;
	int count;
	RBNode parent;
	RBNode left;
	RBNode right;
	boolean isRed;
	
	RBNode(){
		id=0;
		count=0;
		parent=null;
		left=null;
		right=null;
		isRed=false;
	}
	
	RBNode(int a,int b){
		id=a;
		count=b;
		parent=null;
		left=null;
		right=null;
		isRed=true;
	}
}