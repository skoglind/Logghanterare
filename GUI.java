import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * <h1>GUI</h1>
 * GUI class, to show the GUI form
 *
 * @author Fredrik Skoglind (group 11/root)
 * @version 1.0 MVP
 * @since 2018-05-03
 */
public class GUI extends JFrame {
    private L_Run parser;
    private BufferedImage bi;
    private String selectedFile;
    private int slideConvert;

    private JPanel GUIPane;
    private JButton btnSelectFile;
    private JPanel paneImage;
    private JSlider slideTime;
    private JLabel lblSelectedFile;
    private JLabel viewImage;
    private JLabel lblShowValue;
    private int lastSliderValue;

    private File setDirectory;

    public GUI() {
        this.lastSliderValue = 0;
        this.slideConvert = 1000; // Milliseconds to seconds
        this.setDirectory = null;
        this.selectedFile = null;

        this.setContentPane(GUIPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(800, 500);
        this.setMinimumSize(new Dimension(800, 500));
        this.setTitle("Masskadescenario Loggfilsanalys");

        slideTime.setEnabled(false);

        this.parser = new L_Run();
        this.listeners();
    }

    /**
     * Show the GUI-Form
     */
    public void showMe() {
        this.setVisible(true);
    }

    // All active listeners, loaded from constructor
    private void listeners() {
        btnSelectFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                selectFile();
            }
        });

        slideTime.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if(lastSliderValue != getSlideValue()) {
                    lastSliderValue = getSlideValue();
                    loadImage(true);
                }
            }
        });

        this.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                loadImage(false);
            }
        });
    }

    /**
     * Open File Selector to select file to parse and render
     */
    public void selectFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Log files", "csv");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(setDirectory);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile().getAbsolutePath();
            setDirectory = chooser.getCurrentDirectory();
            lblSelectedFile.setText(selectedFile);
            parseFile(selectedFile);
            paneImage.setBorder(new TitledBorder(parser.getMapname()));
            slideTime.setEnabled(true);
            loadImage(true);
        }
    }

    private void setSlideMin() {
        slideTime.setMinimum(0);
    }

    // Set max value on slider
    private void setSlideMax(int value) {
        int newValue = value / slideConvert;
        slideTime.setMaximum(newValue);
    }

    // Set value to slider
    private void setSlideValue(int value) {
        int newValue = value / slideConvert;
        slideTime.setValue(newValue);
    }

    // Get value from slider
    private int getSlideValue() {
        int newValue = slideTime.getValue() * slideConvert;
        showSlideValue(slideTime.getValue());
        return newValue;
    }

    private void showSlideValue(int value) {
        lblShowValue.setText(Integer.toString(value) + "s");
    }

    /**
     * Scale rectangle to a specific locked boundry
     * @param imgSize       Rectangle
     * @param boundary      Boundry-rectangle
     * @return              Scaled Rectangle, keept aspect ratio
     */
    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        if (original_width > bound_width) {
            new_width = bound_width;
            new_height = (new_width * original_height) / original_width;
        }

        if (new_height > bound_height) {
            new_height = bound_height;
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    /**
     * Parse the selected file into the L_Run -object
     * @param filename
     */
    private void parseFile(String filename) {
        parser.parse(filename);
        setSlideMin();
        setSlideMax(parser.getEndTime());
        setSlideValue(parser.getEndTime());
    }

    /**
     * Load Image to JPanel
     * @param reRender      If image should be rendered again
     */
    private void loadImage(Boolean reRender) {
        Image biSized;
        ImageIcon imageIcon;

        if(reRender) { parser.render(getSlideValue()); }
        bi = parser.getLastRenderedImage();

        Dimension size = getScaledDimension(new Dimension(bi.getWidth(), bi.getHeight()),
                                            new Dimension(viewImage.getWidth(), viewImage.getHeight()));
        Double imgWidth = size.getWidth();
        int intWidth = imgWidth.intValue();
        Double imgHeight = size.getHeight();
        int intHeight = imgHeight.intValue();


        biSized = bi.getScaledInstance(intWidth, intHeight, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(biSized);
        viewImage.setIcon(imageIcon);
    }
}
