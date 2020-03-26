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
	private final Condition busEmpty = lock.newCondition(); //condiçao que simboliza o autocarro estar vazio
	private final Condition waitToArrive = lock.newCondition(); //condiçao que simboliza a espera da chegada do autocarro
	
	
	private boolean arrive=false;
	private Bus bus;
	//private Logger logger;
	
	
	//construtor
	public DepartureTransTerm(Bus bus, Logger logger) {
		this.bus = bus;
		//this.logger = logger;
	}

	//função para a saida do autocarro
	public boolean leaveTheBus(Passenger P) {
		lock.lock();
		try {
			while(!this.arrive) { //enquanto o autocarro nao tiver chegado, fica em espera
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
