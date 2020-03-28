package monitors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.ArrivalTermExitPassengerInterface;
import airport.Passenger;

/**
 * 
 * Monitor Arrival Terminal Exit
 *
 */
public class ArrivalTermExit  implements ArrivalTermExitPassengerInterface{
	private int nPassengers=0;
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition plock = lock.newCondition(); //condição para fazer com que os passageiros esperem pelo ultimo para ir embora
	private List<Passenger> pList;
	
	//construtor
	/**
	 * pList é a lista de passageiros a espera do ultimo para ir embora
	 */
	public ArrivalTermExit() {
		nPassengers = 0;
		pList = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param n nrº de passageiros a ir embora
	 */
	public void setNPassengers(int n) {
		lock.lock();
		try {
			nPassengers = n;	//nrº de passageiros a ir embora
		}finally {
			plock.signalAll();
			lock.unlock();
		}
	}
	
	/**
	 * função usada pelos passageiros para ir embora
	 */
	public boolean GoHome(Passenger P) {
		lock.lock();
		try {
			if(!pList.contains(P)) { //lista de passageiros a espera para ir embora
				pList.add(P);
			}
			
			while(nPassengers==0 || nPassengers!=pList.size()) { //enquanto nao houver passageiros para ir embora ou o tamanho da lista for diferente
				plock.await();									//do nrº de passageiros a ir embora fica a espera
			}

			nPassengers--; //quando um sai é decrementado
			pList.remove(P); //removido da lista
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
