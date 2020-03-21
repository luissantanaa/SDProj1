package monitors;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.ArrivalTransferTermBDriverInterface;
import Interfaces.ArrivalTransferTermPassengerInterface;
import airport.Bus;
import airport.BusDriver;
import airport.Logger;
import airport.Passenger;

public class ArrivalTransferTerm implements ArrivalTransferTermPassengerInterface, ArrivalTransferTermBDriverInterface{
	
	
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition busFull = lock.newCondition();
	private final Condition passengerWait = lock.newCondition();
	
	private Bus bus;
	private Logger logger;
	private boolean dayWorkEnd;
	private Queue<Passenger> passengersWaiting;
	
	
	public ArrivalTransferTerm(Bus bus, Logger logger) {
		this.bus = bus;
		this.logger = logger;
		this.dayWorkEnd = false;
		this.passengersWaiting = new LinkedList<Passenger>();
	}
	
	
	
	
	public boolean enterTheBus(Passenger p) {
		if(!passengersWaiting.contains(p)) {
			//System.out.println("\n\n\nenterTheBus" + p.getId() +"\n\n\n");
			
			passengersWaiting.add(p);
			logger.addPassengersWaiting(p);
		}
		lock.lock();
		try {	
			while(!bus.hasSpace()) {
				//System.out.println("\n\n\nenterTheBus" + p.getId() +"\n\n\n");
				passengerWait.await();
			}
			if(passengersWaiting.peek() == p) {
				if(bus.addPassenger(passengersWaiting.poll())) {
					
					logger.removePassengersWaiting(p);
					if(!bus.hasSpace()) {
						
						busFull.signal();
					}
					return true;
				}
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return false;
			
	}
	
	
	
	public void dayWordEnd() {
		lock.lock();
		try {
			this.dayWorkEnd = true;
			busFull.signal();
		}finally {
			lock.unlock();
		}
		
	}
	
	public boolean BusNotFull() {
		
		lock.lock();
		try {
			while(bus.hasSpace() && !this.dayWorkEnd) {
				
				busFull.await();
			}
			if(!bus.hasSpace()) {
			
				return false;
			}
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return true;
	}

	public boolean hasDaysWorkEnded() {
		return this.dayWorkEnd;	
	}
	
	public void announcingBusBoarding() {
		lock.lock();
		try {
			//System.out.println("\n\n\n Bus Boarding\n\n\n");
			passengerWait.signalAll();
		}finally {
			lock.unlock();
		}
	}
}
