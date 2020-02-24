package airport;

public class Bag {
	int owner;
	boolean dest;
	public Bag(int owner, boolean dest) {
		this.owner = owner;
		this.dest = dest;
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
}
