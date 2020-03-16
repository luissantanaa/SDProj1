package monitors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import airport.Bag;

public class TempStorageArea {
	private final ReentrantLock lock = new ReentrantLock();
	List<Bag> bags;
	public TempStorageArea() {
		bags = new ArrayList<Bag>();
	}
	public boolean addBag(Bag bag){
		lock.lock();
		try {
			if(Math.random() > 0.25) {
				bags.add(bag);
				return true;
			} 
		}finally {
			lock.unlock();
		}
		return false;
	}
}
