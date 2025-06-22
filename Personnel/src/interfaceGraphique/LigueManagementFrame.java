
package interfaceGraphique;

import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LigueManagementFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private GestionPersonnel gestionPersonnel;

    public LigueManagementFrame() {
        this(GestionPersonnel.getGestionPersonnel());
    }

    public LigueManagementFrame(GestionPersonnel gestionPersonnel) {
        this.gestionPersonnel = gestionPersonnel;

        setTitle("Gestion des ligues");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton buttonShowLigues = new JButton("Afficher les ligues");
        buttonShowLigues.addActionListener((ActionEvent e) -> {
            StringBuilder liguesInfo = new StringBuilder();
            gestionPersonnel.getLigues().forEach(ligue -> {
                liguesInfo.append("Ligue: ").append(ligue.getNom()).append("\n");
            });
            JOptionPane.showMessageDialog(this, liguesInfo.toString(), "Liste des ligues", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton buttonAddLigue = new JButton("Ajouter une ligue");
        buttonAddLigue.addActionListener((ActionEvent e) -> {
            String nom = JOptionPane.showInputDialog(this, "Nom de la ligue :", "Ajouter une ligue", JOptionPane.PLAIN_MESSAGE);
            if (nom != null && !nom.trim().isEmpty()) {
                try {
                    gestionPersonnel.addLigue(nom);
                    JOptionPane.showMessageDialog(this, "Ligue ajoutée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } catch (SauvegardeImpossible ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la ligue.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton buttonSelectLigue = new JButton("Sélectionner une ligue");
        buttonSelectLigue.addActionListener((ActionEvent e) -> {
            String[] ligueNames = gestionPersonnel.getLigues().stream().map(Ligue::getNom).toArray(String[]::new);
            String selectedLigue = (String) JOptionPane.showInputDialog(this, "Sélectionnez une ligue :", "Sélectionner une ligue",
                    JOptionPane.PLAIN_MESSAGE, null, ligueNames, ligueNames.length > 0 ? ligueNames[0] : null);
            if (selectedLigue != null) {
                Ligue ligue = gestionPersonnel.getLigues().stream().filter(l -> l.getNom().equals(selectedLigue)).findFirst().orElse(null);
                if (ligue != null) {
                    showLigueOptions(ligue);
                }
            }
        });

        JButton buttonReturn = new JButton("Retour");
        buttonReturn.addActionListener((ActionEvent e) -> {
            this.dispose();
        });

        panel.add(buttonShowLigues);
        panel.add(buttonAddLigue);
        panel.add(buttonSelectLigue);
        panel.add(buttonReturn);

        add(panel);
    }

    private void showLigueOptions(Ligue ligue) {
        JFrame optionsFrame = new JFrame("Options pour " + ligue.getNom());
        optionsFrame.setSize(400, 300);
        optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        optionsFrame.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        JButton buttonShowLigue = new JButton("Afficher la ligue");
        buttonShowLigue.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(optionsFrame, "Ligue: " + ligue.getNom(), "Afficher la ligue", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton buttonManageEmployees = new JButton("Gérer les employés de " + ligue.getNom());
        buttonManageEmployees.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(optionsFrame, "Gestion des employés non implémentée.", "Gérer les employés", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton buttonChangeAdmin = new JButton("Changer l'administrateur");
        buttonChangeAdmin.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(optionsFrame, "Changement d'administrateur non implémenté.", "Changer l'administrateur", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton buttonRename = new JButton("Renommer");
        buttonRename.addActionListener((ActionEvent e) -> {
            String newName = JOptionPane.showInputDialog(optionsFrame, "Nouveau nom :", "Renommer", JOptionPane.PLAIN_MESSAGE);
            if (newName != null && !newName.trim().isEmpty()) {
                ligue.setNom(newName);
                JOptionPane.showMessageDialog(optionsFrame, "Ligue renommée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton buttonDelete = new JButton("Supprimer");
        buttonDelete.addActionListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(optionsFrame, "Êtes-vous sûr de vouloir supprimer cette ligue ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                gestionPersonnel.remove(ligue);
                JOptionPane.showMessageDialog(optionsFrame, "Ligue supprimée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                optionsFrame.dispose();
            }
        });

        JButton buttonReturn = new JButton("Retour");
        buttonReturn.addActionListener((ActionEvent e) -> {
            optionsFrame.dispose();
        });

        panel.add(buttonShowLigue);
        panel.add(buttonManageEmployees);
        panel.add(buttonChangeAdmin);
        panel.add(buttonRename);
        panel.add(buttonDelete);
        panel.add(buttonReturn);

        optionsFrame.add(panel);
        optionsFrame.setVisible(true);
    }
}
