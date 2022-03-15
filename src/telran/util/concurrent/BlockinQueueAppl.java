package telran.util.concurrent;

import java.util.concurrent.BlockingQueue;

public class BlockinQueueAppl {
	public static void main(String[] args) throws InterruptedException {
//		BlockingQueue<Integer> queueDefault = new MyListBlockingQueue<Integer>();
		BlockingQueue<Integer> queue = new MyListBlockingQueue<>(1000);
		queue.add(5);
		queue.offer(8);
		System.out.println("size="+queue.size() + "  isEmpty="+queue.isEmpty()+"  head="+queue.element());
		queue.remove(5);
		System.out.println("head="+queue.peek());
		queue.clear();
		System.out.println("size="+queue.size());
		queue.put(6);
		System.out.println("head="+queue.take()); 
	}
}
