import java.awt.geom.Rectangle2D;

/**
 * Класс для расчёта фрактала Мандельброта
 */
public class Mandelbrot extends FractalGenerator
{

    public static final int MAX_ITERATIONS = 2000;

    /**
     * Установка начальных значений для формулы расчёта
     */
    public void getInitialRange(Rectangle2D.Double range)
    {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    /**
     * Метод, рассчитывающий количество итераций для пикселей
     * Метод учитывает то, что модуль комплексного числа должен не превышать 2
     */
    public int numIterations(double x, double y)
    {
        int iteration = 0;
        ComplexNums cxnum = new ComplexNums(x, y);
        while (iteration < MAX_ITERATIONS && ((cxnum.Zreal * cxnum.Zreal) + (cxnum.Zimaginary * cxnum.Zimaginary)) < 4)
        {
            cxnum.Iteration();
            iteration++;
        }
        // Если достигнут максимум итераций (2000), возвращает -1
        if (iteration == MAX_ITERATIONS){ return -1; }
        else { return iteration; }
    }
}