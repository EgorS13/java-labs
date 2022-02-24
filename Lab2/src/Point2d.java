public class Point2d {

    public static void main(String[] args) {
    }

    private double xCoord;
    private double yCoord;
    public Point2d(double x, double y){
        xCoord = x;
        yCoord = y;
    }
    public Point2d(){
        //Вызов конструктора с двумя параметрами и определение источника
        this(0,0);
    }
    public double getX(){
        return xCoord;
    }
    public double getY(){
        return yCoord;
    }
    public void setX(double val){
        xCoord = val;
    }
    public void setY(double val){
        yCoord = val;
    }
}
