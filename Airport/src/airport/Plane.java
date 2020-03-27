package airport;

import java.util.Queue;


/**
 * 
 * classe que implementa os aviões usados durante as simulações
 *
 */
public class Plane {
	private Queue<Passenger> passengers;
	private int planeId;
	private Queue<Bag> bags;
	
	//construtor
	/**
	 * 
	 * @param passengers - fila de passageiros
	 * @param planeId - identificador do avião
	 * @param bags - fila das malas dos passageiros
	 */
	public Plane(Queue<Passenger> passengers, int planeId, Queue<Bag> bags) {
		super();
		this.passengers = passengers;
		this.planeId = planeId;
		this.bags = bags;
	}
	

	//getters e setters
	public Queue<Passenger> getPassengers() {
		return passengers;
	}
	public void setPassengers(Queue<Passenger> passengers) {
		this.passengers = passengers;
	}
	
	public Passenger removePassenger() {
		return this.passengers.remove();
	}
	public boolean isEmpty(){
		return this.passengers.size()==0;
	}
	
	public int getPlaneId() {
		return planeId;
	}
	public void setPlaneId(int planeId) {
		this.planeId = planeId;
	}
	public Queue<Bag> getBags() {
		return bags;
	}
	public Bag getBag(){
		return bags.remove();
	}
	public void setBags(Queue<Bag> bags) {
		this.bags = bags;
	}
}
