package monitors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.TempStorageInterfacePorter;
import airport.Bag;
import airport.Logger;

/**
 * 
 * Monitor Temporary Storage Area
 *
 */

public class TempStorageArea implements TempStorageInterfacePorter {
	private final ReentrantLock lock = new ReentrantLock();
	List<Bag> bags;
	Logger logger;
	
	//construtor
	/**
	 * bags é uma lista de malas 
	 * @param logger
	 */
	public TempStorageArea(Logger logger) {
		bags = new ArrayList<Bag>();
		this.logger = logger;
	}
	//adição de malas
	/**
	 * função usada pelo bagageiro para adicionar malas
	 */
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
