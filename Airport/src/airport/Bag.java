package airport;

public class Bag {
	private int owner;
	private boolean dest;
	private int number;
	public Bag(int owner, boolean dest, int number) {
		this.owner = owner;
		this.dest = dest;
		this.number = number;
	}
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
