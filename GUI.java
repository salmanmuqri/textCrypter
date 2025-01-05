import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GUI {

    private JFrame mainFrame;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GUI window = new GUI();
                window.mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GUI() {
        initialize();
    }

    private void initialize() {
        mainFrame = new JFrame("Text Encrypter & Decrypter");
        mainFrame.setBounds(100, 100, 350, 150);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(new GridLayout(2, 1, 10, 10));

        JButton encryptButton = new JButton("Encrypt Text");
        JButton decryptButton = new JButton("Decrypt Text");

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEncryptionWindow();
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDecryptionWindow();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        mainFrame.getContentPane().add(buttonPanel);

        mainFrame.setResizable(false);
    }

    private void showEncryptionWindow() {
        JFrame encryptFrame = new JFrame("Encrypt Text");
        encryptFrame.setBounds(100, 100, 500, 400);
        encryptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        encryptFrame.setLayout(new BoxLayout(encryptFrame.getContentPane(), BoxLayout.Y_AXIS));
        encryptFrame.setResizable(false);

        JTextField keyField = new JTextField(16);
        JTextArea inputTextArea = new JTextArea(4, 40);
        JTextArea outputTextArea = new JTextArea(4, 40);
        outputTextArea.setEditable(false);

        JButton encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String key = keyField.getText();
                    String inputText = inputTextArea.getText();
                    if (key.length() == 16) {
                        String encryptedText = Encrypter.encrypt(inputText, key);
                        outputTextArea.setText(encryptedText);
                    } else {
                        JOptionPane.showMessageDialog(encryptFrame, "Key must be 16 characters long!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(encryptFrame, "Encryption failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyField.setText("");
                inputTextArea.setText("");
                outputTextArea.setText("");
            }
        });

        JButton exportButton = new JButton("Export Output");
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Save Encrypted Output");
                    int userSelection = fileChooser.showSaveDialog(encryptFrame);
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File fileToSave = fileChooser.getSelectedFile();
						if (!fileToSave.getName().endsWith(".txt")) {
                		    fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
                		}
                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave));
                        writer.write(outputTextArea.getText());
                        writer.close();
                        JOptionPane.showMessageDialog(encryptFrame, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(encryptFrame, "Failed to save file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel keyPanel = new JPanel(new FlowLayout());
        keyPanel.add(new JLabel("Enter Key (16 characters for AES):"));
        keyPanel.add(keyField);

        JPanel textPanel = new JPanel(new FlowLayout());
        textPanel.add(new JLabel("Enter Text to Encrypt:"));
        textPanel.add(new JScrollPane(inputTextArea));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(encryptButton);

        JPanel outputPanel = new JPanel(new FlowLayout());
        outputPanel.add(new JLabel("Encrypted Output:"));
        outputPanel.add(new JScrollPane(outputTextArea));

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(clearButton);
        bottomPanel.add(exportButton);

        encryptFrame.add(keyPanel);
        encryptFrame.add(textPanel);
        encryptFrame.add(buttonPanel);
        encryptFrame.add(outputPanel);
        encryptFrame.add(bottomPanel);

        encryptFrame.setVisible(true);
    }

    private void showDecryptionWindow() {
        JFrame decryptFrame = new JFrame("Decrypt Text");
        decryptFrame.setBounds(100, 100, 500, 400);
        decryptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        decryptFrame.setLayout(new BoxLayout(decryptFrame.getContentPane(), BoxLayout.Y_AXIS));
        decryptFrame.setResizable(false);

        JTextField keyField = new JTextField(16);
        JTextArea inputTextArea = new JTextArea(4, 40);
        JTextArea outputTextArea = new JTextArea(4, 40);
        outputTextArea.setEditable(false);

        JButton decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String key = keyField.getText();
                    String encryptedText = inputTextArea.getText();
                    if (key.length() == 16) {
                        String decryptedText = Decrypter.decrypt(encryptedText, key);
                        outputTextArea.setText(decryptedText);
                    } else {
                        JOptionPane.showMessageDialog(decryptFrame, "Key must be 16 characters long!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(decryptFrame, "Decryption failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyField.setText("");
                inputTextArea.setText("");
                outputTextArea.setText("");
            }
        });

        JButton exportButton = new JButton("Export Output");
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Save Decrypted Output");
                    int userSelection = fileChooser.showSaveDialog(decryptFrame);
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File fileToSave = fileChooser.getSelectedFile();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave));
                        writer.write(outputTextArea.getText());
                        writer.close();
                        JOptionPane.showMessageDialog(decryptFrame, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(decryptFrame, "Failed to save file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JPanel keyPanel = new JPanel(new FlowLayout());
        keyPanel.add(new JLabel("Enter Key (16 characters for AES):"));
        keyPanel.add(keyField);

        JPanel textPanel = new JPanel(new FlowLayout());
        textPanel.add(new JLabel("Enter Text to Decrypt:"));
        textPanel.add(new JScrollPane(inputTextArea));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(decryptButton);

        JPanel outputPanel = new JPanel(new FlowLayout());
        outputPanel.add(new JLabel("Decrypted Output:"));
        outputPanel.add(new JScrollPane(outputTextArea));

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(clearButton);
        bottomPanel.add(exportButton);

        decryptFrame.add(keyPanel);
        decryptFrame.add(textPanel);
        decryptFrame.add(buttonPanel);
        decryptFrame.add(outputPanel);
        decryptFrame.add(bottomPanel);

        decryptFrame.setVisible(true);
    }
}


        