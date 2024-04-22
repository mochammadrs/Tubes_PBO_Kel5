/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectperpus;

/**
 *
 * @author rakha
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrationForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField dobField;
    private JTextField contactField;

    public RegistrationForm() {
        setTitle("Registration Form");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelMain = new JPanel();
        panelMain.setLayout(new GridLayout(8, 1));

        JPanel usernamePanel = new JPanel(new GridLayout(1, 2));
        JPanel passwordPanel = new JPanel(new GridLayout(1, 2));
        JPanel namePanel = new JPanel(new GridLayout(1, 2));
        JPanel addressPanel = new JPanel(new GridLayout(1, 2));
        JPanel dobPanel = new JPanel(new GridLayout(1, 2));
        JPanel contactPanel = new JPanel(new GridLayout(1, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField();
        JLabel dobLabel = new JLabel("Date of Birth:");
        dobField = new JTextField();
        JLabel contactLabel = new JLabel("Contact:");
        contactField = new JTextField();

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        Dimension buttonSize = new Dimension(150, 30);
        registerButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        addressPanel.add(addressLabel);
        addressPanel.add(addressField);
        dobPanel.add(dobLabel);
        dobPanel.add(dobField);
        contactPanel.add(contactLabel);
        contactPanel.add(contactField);

        panelMain.add(usernamePanel);
        panelMain.add(passwordPanel);
        panelMain.add(namePanel);
        panelMain.add(addressPanel);
        panelMain.add(dobPanel);
        panelMain.add(contactPanel);
        panelMain.add(registerButton);
        panelMain.add(backButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String name = nameField.getText();
                String address = addressField.getText();
                Date dob = getDateFromString(dobField.getText());
                String contact = contactField.getText();
                // Perform registration process here
                register(username, password, name, address, dob, contact);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current form
                openSignInForm(); // Open the registration form
            }
        });

        getContentPane().add(panelMain, BorderLayout.CENTER);
    }
    // Metode untuk mengonversi string menjadi objek Date

    private Date getDateFromString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid date", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void openSignInForm() {
        SignInForm signInForm = new SignInForm();
        signInForm.setVisible(true);
        this.setVisible(false);
    }

    private void register(String username, String password, String name, String address, Date dob, String contact) {
        // Implementation of registration process
        try {
            DatabaseConnection.registerUser(username, password, name, address, dob, contact);
            JOptionPane.showMessageDialog(this, "Registration successful!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Registration failed! Because " + e);
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrationForm::new);
    }
}
