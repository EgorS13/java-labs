import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
//import javax.swing.JFileChooser.*;
import javax.swing.filechooser.*;
//import javax.imageio.ImageIO.*;
import java.awt.image.*;

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
     * Объект, определяющий размер текущего диапазона просмотра
     * (То, что показывается в настоящий момент)
     */
    private Rectangle2D.Double range;
    /**
     * Переменная, хранящая количество строк, что остались до полной отрисовки фрактала
     */
    private int rowsRemaining;
    /**
     * Поле с объектом кнопки сброса.
     * Объявлено здесь для доступа к ней в разных методах класса.
     */
    private JButton resetButton;
    /**
     * Поле с объектом кнопки сохранения.
     * Объявлено здесь для доступа к ней в разных методах класса.
     */
    private JButton saveButton;
    /**
     * Поле с объектом ComboBox (выпадающий список).
     * Объявлено здесь для доступа к ней в разных методах класса.
     */
    private JComboBox myComboBox;

    /**
     * Конструктор, принимающий размер дисплея и сохраняющий его,
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

        /** Кнопка сброса **/
        resetButton = new JButton("Reset");
        Frame.add(resetButton, BorderLayout.SOUTH);
        ButtonHandler resetHandler = new ButtonHandler();
        // Обработка события нажатия на кнопку
        resetButton.addActionListener(resetHandler);
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        /** Создание объекта combo-box **/
        myComboBox = new JComboBox();

        /** Добавляем элементы в combo-box **/
        FractalGenerator mandelbrotFractal = new Mandelbrot();
        myComboBox.addItem(mandelbrotFractal);
        FractalGenerator tricornFractal = new Tricorn();
        myComboBox.addItem(tricornFractal);
        FractalGenerator burningShipFractal = new BurningShip();
        myComboBox.addItem(burningShipFractal);

        /** Обрабатывать нажатия будет ButtonHandler **/
        ButtonHandler fractalChooser = new ButtonHandler();
        myComboBox.addActionListener(fractalChooser);

        /**
         * Создаём панель и добавляем на неё combo-box.
         * Добавляем также текст пояснения "Fractal:"
         * Наконец, прописываем расположение этой панели наверху
         */
        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        Frame.add(myPanel, BorderLayout.NORTH);

        /**
         * Создаём кнопку сохранения и добавляем её на созданную панель внизу
         */
        saveButton = new JButton("Save");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        Frame.add(myBottomPanel, BorderLayout.SOUTH);

        /** Обрабатывать события будет ButtonHandler **/
        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);

        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Frame.pack();
        Frame.setVisible(true);
        Frame.setResizable(false);

    }
    /**
     * Вспомогательный метод для отображения фрактала.
     * Этот метод проходит по каждой строке изображения и вызывает работу FractalWorker для каждой строки.
     * Он же проходится по пикселям на дисплее и вычисляет количество итераций для координат во фрактале
     * Если кол-во итераций = -1, он устанавливает чёрный цвет пикселя,
     * иначе же выбирает значение в зависимости от количества итераций.
     */
    private void drawFractal(){
        enableUI(false);
        rowsRemaining = displaySize;
        for (int x = 0; x < displaySize; x++){
            FractalWorker drawRow = new FractalWorker(x);
            drawRow.execute();
        }
    }
    /**
     * Метод выключения интерфейса во избежание ошибок параллельного доступа
     */
    public void enableUI(boolean b) {
        saveButton.setEnabled(b);
        resetButton.setEnabled(b);
        myComboBox.setEnabled(b);
    }
    /**
     * Внутренний класс для обработки событий ActionListener
     */
    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            /**
             * Если нажатие на combo-box, берётся выбранный фрактал и выводится на дисплей
             */
            if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                fractal = (FractalGenerator) mySource.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();
            }
            /**
             * Если нажатие на кнопку сброса, сбрасывает приближение
             */
            else if (command.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
            /**
             * Если нажатие на кнопку сохранения, сохраняет текущее отображение фрактала
             */
            else if (command.equals("Save")) {

                JFileChooser myFileChooser = new JFileChooser();

                /** Сохраняет только PNG-изображения **/
                FileFilter extensionFilter = new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);
                /**
                 * Удостоверяется, что filechooser не разрешит что-то, помимо *.png
                 */
                myFileChooser.setAcceptAllFileFilterUsed(false);

                /**
                 * Открывает окно с возможностью выбора директории сохранения
                 */
                int userSelection = myFileChooser.showSaveDialog(display);

                /**
                 * Если пользователь решает таки сохранить файл, операция сохранения продолжается
                 */
                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    /** Получает файл и имя файла **/
                    java.io.File file = myFileChooser.getSelectedFile();
                    String file_name = file.toString();

                    /** Пытается сохранить изображение на диск **/
                    try {
                        BufferedImage showImage = display.getImage();
                        javax.imageio.ImageIO.write(showImage, "png", file);
                    }
                    /**
                     * Ловит все исключения и пишет сообщение об исключении
                     */
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(display, exception.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
                /**
                 * Если пользователь передумал сохранять файл, return
                 */
                else return;
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
            if (rowsRemaining == 0) {
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
    }

    /**
     * Внутренний класс-наследник от SwingWorker,
     * используется для реализации многопоточных вычислений.
     */
    private class FractalWorker extends SwingWorker<Object, Object> {
        private int yCoord;
        private int[] rgb;

        public FractalWorker(int y) {
            this.yCoord = y;
        }
        /**
         * Переопределение метода из SwingWorker,
         * используется для вычисления значения фракталов для пикселей строк.
         * Разница с прошлыми лабораторными в том, что теперь данные действия будут происходить в отдельном потоке.
         */
        @Override
        public Object doInBackground() throws Exception {
            rgb = new int[displaySize];
            for (int i = 0; i < displaySize; i++){
                int num = fractal.numIterations(FractalGenerator.getCoord (range.x, range.x + range.width, displaySize, i), FractalGenerator.getCoord (range.y, range.y + range.height, displaySize, yCoord));
                if (num == -1) {
                    rgb[i] = 0;
                } else {
                    float hue = 0.7f + (float) num / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    rgb[i] = rgbColor;
                }
                display.drawPixel(i, yCoord, rgb[i]);
            }
            return null;
        }
        /**
         * Переопределение метода из SwingWorker.
         * Добавляет отрисованные строки и уменьшает значение переменной rowsRemaining
         */
        @Override
        protected void done() {
            display.repaint(0, 0, yCoord, displaySize, 1);
            rowsRemaining--;
            if (rowsRemaining == 0)
                enableUI(true);
        }

    }
    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}