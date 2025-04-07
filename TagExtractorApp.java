import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

/**
 * TagExtractorApp is the main GUI for the Tag/Keyword extractor.
 * Allows user to load a text file and stop word file, extract keywords,
 * display them in a scrollable area, and save the results.
 */
public class TagExtractorApp extends JFrame {

    private JTextArea outputArea;
    private JLabel fileNameLabel;
    private File stopWordsFile;

    public TagExtractorApp() {
        super("Tag / Keyword Extractor");
        setLayout(new BorderLayout());

        // Top panel with file name display
        JPanel topPanel = new JPanel(new FlowLayout());
        fileNameLabel = new JLabel("No file selected");
        topPanel.add(fileNameLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center area for output
        outputArea = new JTextArea(25, 50);
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton loadStopWordsBtn = new JButton("Load Stop Words");
        loadStopWordsBtn.addActionListener(e -> loadStopWords());

        JButton loadTextFileBtn = new JButton("Load Text File");
        loadTextFileBtn.addActionListener(e -> loadTextFile());

        JButton saveOutputBtn = new JButton("Save Output");
        saveOutputBtn.addActionListener(e -> saveOutput());

        buttonPanel.add(loadStopWordsBtn);
        buttonPanel.add(loadTextFileBtn);
        buttonPanel.add(saveOutputBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void loadStopWords() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            stopWordsFile = chooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "Stop words loaded: " + stopWordsFile.getName());
        }
    }

    private void loadTextFile() {
        if (stopWordsFile == null) {
            JOptionPane.showMessageDialog(this, "Load stop words file first!");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File inputFile = chooser.getSelectedFile();
            fileNameLabel.setText("File: " + inputFile.getName());

            Map<String, Integer> tags = TagExtractorLogic.extractTags(inputFile, stopWordsFile);
            StringBuilder sb = new StringBuilder();
            tags.forEach((word, count) -> sb.append(word).append(": ").append(count).append("\n"));
            outputArea.setText(sb.toString());
        }
    }

    private void saveOutput() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File outputFile = chooser.getSelectedFile();
            boolean success = TagExtractorLogic.saveTags(outputArea.getText(), outputFile);
            String message = success ? "Saved to " + outputFile.getName() : "Failed to save output";
            JOptionPane.showMessageDialog(this, message);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TagExtractorApp::new);
    }
}
