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

public class SignInForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public SignInForm() {
        setTitle("Sign In / Register");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelMain = new JPanel();
        panelMain.setLayout(new GridLayout(4, 1));

        JPanel userNamePanel = new JPanel();
        userNamePanel.setLayout(new GridLayout(1, 2));

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new GridLayout(1, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton signInButton = new JButton("Sign In");
        JButton registerButton = new JButton("Register");

        // Set preferred size for the buttons
        Dimension buttonSize = new Dimension(150, 30); // Adjust the width and height as needed
        signInButton.setPreferredSize(buttonSize);
        registerButton.setPreferredSize(buttonSize);

        userNamePanel.add(usernameLabel);
        userNamePanel.add(usernameField);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        panelMain.add(userNamePanel);
        panelMain.add(passwordPanel);
        panelMain.add(signInButton);
        panelMain.add(registerButton);

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                // Perform sign in process here
                signIn(username, password);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open registration form
                dispose(); // Close the current form
                openRegistrationForm();
            }
        });

        getContentPane().add(panelMain, BorderLayout.CENTER);
        setVisible(true);
    }

    private void signIn(String username, String password) {
        // Implementation of sign in process
        try {
            JOptionPane.showMessageDialog(this, "Sign in successful!");
            if (DatabaseConnection.signIn(username, password)){
                BookTableGUI gui = new BookTableGUI();
                gui.setVisible(true);
                this.setVisible(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Sign in failed! Because " + e);
        }
        
    }

    private void openRegistrationForm() {
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setVisible(true);
        this.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SignInForm();
            }
        });
    }
}