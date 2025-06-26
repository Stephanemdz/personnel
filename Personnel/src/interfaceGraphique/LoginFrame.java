
package interfaceGraphique;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public LoginFrame() {
        // Configuration de la fenêtre
        setTitle("Connexion à M2L");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre

        // Création du panneau principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10)); // Grille pour les composants

        // Ajout des composants
        JLabel labelInfo = new JLabel("Connectez-vous à M2L"); // Texte fixe
        labelInfo.setHorizontalAlignment(SwingConstants.CENTER); // Centrer le texte
        JLabel labelUsername = new JLabel("Nom d'utilisateur:");
        JTextField textUsername = new JTextField();
        JLabel labelPassword = new JLabel("Mot de passe:");
        JPasswordField textPassword = new JPasswordField();
        JButton buttonLogin = new JButton("Se connecter");

        // Ajout des composants au panneau
        panel.add(labelInfo); // Afficher le texte fixe
        panel.add(new JLabel()); // Espace vide
        panel.add(labelUsername);
        panel.add(textUsername);
        panel.add(labelPassword);
        panel.add(textPassword);
        panel.add(new JLabel()); // Espace vide
        panel.add(buttonLogin);

        // Encapsulation du panneau principal avec un padding
        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding de 15px
        paddedPanel.add(panel, BorderLayout.CENTER);

        // Ajout du panneau avec padding à la fenêtre
        add(paddedPanel);

        // Gestionnaire d'événements pour le bouton

buttonLogin.addActionListener(e -> {
    String username = textUsername.getText();
    String password = new String(textPassword.getPassword());

    try {
        // Connexion à la base de données
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/m2l", "Admin", "root");

        // Requête pour récupérer les informations du root

		String query = "SELECT nom, password FROM compte_employe WHERE nom = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, username); // Utilisation du nom d'utilisateur saisi

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String dbUsername = resultSet.getString("nom");
            String dbPassword = resultSet.getString("password");

            // Vérification des informations
            if (username.equals(dbUsername) && password.equals(dbPassword)) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Connexion réussie !");
                new HomeFrame().setVisible(true); // Afficher la page d'accueil
                dispose(); // Fermer la fenêtre de connexion
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Nom d'utilisateur ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(LoginFrame.this, "Nom d'utilisateur ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Fermeture des ressources
        resultSet.close();
        statement.close();
        connection.close();
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(LoginFrame.this, "Erreur de connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
});


    }
}
