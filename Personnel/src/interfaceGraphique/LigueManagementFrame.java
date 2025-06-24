
package interfaceGraphique;

import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
		    showEmployeeOptions(ligue);
		});


        JButton buttonChangeAdmin = new JButton("Changer l'administrateur");

buttonChangeAdmin.addActionListener((ActionEvent e) -> {
    String[] employeeNames = ligue.getEmployes().stream()
        .map(employe -> employe.getNom() + " " + employe.getPrenom())
        .toArray(String[]::new);

    String selectedEmployee = (String) JOptionPane.showInputDialog(
        optionsFrame,
        "Sélectionnez un nouvel administrateur :",
        "Changer l'administrateur",
        JOptionPane.PLAIN_MESSAGE,
        null,
        employeeNames,
        employeeNames.length > 0 ? employeeNames[0] : null
    );

    if (selectedEmployee != null) {
        Employe newAdmin = ligue.getEmployes().stream()
            .filter(employe -> (employe.getNom() + " " + employe.getPrenom()).equals(selectedEmployee))
            .findFirst()
            .orElse(null);

        if (newAdmin != null) {
            ligue.setAdministrateur(newAdmin);
			JOptionPane.showMessageDialog(
			    optionsFrame,
			    "L'administrateur a été changé avec succès.",
			    "Succès",
			    JOptionPane.INFORMATION_MESSAGE
			);
        }
    }
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
    
// Méthode pour afficher les options de gestion des employés d'une ligue
private void showEmployeeOptions(Ligue ligue) {
    JFrame employeeOptionsFrame = new JFrame("Gérer les employés de " + ligue.getNom());
    employeeOptionsFrame.setSize(400, 300);
    employeeOptionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    employeeOptionsFrame.setLocationRelativeTo(this);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(4, 1, 10, 10));

    // Bouton "Afficher les employés"
    JButton buttonShowEmployees = new JButton("Afficher les employés");
    buttonShowEmployees.addActionListener((ActionEvent e) -> {
        StringBuilder employeesInfo = new StringBuilder();
        ligue.getEmployes().forEach(employe -> {
            employeesInfo.append("Nom: ").append(employe.getNom())
                         .append(", Prénom: ").append(employe.getPrenom())
                         .append(", Email: ").append(employe.getMail())
                         .append("\n");
        });
        JOptionPane.showMessageDialog(employeeOptionsFrame, employeesInfo.toString(), "Liste des employés", JOptionPane.INFORMATION_MESSAGE);
    });

    // Bouton "Ajouter un employé"
    JButton buttonAddEmployee = new JButton("Ajouter un employé");

	buttonAddEmployee.addActionListener((ActionEvent e) -> {
	    String nom = JOptionPane.showInputDialog(employeeOptionsFrame, "Nom de l'employé :", "Ajouter un employé", JOptionPane.PLAIN_MESSAGE);
	    String prenom = JOptionPane.showInputDialog(employeeOptionsFrame, "Prénom de l'employé :", "Ajouter un employé", JOptionPane.PLAIN_MESSAGE);
	    String email = JOptionPane.showInputDialog(employeeOptionsFrame, "Email de l'employé :", "Ajouter un employé", JOptionPane.PLAIN_MESSAGE);
	    String password = JOptionPane.showInputDialog(employeeOptionsFrame, "Mot de passe de l'employé :", "Ajouter un employé", JOptionPane.PLAIN_MESSAGE);
	    String dateArriveeStr = JOptionPane.showInputDialog(employeeOptionsFrame, "Date d'arrivée (JJ/MM/AAAA) :", "Ajouter un employé", JOptionPane.PLAIN_MESSAGE);
	    String dateDepartStr = JOptionPane.showInputDialog(employeeOptionsFrame, "Date de départ (JJ/MM/AAAA, laisser vide si pas de date) :", "Ajouter un employé", JOptionPane.PLAIN_MESSAGE);
	
	    try {
	        LocalDate dateArrivee = LocalDate.parse(dateArriveeStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        LocalDate dateDepart = null;
	
	        if (dateDepartStr != null && !dateDepartStr.trim().isEmpty()) {
	            dateDepart = LocalDate.parse(dateDepartStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	            if (dateDepart.isBefore(dateArrivee)) {
	                throw new IllegalArgumentException("La date de départ doit être postérieure ou égale à la date d'arrivée.");
	            }
	        }
	
	        ligue.addEmploye(nom, prenom, email, password, dateArrivee, dateDepart);
	        JOptionPane.showMessageDialog(employeeOptionsFrame, "Employé ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
	    } catch (DateTimeParseException ex) {
	        JOptionPane.showMessageDialog(employeeOptionsFrame, "Format de date invalide. Veuillez utiliser le format JJ/MM/AAAA.", "Erreur", JOptionPane.ERROR_MESSAGE);
	    } catch (IllegalArgumentException ex) {
	        JOptionPane.showMessageDialog(employeeOptionsFrame, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
	    } catch (SauvegardeImpossible ex) {
	        JOptionPane.showMessageDialog(employeeOptionsFrame, "Erreur lors de l'ajout de l'employé.", "Erreur", JOptionPane.ERROR_MESSAGE);
	    }
	});


    // Bouton "Sélectionner un employé"
    JButton buttonSelectEmployee = new JButton("Sélectionner un employé");

	buttonSelectEmployee.addActionListener((ActionEvent e) -> {
	    String[] employeeNames = ligue.getEmployes().stream().map(employe -> employe.getNom() + " " + employe.getPrenom()).toArray(String[]::new);
	    String selectedEmployee = (String) JOptionPane.showInputDialog(employeeOptionsFrame, "Sélectionnez un employé :", "Sélectionner un employé",
	            JOptionPane.PLAIN_MESSAGE, null, employeeNames, employeeNames.length > 0 ? employeeNames[0] : null);
	    if (selectedEmployee != null) {
	        Employe employe = ligue.getEmployes().stream()
	                               .filter(event -> (event.getNom() + " " + event.getPrenom()).equals(selectedEmployee))
	                               .findFirst().orElse(null);
	        if (employe != null) {
	            showEmployeeActions(employe);
	        }
	    }
	});


    // Bouton "Retour"
    JButton buttonReturn = new JButton("Retour");
    buttonReturn.addActionListener((ActionEvent e) -> {
        employeeOptionsFrame.dispose();
    });

    panel.add(buttonShowEmployees);
    panel.add(buttonAddEmployee);
    panel.add(buttonSelectEmployee);
    panel.add(buttonReturn);

    employeeOptionsFrame.add(panel);
    employeeOptionsFrame.setVisible(true);
}


//// Méthode pour afficher les actions possibles sur un employé
private void showEmployeeActions(Employe employe) {
    JFrame employeeActionsFrame = new JFrame("Modifier le compte de " + employe.getNom());
    employeeActionsFrame.setSize(400, 400);
    employeeActionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    employeeActionsFrame.setLocationRelativeTo(this);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(7, 2, 10, 10));

    // Champs de modification
    panel.add(new JLabel("Nom :"));
    JTextField fieldName = new JTextField(employe.getNom());
    panel.add(fieldName);

    panel.add(new JLabel("Prénom :"));
    JTextField fieldFirstName = new JTextField(employe.getPrenom());
    panel.add(fieldFirstName);

    panel.add(new JLabel("Adresse mail :"));
    JTextField fieldEmail = new JTextField(employe.getMail());
    panel.add(fieldEmail);

    panel.add(new JLabel("Mot de passe :"));
    JTextField fieldPassword = new JTextField(employe.getPassword());
    panel.add(fieldPassword);

    panel.add(new JLabel("Date d'arrivée (JJ/MM/AAAA) :"));
    JTextField fieldArrivalDate = new JTextField(employe.getDateArrivee() != null ? employe.getDateArrivee().toString() : "");
    panel.add(fieldArrivalDate);

    panel.add(new JLabel("Date de départ (JJ/MM/AAAA, laisser vide si pas de date) :"));
    JTextField fieldDepartureDate = new JTextField(employe.getDateDepart() != null ? employe.getDateDepart().toString() : "");
    panel.add(fieldDepartureDate);

    // Bouton "Enregistrer"
    JButton buttonSave = new JButton("Enregistrer les modifications");
    buttonSave.addActionListener((ActionEvent e) -> {
        try {
            employe.setNom(fieldName.getText());
            employe.setPrenom(fieldFirstName.getText());
            employe.setMail(fieldEmail.getText());
            employe.setPassword(fieldPassword.getText());
            employe.setDateArrivee(LocalDate.parse(fieldArrivalDate.getText()));

            if (!fieldDepartureDate.getText().trim().isEmpty()) {
                employe.setDateDepart(LocalDate.parse(fieldDepartureDate.getText()));
            } else {
                employe.setDateDepart(null); // Permet de laisser la date de départ vide
            }

            JOptionPane.showMessageDialog(employeeActionsFrame, "Modifications enregistrées avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(employeeActionsFrame, "Erreur lors de l'enregistrement des modifications : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    });

    // Bouton "Retour"
    JButton buttonReturn = new JButton("Retour");
    buttonReturn.addActionListener((ActionEvent e) -> {
        employeeActionsFrame.dispose();
    });

    panel.add(buttonSave);
    panel.add(buttonReturn);

    employeeActionsFrame.add(panel);
    employeeActionsFrame.setVisible(true);
}


}
