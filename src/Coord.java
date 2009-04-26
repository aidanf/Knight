import waba.fx.*;
import waba.sys.*;
import waba.util.*;

public class Coord{

    private int _x;
    private int _y;
    private int _z;

    public Coord(){}

    public Coord(int x, int y){
	_x=x;
	_y=y;
    }

    public Coord(int x, int y, int z){
	_x=x;
	_y=y;
	_z=z;
    }

    public int x(){
	return _x;
    }
    public int y(){
	return _y;
    }
    public int z(){
	return _z;
    }
}
