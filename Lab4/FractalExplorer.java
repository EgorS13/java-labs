import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;


/**
 * Класс, необходимый для отображения графического интерфейса
 * и обработки действий пользователя
 */
public class FractalExplorer
{
    /** Ширина и высота отображения - целочисленная, поэтому int **/
    private int displaySize;

    /** JImageDisplay для обновления отображения изображения **/
    private JImageDisplay display;

    /** Объект для разных типов фракталов (задел на будущее в том числе) **/
    private FractalGenerator fractal;

    /**
     * Объект, опредяляющий размер текущего диапазона просмотра
     * (То, что показывается в настоящий момент)
     */
    private Rectangle2D.Double range;

    /**
     * конструктор, принимающий размер дисплея и сохраняющий его,
     * после чего инициализирующий объекты диапазона и генератора фрактала
     */
    public FractalExplorer(int size) {
        displaySize = size;
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);

    }

    /**
     * Этот метод инициализирует графический интерфейс Swing и кнопку для сброса
     */
    public void createAndShowGUI()
    {
        display.setLayout(new BorderLayout());
        JFrame Frame = new JFrame("Fractal Explorer");
        // Добавляет и центрует объект изображения
        Frame.add(display, BorderLayout.CENTER);

        /** Создание объекта кнопки сброса **/
        JButton resetButton = new JButton("Reset");
        Frame.add(resetButton, BorderLayout.SOUTH);
        ButtonHandler resetHandler = new ButtonHandler();
        // Обработка события нажатия на кнопку
        resetButton.addActionListener(resetHandler);
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        JPanel myPanel = new JPanel();
        Frame.add(myPanel, BorderLayout.NORTH);

        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(resetButton);
        Frame.add(myBottomPanel, BorderLayout.SOUTH);

        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Frame.pack();
        Frame.setVisible(true);
        Frame.setResizable(false);

    }

    /**
     * Вспомогательный метод для отображения фрактала.
     * Он проходится по пикселям на дисплее и вычисляет количество итераций для координат во фрактале
     * Если кол-во итераций = -1, он устанавливает чёрный цвет пикселя,
     * иначе же выбирает значение в зависимости от количества итераций.
     * Когда всё готово - обновляет дисплей
     */
    private void drawFractal()
    {
        for (int x=0; x<displaySize; x++){
            for (int y=0; y<displaySize; y++){
                double xCoord = fractal.getCoord(range.x,
                        range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y,
                        range.y + range.height, displaySize, y);
                int i = fractal.numIterations(xCoord, yCoord);
                if (i == -1){
                    display.drawPixel(x, y, 0);
                }
                else{
                    float hue = 0.7f + (float) i / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    display.drawPixel(x, y, rgbColor);
                }
            }
        }
        display.repaint();
    }
    /**
     * Внутренний класс для обработки событий ActionListener
     */
    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            if (command.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
        }
    }

    /**
     * Внутренний класс для обработки событий MouseAdapter
     */
    private class MouseHandler extends MouseAdapter
    {
        /**
         * Когда происходит нажатие мышкой, перемещается на указанные щелчком координаты.
         * Приближение вполовину от нынешнего.
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            // Принимает x координату нажатия
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
            // Принимает y координату нажатия
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            // Перерисовывает фрактал после приближения
            drawFractal();
        }
    }

    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}