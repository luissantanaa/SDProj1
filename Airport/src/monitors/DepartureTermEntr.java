package monitors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.DepartureTermEntrancePassengerInterface;
import airport.Passenger;

public class DepartureTermEntr implements DepartureTermEntrancePassengerInterface {
	private int nPassengers=0;
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition plock = lock.newCondition(); 
	private List<Passenger> pList;
	
	
	//construtor
	public DepartureTermEntr() {
			nPassengers = 0;
			pList = new ArrayList<>();
	}
	
	public void setNPassengers(int n) {
		lock.lock();
		try {
			
			nPassengers = n; //nrº de passageiros que vao ficar a espera para ir para o proximo voo
	
		}finally {
			plock.signalAll();
			lock.unlock();
		}
	}
	
	
	public boolean prepareNextLeg(Passenger P) {
		lock.lock();
		try {
			if(!pList.contains(P)) { //se nao estiver na lista de espera de passageiros que vao para o proximo voo, adiciona
				pList.add(P);
			}
			
			while(nPassengers==0 || nPassengers!=pList.size()) { //enquanto o nr de passageiros em espera for 0 
																//OU enquanto o tamanho da fila for diferente do nr de passageiros que vao ter que ficar em espera
				plock.await();									//fica em espera
			}
		
			nPassengers--; //retira passageiro da lista e do nr de passageiros total em espera
			pList.remove(P);
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			plock.signalAll();
			lock.unlock();
		}
		return false;
	}
}
