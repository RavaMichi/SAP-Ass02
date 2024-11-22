package service.domain;

/**
 * Value object. Represents a 2D vector.
 * @param x
 * @param y
 */
public record V2d(double x, double y) implements java.io.Serializable {
    public V2d sum(V2d v){
        return new V2d(x+v.x,y+v.y);
    }

    public V2d rotate(double degree) {
        var rad = degree * Math.PI/180;
        var cs = Math.cos(rad);
        var sn = Math.sin(rad);
        var x1 = x * cs - y * sn;
        var y1 = x * sn + y * cs;
        var v = new V2d(x1, y1).getNormalized();
        return v;
    }

    public double mod(){
        return (double)Math.sqrt(x*x+y*y);
    }

    public V2d getNormalized(){
        double module = mod();
        return new V2d(x/module,y/module);
    }

    public V2d mul(double fact){
        return new V2d(x*fact,y*fact);
    }

    public String toString(){
        return "("+x+","+y+")";
    }

}
