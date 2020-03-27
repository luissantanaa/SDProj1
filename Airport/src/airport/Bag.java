package airport;


/**
 * 
 * Classe que implementa as malas levadas pelos passageiros
 *
 */
public class Bag { 
	private int owner;
	private boolean dest;
	private int number;
	//construtor
	/**
	 * 
	 * @param owner - dono da mala
	 * @param dest - destino da mala
	 * @param number - identificador da mala
	 */
	public Bag(int owner, boolean dest, int number) {
		this.owner = owner;
		this.dest = dest;
		this.number = number;
	}
	
	//getters and setters
	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
	}
	public boolean isDest() {
		return dest;
	}
	public void setDest(boolean dest) {
		this.dest = dest;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
}
