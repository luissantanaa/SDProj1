package monitors;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.ArrivalTransferTermBDriverInterface;
import Interfaces.ArrivalTransferTermPassengerInterface;
import airport.Bus;
import airport.Logger;
import airport.Passenger;

/**
 * 
 * Monitor Arrival Transfer Terminal
 *
 */

public class ArrivalTransferTerm implements ArrivalTransferTermPassengerInterface, ArrivalTransferTermBDriverInterface{
	
	
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition busFull = lock.newCondition(); //condição autocarro cheio
	private final Condition passengerWait = lock.newCondition(); //condição para espera de passageiros
	
	private Bus bus;
	private Logger logger;
	private boolean dayWorkEnd;
	private Queue<Passenger> passengersWaiting;
	
	private boolean lastPass = false; //boolean simboliza ultimo passageiro do voo
	private boolean driverLeft = false; //boolean simboliza se o condutor do autocarro esta em viagem
	private boolean canEnter = false; //boolean simboliza se os passageiros podem entrar no autocarro
	private int passNumber = 0; //nrº de passageiros que vão entrar no autocarro
	
	
	//construtor
	/**
	 * 
	 * @param bus autocarro usado na simulação
	 * @param logger
	 */
	public ArrivalTransferTerm(Bus bus, Logger logger) {
		this.bus = bus;
		this.logger = logger;
		this.dayWorkEnd = false;
		this.passengersWaiting = new LinkedList<Passenger>();
	}
	
	
	
	/**
	 * função usada pelos passageiros para entrar no autcarro
	 */
	public boolean enterTheBus(Passenger p) { //entrada de passageiros para o autocarro
		
		lock.lock();
		try {	

				if(!passengersWaiting.contains(p)) { //se nao esta na fila de espera para entrar no autocarro, entra na fila
					passengersWaiting.add(p);
					logger.addPassengersWaiting(p);
					logger.toPrint();
				}

			
			while(!bus.hasSpace() || driverLeft || !canEnter) { //se o autocarro não tem espaço ou não pode entrar, ou o condutor esta em viagem, a thread fica em espera
				if(this.passNumber!=0 && passengersWaiting.size() >= this.passNumber) { //se o nrº de passageiros da viagem for diferente de 0
																						//e o tamanho da fila de espera for igual ao nrº de passageiros da viagem
					canEnter=true;														//entao podem entrar
					break;
				}else {
					passengerWait.await();
				}
				
			}
		
			
			if(canEnter) {
				
				passengerWait.signalAll();	//se podem entrar acorda as threads
				if(passengersWaiting.peek() == p) { //se o passageiro em questão for o passageiro no primeiro lugar da fila
					
					if(bus.addPassenger(passengersWaiting.poll())) { //então é adicionado
						
						logger.removePassengersWaiting(p);
						if(!bus.hasSpace()) { //se o autocarro estiver cheio, passageiros nao podem entrar
							canEnter = false;
							this.passNumber=0; //nrº de passageiros da viagem passa a 0
							busFull.signal(); //e o condutor é sinalizado
							
						}else if(passengersWaiting.size()== 0) { //se nao estiver ninguem na fila de espera
							this.passNumber=0; //nrº de passageiros da viagem passa a 0
							canEnter = false; //ninguem pode entrar
							lastPass = true; //ultimo passageiro passa a true simbolizando que a viagem anterior foi a ultima
							busFull.signal(); //condutor é acordado para terminar o seu ciclo de vida
							
						}
						
						return true;
					}
				}
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return false;
			
	}
	
	
	//funções auxiliares
	public void LastPassenger() {
		lock.lock();					//VER
		try {	
			lastPass = true;
		}finally {
			busFull.signal(); //ultimo passageiro acorda o condutor para terminar o seu lifecycle
			lock.unlock();
		}
	}
	
	
	public void dayWorkEnd() {
		lock.lock();
		try {
			this.dayWorkEnd = true;
			busFull.signal(); //ultimo passageiro acorda o condutor para terminar o seu lifecycle
		}finally {
			lock.unlock();
		}
	}
	/**
	 * função usada para verificar se o autocarro esta cheio ou nao
	 */
	public boolean BusNotFull() {
		
		lock.lock();
		try {
			while(bus.hasSpace() && !this.dayWorkEnd && !lastPass ) { //enquanto o autocarro nao estiver cheio
																	//e o dia ainda nao tiver acabado e ainda nao tiver entrado o ultimo passageiro	
				busFull.await();									//a thread fica em wait
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

	
	/**
	 * retorna a variavel que simboliza o final do dia de trabalho
	 */
	public boolean hasDaysWorkEnded() {
		return this.dayWorkEnd;	
	}
	
	
	//anuncia que o autocarro esta a receber pessoas
	/**
	 * anuncia que o autocarro esta a recolher pessoas
	 */
	public void announcingBusBoarding(int passNumber) {
		lock.lock();
		try {
			
			this.passNumber = passNumber;
			lastPass = false;
			driverLeft = false;
			
		}finally {
			passengerWait.signalAll();
			lock.unlock();
		}
	}
}
