public class test{
	public static void main(String[] args){
		RBTree rbtree = new RBTree();
		int[] ids = {1,2,3,4,5,6,7,8,9,10};
		int[] counts = {1,2,3,4,5,6,7,8,9,10};
		rbtree.initialization(ids, counts, 0, 10);
		rbtree.check();
	}
}