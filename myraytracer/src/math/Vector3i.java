package math;

public class Vector3i {
	
	public int x;
	public int y;
	public int z;
	
	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString(){
		return "x: " + x + " y: " + y + " z: " + z + "\n";
	}
}