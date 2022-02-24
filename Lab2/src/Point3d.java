import java.util.Scanner;

public class Point3d extends Point2d{

    public static void main(String[] args) {

    }

    private double xCoord;
    private double yCoord;
    private double zCoord;
    public Point3d(double x, double y, double z){
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }
    public double getZ(){
        return zCoord;
    }
    public void setZ(double val){
        zCoord = val;
    }
    public static double distanceTo(Point3d p1, Point3d p2) {
        return Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2) + (Math.pow((p1.getZ() - p2.getZ()), 2)));
    }
    public boolean eqv(Point3d p){
    if ((p.xCoord == this.xCoord) && (p.yCoord == this.yCoord) && (p.zCoord == this.zCoord))
        return true;
    else
        return false;
    }
    public String outPo() {
        return ("{" + "x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + "}");
    }
    public Point3d changeCo(Scanner in) {
        double val1 = in.nextDouble();
        double val2 = in.nextDouble();
        double val3 = in.nextDouble();
        this.setX(val1);
        this.setY(val2);
        this.setZ(val3);
        return this;
    }

}

