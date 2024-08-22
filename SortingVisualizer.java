import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class SortingVisualizer extends JPanel {

    private static final long serialVersionUID = 1L;
    private final int WIDTH = 1000, HEIGHT = WIDTH * 9 / 16;
    private final int SIZE = 500;
    private final int BAR_WIDTH = WIDTH / SIZE;
    private float[] bar_height = new float[SIZE];
    private SwingWorker<Void, Void> shuffler, sorter;
    private int current_index, traversing_index;

    public SortingVisualizer() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initBarHeight();
        initSorter();
        initShuffler();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.CYAN);

        for (int i = 0; i < SIZE; i++) {
            Rectangle2D.Float bar = new Rectangle2D.Float(i * BAR_WIDTH, HEIGHT - bar_height[i], BAR_WIDTH, bar_height[i]);
            g2d.fill(bar);
        }
    }

    private void initSorter() {
        sorter = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                for (current_index = 1; current_index < SIZE; current_index++) {
                    traversing_index = current_index;
                    while (traversing_index > 0 && bar_height[traversing_index] < bar_height[traversing_index - 1]) {
                        swap(traversing_index, traversing_index - 1);
                        traversing_index--;

                        Thread.sleep(10);
                        repaint();
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                // Any cleanup or final actions after sorting is complete
            }
        };
    }

    private void initShuffler() {
        shuffler = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                int middle = SIZE / 2;

                for (int i = 0, j = middle; j < SIZE; i++, j++) {
                    int random_index = new Random().nextInt(SIZE);
                    swap(i, random_index);

                    random_index = new Random().nextInt(SIZE);
                    swap(j, random_index);

                    Thread.sleep(10);
                    repaint();
                }

                return null;
            }

            @Override
            protected void done() {
                sorter.execute();
            }
        };
        shuffler.execute();
    }

    private void initBarHeight() {
        float interval = (float) HEIGHT / SIZE;
        for (int i = 0; i < SIZE; i++) {
            bar_height[i] = i * interval;
        }
    }

    private void swap(int indexA, int indexB) {
        float temp = bar_height[indexA];
        bar_height[indexA] = bar_height[indexB];
        bar_height[indexB] = temp;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sorting Visualizer");
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new SortingVisualizer());
            frame.validate();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}