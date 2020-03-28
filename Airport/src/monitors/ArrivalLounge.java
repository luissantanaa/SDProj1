package monitors;



import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.ArrivalLoungeInterfaceBDriver;
import Interfaces.ArrivalLoungeInterfacePassenger;
import Interfaces.ArrivalLoungeInterfacePorter;
import airport.Bag;
import airport.Logger;
import airport.Plane;
import states.StatesPerson;

/**
 * 
 * Monitor Arrival Lounge
 *
 */

public class ArrivalLounge implements ArrivalLoungeInterfacePassenger,ArrivalLoungeInterfacePorter, ArrivalLoungeInterfaceBDriver{
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition porterc = lock.newCondition(); 
	private final Condition busDriver = lock.newCondition(); 
	
	
	private boolean lastF;
	private Plane plane;
	private int size;
	private Logger logger;
	private int sizeBus;
	
	private DepartureTermEntr departMonitor; 
	private int departureNpassengers =0;
	
	private ArrivalTermExit arrivalMonitor;
	private int arrivalNpassengers =0;
	
	public Plane getPlane() {
		return plane;
	}
	public void setPlane(Plane plane) {
		this.plane = plane;
		this.logger.setBagSizes(this.getPlane().getBags().size());
		size = 0;
		sizeBus = 0;
		departureNpassengers =0;
		arrivalNpassengers =0;
	}
	
	//construtor
	/**
	 * 
	 * @param logger
	 * @param departMonitor - Departure Terminal Entrance Monitor
	 * @param arrivalMonitor - Arrival Lounge Monitor
	 */
	public ArrivalLounge(Logger logger, DepartureTermEntr departMonitor, ArrivalTermExit arrivalMonitor){
		size = 0;
		sizeBus = 0;
		lastF = false;
		this.logger = logger;
		this.departMonitor = departMonitor;
		this.arrivalMonitor=arrivalMonitor;
	}
	
	
	//sinaliza o final do dia de trabalho para bagageiro e condutor autocarro
	/**
	 * sinaliza o final do dia de trabalho para bagageiro e condutor autocarro
	 */
	public void lastFlight() {
		lock.lock();
		try {
			lastF = true;
		}finally {
			porterc.signal();
			busDriver.signal();
			lock.unlock();
		}
	
	}
	
	//retorna variavel booleana que sinaliza final do dia
	/**
	 *@return variavel booleana que sinaliza final do dia
	 */
	public boolean itWasLast() {
		return lastF;
	}
	
	
	/**
	 * Função usada pelos passageiros para decidirem o que fazer, quando saiem do avião
	 */
	public StatesPerson whatShouldIDo(List<Bag> bag, boolean dest) {
		
		lock.lock();
		try {
			if(dest) { //se o passageiro estiver no destino, ou vai buscar malas ou vai para casa
				arrivalNpassengers++;
				logger.incPassDest();
				if(bag.isEmpty()) { //se nao tiver malas, vai para casa
					return StatesPerson.EXITING_THE_ARRIVAL_TERMINAL;
					
				}
				return StatesPerson.AT_THE_LUGGAGE_COLLECTION_POINT;
				
			}else { //se nao estiver no destino, prepara se para o proximo voo
				logger.incPassTransit();
				departureNpassengers++;
				return StatesPerson.AT_THE_ARRIVAL_TRANSFER_TERMINAL;
			}
			
		}finally {			
			size++;
			if(!dest) { //conta nr de passageiros que nao estao no destino
				sizeBus ++;
			}
			
			if(size==6) { //se todos os passageiros ja souberem o que fazer, acorda condutor e bagageiro
				porterc.signal();
				busDriver.signal();
				setDepartureNPassengers();
				setArrivalNPassengers();
			}else if(sizeBus == 3) { //se houver passageiros suficientes para encher autocarro, sinaliza o condutor do autocarro
					busDriver.signalAll();
			}
			lock.unlock();
		}	
	}
	
	/**
	 * passa o nr de passageiros ao monitor
	 */
	public void setDepartureNPassengers() { //passa o nr de passageiros ao monitor
		departMonitor.setNPassengers(departureNpassengers);
	}

	/**
	 * passa o nr de passageiros ao monitor
	 */
	public void setArrivalNPassengers() { //passa o nr de passageiros ao monitor
		arrivalMonitor.setNPassengers(arrivalNpassengers);
	}
	
	/**
	 * Função usada pelo bagageiro, enquanto nao ha malas para recolher
	 */
	public boolean takeARest() {
		lock.lock();
		try {	
			while( (size!=6 && !lastF) || (plane.getBags().size()==0 && !lastF)) { //se o nr de passgeiros que ja sabe o que fazer
																					//for diferente do nrº de passageiros do aviao e nao for o ultimo voo
				porterc.await();													//OU a lista de malas do aviao estiver a zero e nao for o ultimo voo
			}																		// o bagageiro fica em espera
			if(plane.getBags().size()>0) {	//se houver malas para recolher retorna true
				return true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}	
		return false;
	}
	
	
	//função de recolha de malas
	/**
	 * função usada pelo bagageiro para recolher malas
	 */
	public Bag collectBag() {
			lock.lock();
			try {
				if(this.plane.getBags().size() > 0) {				
					return plane.getBag();
				}else {
					return null;
				}
			}finally {
				lock.unlock();
			}
	}
	
	/**
	 * função usada pelo condutor do autocarro para quando pode recolher passageiros
	 */
	public int goCollectPassengers() {
		lock.lock();
		try {
			while((sizeBus<3 && size<6  && !lastF)  || (sizeBus == 0  && !lastF) ) { //se o nrº de passageiros do autocarro for igual ao valor maximo
				busDriver.await();													// e o nr de passageiros encaminhados for igual ao nr de passageiros do aviao
			}																		// e nao  for o ultimo voo
																					//OU nao houver pessoas em espera para o autocarro e nao for o ultimo voo
																					//o condutor fica em espera
			if(sizeBus>=3) {	//se o nr de pessoas em espera para o autocarro for igual ou maior ao tamanho do autocarro
				sizeBus-=3;		//retira o tamanho do autocarro a fila de espera
				return 3;
			}else if(sizeBus!=0){
				int sizeb = sizeBus;
				sizeBus=0;
				return sizeb;
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return -1;
	}

}