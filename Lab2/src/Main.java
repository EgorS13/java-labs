import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Point3d p1 = new Point3d(0, 0, 0);
        Point3d p2 = new Point3d(0, 0, 0);
        Point3d p3 = new Point3d(0, 0, 0);
        System.out.print("Введите координаты первой точки: ");
        p1.changeCo(in);
        System.out.print("Введите координаты второй точки: ");
        p2.changeCo(in);
        System.out.print("Введите координаты третьей точки: ");
        p3.changeCo(in);
        System.out.print("Координаты точки 1: ");
        System.out.println(p1.outPo());
        System.out.print("Координаты точки 2: ");
        System.out.println(p2.outPo());
        System.out.print("Координаты точки 3: ");
        System.out.println(p3.outPo());
        double d1 = Point3d.distanceTo(p1, p2);
        double d2 = Point3d.distanceTo(p2, p3);
        double d3 = Point3d.distanceTo(p1, p3);
        System.out.printf("Расстояние от точки 1 до точки 2: %.2f\n", d1);
        System.out.printf("Расстояние от точки 2 до точки 3: %.2f\n", d3);
        System.out.printf("Расстояние от точки 1 до точки 3: %.2f\n", d2);
        double area = computeArea(d1, d2, d3);
        if ((p1.eqv(p2) || p2.eqv(p3) || p1.eqv(p3)) || (area == 0.00) ) {
            while (p1.eqv(p2) || p2.eqv(p3) || p1.eqv(p3)) {
                System.out.println("Ошибка! По заданным координатам невозможно составить треугольник. ");
                System.out.println("Если хотите поменять значения координат точек, введите true, иначе введите false и программа завершится ");
                boolean yor = in.nextBoolean();
                if (yor) {
                    int whichpoint = 0;
                    System.out.println("Координаты какой точки вы хотите поменять? (Введите 1, 2 или 3) ");
                    whichpoint = in.nextInt();
                    if (whichpoint == 1) {
                        System.out.println("Введите новые значения координат точки: ");
                        p1.changeCo(in);
                        System.out.println(p1.outPo());
                        d1 = Point3d.distanceTo(p1, p2);
                        d3 = Point3d.distanceTo(p1, p3);
                        yor = false;
                    } else if (whichpoint == 2) {
                        System.out.println("Введите новые значения координат точки: ");
                        p2.changeCo(in);
                        System.out.println(p2.outPo());
                        d1 = Point3d.distanceTo(p1, p2);
                        d2 = Point3d.distanceTo(p2, p3);
                        yor = false;
                    } else if (whichpoint == 3) {
                        System.out.println("Введите новые значения координат точки: ");
                        p3.changeCo(in);
                        System.out.println(p3.outPo());
                        d2 = Point3d.distanceTo(p2, p3);
                        d3 = Point3d.distanceTo(p1, p3);
                        yor = false;
                    }
                } else return;
            }
        }
        System.out.printf("Площадь %.2f", computeArea(d1, d2, d3));
        in.close();
    }
    public static double computeArea(double a, double b, double c){
        double p = (a + b + c) / 2;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }
}
