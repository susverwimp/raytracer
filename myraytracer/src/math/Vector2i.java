package math;

public class Vector2i {
	
	public int x;
	public int y;
	
	public Vector2i(){
		this(0, 0);
	}
	
	public Vector2i(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString(){
		return "x: " + x + " y: " + y + "\n";
	}

}
