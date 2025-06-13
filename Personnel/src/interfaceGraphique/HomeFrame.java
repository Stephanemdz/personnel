
package interfaceGraphique;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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
        JButton buttonManageRoot = new JButton("Gérer le compte root");
        buttonManageRoot.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(this, "Gestion du compte root non implémentée.");
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
}
