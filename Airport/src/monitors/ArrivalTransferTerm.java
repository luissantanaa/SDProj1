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
	
	private boolean lastPass = false;
	private boolean driverLeft = false;
	private boolean lestGo = false;
	
	public ArrivalTransferTerm(Bus bus, Logger logger) {
		this.bus = bus;
		this.logger = logger;
		this.dayWorkEnd = false;
		this.passengersWaiting = new LinkedList<Passenger>();
	}
	
	
	
	
	public boolean enterTheBus(Passenger p) {
		
		lock.lock();
		try {	
			// ADD to queueu
			if(!passengersWaiting.contains(p)) {
				passengersWaiting.add(p);
				logger.addPassengersWaiting(p);
				logger.toPrint();
			}
			
		
			// IF its a 3 passenger wake up driver
			
			if(passengersWaiting.size()>=3 && !driverLeft) {
				lestGo = true;
				busFull.signal();
				passengerWait.signalAll();
			}
			
			//if doesn have space wait
			
			while(!bus.hasSpace() || driverLeft || !lestGo) {
				passengerWait.await();
				
			}
			
			// if it the first one on queueu enter the bus
			if(passengersWaiting.peek() == p) {
				if(bus.addPassenger(passengersWaiting.poll())) {
					logger.removePassengersWaiting(p);
					if(!bus.hasSpace()) {
						lestGo = false;
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
	
	
	
	public void LastPassenger() {
		lock.lock();
		try {	
			lastPass = true;
		}finally {
			busFull.signal();
			lock.unlock();
		}
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
			while((bus.hasSpace() && !this.dayWorkEnd) && (!lastPass && !this.dayWorkEnd)) {	
				busFull.await();
			}
		
				driverLeft = true;
				return false;
			
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
			
			lastPass = false;
			driverLeft = false;
			
			if(passengersWaiting.size()>=3 ) {
				lestGo = true;
			}
			
		}finally {
			passengerWait.signalAll();
			lock.unlock();
		}
	}
}
