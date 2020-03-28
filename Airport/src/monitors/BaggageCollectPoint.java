package monitors;

import java.util.ArrayList;

import Interfaces.BaggageCollectPointPassengerInterface;
import Interfaces.BaggageCollectPointPorterInterface;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.Math; 

import airport.Bag;
import airport.Logger;

/**
 * 
 * Monitor Baggage Collection Point
 *
 */

public class BaggageCollectPoint implements BaggageCollectPointPorterInterface,BaggageCollectPointPassengerInterface  {
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition bagNotAdded = lock.newCondition();  //condição para acordar o passageiro caso a sua mala seja perdida
	private boolean noMoreBag = false; //boolean que sinaliza a falta de malas para recolher
	private List<Bag> bags;
	//private Logger logger;
	
	
	//construtor
	/**
	 * 
	 * bags é uma lista de malas
	 */
	public BaggageCollectPoint(Logger logger) {
		bags = new ArrayList<Bag>();
		//this.logger = logger;
	}
	
	/**
	 * função que sinaliza que existem malas para recolher
	 */
	public void moreBags() {
		noMoreBag = false;
	}
	
	/**
	 * função usada pelo bagageiro para adicionar malas
	 */
	public boolean addBag(Bag bag){

		boolean lost = true;
		lock.lock();
		try {
			
			if(Math.random() >= 0.25) { //probabilidade da mala ser perdida
				lost = false;
				bags.add(bag);
				return true;
			}
		}finally {
			if(!lost) {
				bagNotAdded.signal();
			}
			lock.unlock();
		}

		return false;
	}
	
	/**
	 * função que sinaliza que nao ha mais malas para recolher
	 */
	public void noMoreBags() {
		lock.lock();
		try {
			noMoreBag = true;
		}finally {
			bagNotAdded.signalAll();
			lock.unlock();
		}
	}
	
	//função para recolher mala
	/**
	 * função usada pelo passageiro para recolher a sua mala
	 */
	public boolean collectBag(Bag bag) {
		lock.lock();
		try {	
			while(!bags.contains(bag) && !noMoreBag) {
				bagNotAdded.await();
			}
				if(bags.contains(bag)) {
					bags.remove(bag);
					
					return true;
				}
				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			
			lock.unlock();
		}	
		return false;
	}
}
