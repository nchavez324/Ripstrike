package project.model.util;

public class Vector2 implements java.io.Serializable{

    private static final long serialVersionUID = -2352130544823865397L;
    
    public float X, Y;//The two dimensions.
    
    public Vector2(){
        X = 0f;
        Y = 0f;
    }
    
    //The only constructor, takes int both dimensions.
    public Vector2(float x, float y){
        X = x;
        Y = y;
    }
    
    public Vector2 copy(){
        return new Vector2(X,Y);
    }
    
    @Override
	public String toString(){
            return "X: " + X + ", Y: " + Y;
    }
}
