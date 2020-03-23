package monitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.DepartureTransTermBDriverInterface;
import Interfaces.DepartureTransTermPassengerInterface;
import airport.Bus;
import airport.Logger;
import airport.Passenger;

public class DepartureTransTerm implements DepartureTransTermPassengerInterface,DepartureTransTermBDriverInterface{
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition busEmpty = lock.newCondition();
	private final Condition waitToArrive = lock.newCondition();
	
	
	private boolean arrive=false;
	private Bus bus;
	Logger logger;
	
	public DepartureTransTerm(Bus bus, Logger logger) {
		this.bus = bus;
		this.logger = logger;
	}

	public boolean leaveTheBus(Passenger P) {
		lock.lock();
		try {
			while(!this.arrive) {
				waitToArrive.await();
			}
			if(arrive) {
				bus.removePassenger(P);	
				return true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			if(bus.PassengerSize() == 0) {
				busEmpty.signal();
			}
			lock.unlock();
		}
		return false;
	}
	
	
	public void arriveDepTransTerm() {
		lock.lock();
		try {
			this.arrive = true;
		}finally {
			waitToArrive.signalAll();
			lock.unlock();
		}
	}
	
	public void leaveDepTransTerm() {
		this.arrive = false;
	}
	
	public void waitForPassengers() {
		lock.lock();
		try {
			while((bus.PassengerSize() != 0)) {
				busEmpty.await();
			}
			leaveDepTransTerm();
			

		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		
		
		}
	}
