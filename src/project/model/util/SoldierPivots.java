package project.model.util;

public class SoldierPivots {
 
    public Vector2 rootAnchor;
    public Vector2 pivot;
    public Vector2 anchor;
    public int flipOff;
    
    public SoldierPivots(Vector2 pivot, Vector2 anchor, Vector2 rootAnchor, int flipOff){
        this.rootAnchor = rootAnchor;
        this.anchor = anchor;
        this.pivot = pivot;
        this.flipOff = flipOff;
    }

    public SoldierPivots() {
    }
    
}
