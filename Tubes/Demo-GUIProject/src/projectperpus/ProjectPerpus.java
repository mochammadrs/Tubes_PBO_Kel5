/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package projectperpus;

import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author rakha
 */

public class ProjectPerpus extends JFrame {



    public static void main(String[] args) throws SQLException {
        DatabaseConnection.getConnection();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SignInForm();
            }
        });
    }
}

