package telran.util.concurrent;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyListBlockingQueue<E> implements BlockingQueue<E> {
	//TODO fields of the class - FDone
	private Queue<E> queue = new LinkedList<>();
	private int maxQueueSize  = Integer.MAX_VALUE;
	private ReentrantLock mutex = new ReentrantLock();
	private Condition producersWaiting = mutex.newCondition();
	private Condition consumersWaiting = mutex.newCondition();
	
	public MyListBlockingQueue(int limit) {
		//TODO done
		maxQueueSize = limit; 
	}
	// Default constructor
	private MyListBlockingQueue() {}
 
	@Override
	public E remove() {
		// TODO -  Done
		try {
			mutex.lock();
			E e = queue.remove();
			producersWaiting.signal();
			return e;
		} finally {
			mutex.unlock();
		}
	}
	
	@Override
	public E poll() {
		// TODO - Done
		try {
			mutex.lock();
			E e = queue.poll();
			if(e != null) {
				producersWaiting.signal();
			}
			return e;
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public E element() {
		// TODO - Done
		try {
			mutex.lock();
			return queue.element();
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public E peek() {
		// TODO - Done
		try {
			mutex.lock();
			return queue.peek();
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public int size() {
		// TODO - Done
		try {
			mutex.lock();
			return queue.size();
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		// TODO - Done   
		try {
			mutex.lock();
			return queue.isEmpty();
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public Iterator<E> iterator() {
		
		return null;
	}

	@Override
	public Object[] toArray() {
		
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		
		return null;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		
		return false;
	}

	@Override
	public void clear() {
		// TODO - Done
		try {
			mutex.lock();
			if(!queue.isEmpty()) {
				producersWaiting.signal();
				queue.clear();
			}		
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public boolean add(E e) {
		// TODO - Done
		nullValidation(e);
		try {
			mutex.lock();
			queue.add(e);
			consumersWaiting.signal();
			return true;
		} finally {
			mutex.unlock();
		}
	}

	private void nullValidation(Object value) {
		if(value==null) {
			throw new NullPointerException();
		}
	}
	
	@Override
	public boolean offer(E e) {
		// TODO - Done
		nullValidation(e);
		try {
			mutex.lock();
			boolean b = queue.size() == maxQueueSize ? false : queue.add(e);
			if(b) {
				consumersWaiting.signal();
			}
			return b;
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public void put(E e) throws InterruptedException {
		// TODO - Done 
		nullValidation(e);
        try {
    		mutex.lock();
            while (queue.size() == maxQueueSize) {
            	producersWaiting.await();
            }
            queue.add(e);
            consumersWaiting.signal();
        } finally {
        	mutex.unlock();
        }
	}

	@Override
	public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
		// TODO - Done 
		nullValidation(e);
        try {
    		mutex.lock();
            while (queue.size() == maxQueueSize) {
            	if(!producersWaiting.await(timeout, unit)) {
            		return false;
            	}
            }
            queue.add(e);
            consumersWaiting.signal();
            return true;
        } finally {
        	mutex.unlock();
        }
	}

	@Override
	public E take() throws InterruptedException {
		// TODO - Done
        try {
    		mutex.lock();
            while (queue.isEmpty()) {
            	consumersWaiting.await();
            }
            E item = queue.remove();
            producersWaiting.signal();
            return item;
        } finally {
        	mutex.unlock();
        }
	}

	@Override
	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO - Done
        try {
    		mutex.lock();
            while (queue.isEmpty()) {
            	if(!consumersWaiting.await(timeout, unit)) {
            		return null;
            	}
            }
            E item = queue.remove();
            producersWaiting.signal();
            return item;
        } finally {
        	mutex.unlock();
        }
	}

	@Override
	public int remainingCapacity() {
		// TODO - Done
		try {		
			mutex.lock();
			return maxQueueSize - queue.size();
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public boolean remove(Object o) {
		// TODO - Done
		nullValidation(o);
		try {
			mutex.lock();
			boolean b = queue.remove(o);
			if(b) {
				producersWaiting.signal();
			}
			return b;
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public boolean contains(Object o) {
		// TODO - Done
		nullValidation(o);
		try {
			mutex.lock();
			return queue.contains(o);
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public int drainTo(Collection<? super E> c) {
		
		return 0;
	}

	@Override
	public int drainTo(Collection<? super E> c, int maxElements) {
		
		return 0;
	}

}
