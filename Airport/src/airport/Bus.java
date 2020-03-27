package airport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * Classe que implementa o autocarro usado durante a simulação
 *
 */

public class Bus {	
	private List<Passenger> passengers;
	private int maxPasse;
	private final ReentrantLock lock = new ReentrantLock();
	
	
	/**
	 * 
	 * @param MaxPasse - nrº maximo de passageiros do autocarro
	 */
	public Bus(int MaxPasse) {
		super();
		this.maxPasse = MaxPasse;
		this.passengers = new ArrayList<Passenger>();
	}
	
	//getters and setters
	public List<Passenger> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}
	public int PassengerSize() {
		return this.passengers.size();
	}
	public boolean hasSpace() {
		return this.passengers.size() != this.maxPasse; 
	}
	
	//adicionar passageiro ao autocarro
	/**
	 * 
	 * @param P - passageiro a adicionar ao autocarro
	 * @return true se passageiro foi adicionado ao autocarro
	 */
	public boolean addPassenger(Passenger P) {
		
		lock.lock();
		try {
			
			if (hasSpace()) {
				this.passengers.add(P);
				return true;
			}
		}finally {
			lock.unlock();
		}

		return false;
	}
	
	//remover passageiro
	/**
	 * 
	 * @param P - passageiro a remover do autocarro
	 * @return true se passageiro foi removido do autocarro
	 */
	public boolean removePassenger(Passenger P) {
		lock.lock();
		try {
			if (passengers.contains(P)) {
				this.passengers.remove(P);
				return true;
			}
		}finally {
			lock.unlock();
		}
		return false;
	}
	
	//função usada pelo logger
	public String toPrint() {
		String s = "";
		switch(passengers.size()) {
			case 0:
				s = " - - - ";
				break;
			case 1:
				s = " "+ this.passengers.get(0).getPId() + " - - ";
				break;
			case 2:
				s = " "+ this.passengers.get(0).getPId() +" "+this.passengers.get(1).getPId()+ " - ";
				break;
			case 3:
				s = " "+ this.passengers.get(0).getPId() + " " + this.passengers.get(1).getPId() +" "+ this.passengers.get(2).getPId();
				break;
			default:
				
		}
		return s;
	}
	
}
