import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class bbst{
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException{
		Scanner scan = new Scanner(new File(args[0]));
		RBTree rbtree = new RBTree();
		int total=scan.nextInt();
		//System.out.println(total);
//		while(scan.hasNextInt()){
//			int id=scan.nextInt();
//			int count=scan.nextInt();
//			rbtree.add(id, count);
//		}
		int i=-1;
		int[] ids = new int[total];
		int[] counts=new int[total];
		while(scan.hasNextInt()){
			i++;
			ids[i]=scan.nextInt();
			counts[i]=scan.nextInt();
		}
		rbtree.initialization(ids, counts, 0, total);
		rbtree.print();
		System.out.println("Insertion Complete");
		Scanner read = new Scanner(System.in);
		int a=0;
		int b=0;
		while(read.hasNextLine()){
			String command=read.nextLine();
			String[] commands=command.split(" ");
			if(commands.length==1) return;
			if(commands.length==2){
				a=Integer.parseInt(commands[1]);
			}
			else{
				a=Integer.parseInt(commands[1]);
				b=Integer.parseInt(commands[2]);
			}
			switch(commands[0]){
			case "increase":
				rbtree.increase(a, b);
				break;
			
			case "reduce":
				rbtree.reduce(a, b);
				break;
				
			case "count":
				rbtree.count(a);
				break;
				
			case "inrange":
				rbtree.inRange(a, b);
				break;
				
			case "next":
				rbtree.next(a);
				break;
				
			case "previous":
				rbtree.previous(a);
				break;
				
			default:
					break;
			}
		}
	}
}