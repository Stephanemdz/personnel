
package interfaceGraphique;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HomeFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public HomeFrame() {
        // Configuration de la fenêtre
        setTitle("Page d'accueil");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre

        // Création du panneau principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10)); // Grille pour les boutons

        // Bouton "Gérer le compte root"
        JButton buttonManageRoot = new JButton("Chargement...");
        loadRootName(buttonManageRoot);

        buttonManageRoot.addActionListener((ActionEvent e) -> {
            // Ouvrir RootManagementFrame
            RootManagementFrame rootManagementFrame = new RootManagementFrame();
            rootManagementFrame.setVisible(true);
        });

        // Bouton "Gérer les ligues"
        JButton buttonManageLigues = new JButton("Gérer les ligues");
        buttonManageLigues.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(this, "Gestion des ligues non implémentée.");
        });

        // Bouton "Quitter"
        JButton buttonQuit = new JButton("Quitter");
        buttonQuit.addActionListener((ActionEvent e) -> {
            System.exit(0); // Fermer l'application
        });

        // Ajout des boutons au panneau
        panel.add(buttonManageRoot);
        panel.add(buttonManageLigues);
        panel.add(buttonQuit);

        // Ajout du panneau à la fenêtre
        add(panel);
    }

    private void loadRootName(JButton button) {
        try {
            // Connexion à la base de données
            String url = "jdbc:mysql://localhost:3306/m2l";
            String user = "Admin";
            String password = "root";
            Connection connection = DriverManager.getConnection(url, user, password);

            // Requête pour récupérer le nom du root
            String query = "SELECT nom FROM compte_employe WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 1); // Exemple : ID du root

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String rootName = resultSet.getString("nom");
                button.setText("Gérer le compte (" + rootName + ")");
            } else {
                button.setText("Gérer le compte (Non trouvé)");
            }

            // Fermeture des ressources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            button.setText("Erreur de chargement");
        }
    }
}
