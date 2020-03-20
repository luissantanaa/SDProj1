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
	//private final Condition waitToArrive = lock.newCondition();
	
	
	private boolean arrive;
	private Bus bus;
	Logger logger;
	
	public DepartureTransTerm(Bus bus, Logger logger) {
		this.bus = bus;
		this.logger = logger;
		this.arrive = false;
	}

	public void leaveTheBus(Passenger P) {
	
		lock.lock();
		try {
			
		/*while(arrive) {
				
				waitToArrive.await();
			}
			*/
			bus.removePassenger(P);	
		
		}finally {
			if(bus.PassengerSize() == 0) {
				busEmpty.signal();
				
			}
			lock.unlock();
		}
	}
	
	
	public void arriveDepTransTerm() {
		lock.lock();
		try {
			this.arrive = true;
			
		}finally {
			//waitToArrive.signalAll();
			lock.unlock();
		}
	}
	
	public void leaveDepTransTerm() {
		
		this.arrive = false;
	}
	
	public void waitForPassengers() {
		lock.lock();
		try {
			while(!(bus.PassengerSize() == 0)) {
				
				busEmpty.await();
			}
			System.out.print("\n\n\n indo em frente \n\n\n");
			leaveDepTransTerm();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		
		
		}
	}
