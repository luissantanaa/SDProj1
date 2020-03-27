package airport;


import Interfaces.ArrivalLoungeInterfacePorter;
import Interfaces.BaggageCollectPointPorterInterface;
import Interfaces.TempStorageInterfacePorter;
import states.StatesPorter;


/**
 * classe que simula o bagageiro 
 * 
 **/
public class Porter extends Thread{
	private ArrivalLoungeInterfacePorter arrivalmonitor; //monitores usados pelo bagageiro
	private BaggageCollectPointPorterInterface baggageCollectPointMonitor;
	private TempStorageInterfacePorter tempstoragemonitor;	
	private Bag b; //mala que foi buscar
	private StatesPorter state;
	private Logger logger ;
	volatile boolean end = false; //boolean volatil usado para terminar a thread

	//construtor
	/**
	 * 
	 * @param logger - usado para log durante a simulação
	 * @param arrivalmonitor - Monitor Arrival Lounge
	 * @param baggageCollectPointMonitor - Monitor Baggage Collection Point
	 * @param tempstoragemonitor - Monitor Temporary Storage
	 */
	public Porter( Logger logger , ArrivalLoungeInterfacePorter arrivalmonitor, BaggageCollectPointPorterInterface baggageCollectPointMonitor, TempStorageInterfacePorter tempstoragemonitor) {
		super();
		this.arrivalmonitor = arrivalmonitor;
		this.baggageCollectPointMonitor = baggageCollectPointMonitor;
		this.tempstoragemonitor = tempstoragemonitor;
		this.state = StatesPorter.WAITING_FOR_A_PLANE_TO_LAND;
		this.logger = logger;
		
	}
	/**
	 * 
	 * Getters e setters da classe
	 */
	//getters and setters
	public Bag getB() {
		return b;
	}
	public void setB(Bag b) {
		this.b = b;
	}
	public StatesPorter getStates() {
		return state;
	}
	
	/**
	 * 
	 * Função usado para passar os estados para a sua respetiva sigla
	 */
	//função usada pelo Logger para passar o estado para a respetiva sigla
	public String getString() {
		String s="";
		switch(this.state) {
		case WAITING_FOR_A_PLANE_TO_LAND:
			s = s + "WPTL";
			break;
		case AT_THE_PLANES_HOLD:
			s = s + "APLH";
			break;
		case AT_THE_LUGGAGE_BELT_CONVEYOR:
			s = s + "ALCB";
			break;
		case AT_THE_STOREROOM:
			s = s + "ALCR";
			break;
		default:
				
		}
		return s;

	}
	
	//funções auxiliares
	public boolean isStateFinal() { //função que retorna se o estado correspondente é final ou não
		return end;
	}
	public void endThread(){
        end = true;	//função que sinaliza a morte da thread

    }
	
	/**
	 * Função run simula o lifecycle da thread
	 */
	public void run() {	//lifecycle da thread
		while(!isStateFinal()){
			switch(this.state) {
				case WAITING_FOR_A_PLANE_TO_LAND:
					if(arrivalmonitor.itWasLast()) { //se for o ultimo voo, mata a thread
						endThread();
					}
					if(arrivalmonitor.takeARest()){
						this.tryToCollectABag();
						logger.toPrint();
					}
					break;
				case AT_THE_PLANES_HOLD:
					logger.toPrint();
					this.b = this.arrivalmonitor.collectBag();
					if(this.b == null) { //se for null, nao ha mais malas para recolher e muda o estado
						this.baggageCollectPointMonitor.noMoreBags();
						this.noMoreBagsToCollect();
					}else {
						if(this.b.isDest()) { //verifica o destino da mala e encaminha a mesma para o seu respetivo sitio
							this.carryItToAppropriateStore(StatesPorter.AT_THE_LUGGAGE_BELT_CONVEYOR);
							
							if(this.baggageCollectPointMonitor.addBag(this.b)) {
								logger.incrementCB(); //função do logger que incrementa o nr de malas recolhidas
								logger.toPrint();
							}
							
						}else {
							this.carryItToAppropriateStore(StatesPorter.AT_THE_STOREROOM);
							logger.incrementCR();
							logger.toPrint();
							this.tempstoragemonitor.addBag(this.b);
							
						}
						
					}
				
					break;
				case AT_THE_LUGGAGE_BELT_CONVEYOR:
					this.tryToCollectABag(); //função de mudança de estado
					break;
				case AT_THE_STOREROOM:
					this.tryToCollectABag(); //função de mudança de estado
					break;
				default:
			}
		}
		
	}
	
	/**
	 * função transição de estado
	 */
	public void tryToCollectABag() { //função de mudança de estado
		this.state = StatesPorter.AT_THE_PLANES_HOLD;
	}
	
	/**
	 * função transição de estado
	 */
	public void carryItToAppropriateStore(StatesPorter state) { //função de mudança de estado
		this.state = state;
	}

	/**
	 * função transição de estado
	 */
	public void noMoreBagsToCollect() { //função de mudança de estado
		this.state = StatesPorter.WAITING_FOR_A_PLANE_TO_LAND;
		logger.toPrint();
	}
	
}
