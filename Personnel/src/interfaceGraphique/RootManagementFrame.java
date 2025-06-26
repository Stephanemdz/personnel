
package interfaceGraphique;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RootManagementFrame extends JFrame {
    private JPanel mainPanel;

    public RootManagementFrame() {
        setTitle("Gestion du compte root");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());

        mainPanel.add(createHomePanel(), "Home");
        mainPanel.add(createDisplayEmployeePanel(), "DisplayEmployee");
        mainPanel.add(createChangeNamePanel(), "ChangeName");
        mainPanel.add(createChangeFirstNamePanel(), "ChangeFirstName");
        mainPanel.add(createChangeEmailPanel(), "ChangeEmail");
        mainPanel.add(createChangePasswordPanel(), "ChangePassword");

        add(mainPanel);
        showPanel("Home");
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));

        JButton btnDisplayEmployee = new JButton("Afficher l'employé");
        btnDisplayEmployee.addActionListener(e -> showPanel("DisplayEmployee"));

        JButton btnChangeName = new JButton("Changer le nom");
        btnChangeName.addActionListener(e -> showPanel("ChangeName"));

        JButton btnChangeFirstName = new JButton("Changer le prénom");
        btnChangeFirstName.addActionListener(e -> showPanel("ChangeFirstName"));

        JButton btnChangeEmail = new JButton("Changer le mail");
        btnChangeEmail.addActionListener(e -> showPanel("ChangeEmail"));

        JButton btnChangePassword = new JButton("Changer le mot de passe");
        btnChangePassword.addActionListener(e -> showPanel("ChangePassword"));

        JButton btnQuit = new JButton("Quitter");
        btnQuit.addActionListener(e -> dispose());

        panel.add(btnDisplayEmployee);
        panel.add(btnChangeName);
        panel.add(btnChangeFirstName);
        panel.add(btnChangeEmail);
        panel.add(btnChangePassword);
        panel.add(btnQuit);

        return panel;
    }


	private JPanel createDisplayEmployeePanel() {
	    JPanel panel = new JPanel(new BorderLayout());
	    JLabel label = new JLabel("Informations du root :");
	    JTextArea rootInfo = new JTextArea(5, 20);
	    rootInfo.setEditable(false);
	
	    JButton btnLoad = new JButton("Charger");
	    btnLoad.addActionListener(e -> {
	        try {
	            // Connexion à la base de données
	            String url = "jdbc:mysql://localhost:3306/m2l";
	            String user = "Admin";
	            String password = "root";
	            Connection connection = DriverManager.getConnection(url, user, password);
	
	            // Requête pour récupérer les informations du root
	            String query = "SELECT nom, email FROM compte_employe WHERE id = ?";
	            PreparedStatement statement = connection.prepareStatement(query);
	            statement.setInt(1, 1); // Exemple : ID du root
	
	            ResultSet resultSet = statement.executeQuery();
	            if (resultSet.next()) {
	                String rootName = resultSet.getString("nom");
	                String rootEmail = resultSet.getString("email");
	                rootInfo.setText("Nom: " + rootName + "\nEmail: " + rootEmail);
	            } else {
	                rootInfo.setText("Informations du root non trouvées.");
	            }
	
	            // Fermeture des ressources
	            resultSet.close();
	            statement.close();
	            connection.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            rootInfo.setText("Erreur lors du chargement des informations.");
	        }
	    });
	
	    JButton btnBack = new JButton("Retour");
	    btnBack.addActionListener(e -> showPanel("Home"));
	
	    panel.add(label, BorderLayout.NORTH);
	    panel.add(new JScrollPane(rootInfo), BorderLayout.CENTER);
	    panel.add(btnLoad, BorderLayout.WEST);
	    panel.add(btnBack, BorderLayout.SOUTH);
	    return panel;
	}



	private JPanel createChangeNamePanel() {
	    JPanel panel = new JPanel(new BorderLayout());
	    JLabel label = new JLabel("Nouveau nom :");
	    JTextField nameField = new JTextField(20);
	
	    JButton btnSave = new JButton("Enregistrer");
	    btnSave.addActionListener(e -> {
	        String newName = nameField.getText().trim();
	        if (!newName.isEmpty()) {
	            try {
	                // Connexion à la base de données
	                String url = "jdbc:mysql://localhost:3306/m2l";
	                String user = "Admin";
	                String password = "root";
	                Connection connection = DriverManager.getConnection(url, user, password);
	
	                // Requête pour mettre à jour le nom du root
	                String query = "UPDATE compte_employe SET nom = ? WHERE id = ?";
	                PreparedStatement statement = connection.prepareStatement(query);
	                statement.setString(1, newName);
	                statement.setInt(2, 1); // Exemple : ID du root
	
	                int rowsUpdated = statement.executeUpdate();
	                if (rowsUpdated > 0) {
	                    JOptionPane.showMessageDialog(this, "Nom modifié avec succès !");
	                } else {
	                    JOptionPane.showMessageDialog(this, "Erreur : le nom n'a pas été modifié.");
	                }
	
	                // Fermeture des ressources
	                statement.close();
	                connection.close();
	            } catch (Exception ex) {
	                ex.printStackTrace();
	                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du nom.");
	            }
	        } else {
	            JOptionPane.showMessageDialog(this, "Veuillez entrer un nom valide.");
	        }
	    });
	
	    JButton btnBack = new JButton("Retour");
	    btnBack.addActionListener(e -> showPanel("Home"));
	
	    panel.add(label, BorderLayout.NORTH);
	    panel.add(nameField, BorderLayout.CENTER);
	    panel.add(btnSave, BorderLayout.WEST);
	    panel.add(btnBack, BorderLayout.SOUTH);
	    return panel;
	}



private JPanel createChangeFirstNamePanel() {
    JPanel panel = new JPanel(new BorderLayout());
    JLabel label = new JLabel("Nouveau prénom :");
    JTextField firstNameField = new JTextField(20);

    JButton btnSave = new JButton("Enregistrer");
    btnSave.addActionListener(e -> {
        String newFirstName = firstNameField.getText().trim();
        if (!newFirstName.isEmpty()) {
            try {
                // Connexion à la base de données
                String url = "jdbc:mysql://localhost:3306/m2l";
                String user = "Admin";
                String password = "root";
                Connection connection = DriverManager.getConnection(url, user, password);

                // Requête pour mettre à jour le prénom
                String query = "UPDATE compte_employe SET prenom = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, newFirstName);
                statement.setInt(2, 1); // Exemple : ID du root

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Prénom modifié avec succès !");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur : le prénom n'a pas été modifié.");
                }

                // Fermeture des ressources
                statement.close();
                connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du prénom.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un prénom valide.");
        }
    });

    JButton btnBack = new JButton("Retour");
    btnBack.addActionListener(e -> showPanel("Home"));

    panel.add(label, BorderLayout.NORTH);
    panel.add(firstNameField, BorderLayout.CENTER);
    panel.add(btnSave, BorderLayout.WEST);
    panel.add(btnBack, BorderLayout.SOUTH);
    return panel;
}



	private JPanel createChangeEmailPanel() {
	    JPanel panel = new JPanel(new BorderLayout());
	    JLabel label = new JLabel("Nouvel email :");
	    JTextField emailField = new JTextField(20);
	
	    JButton btnSave = new JButton("Enregistrer");
	    btnSave.addActionListener(e -> {
	        String newEmail = emailField.getText().trim();
	        if (!newEmail.isEmpty()) {
	            try {
	                // Connexion à la base de données
	                String url = "jdbc:mysql://localhost:3306/m2l";
	                String user = "Admin";
	                String password = "root";
	                Connection connection = DriverManager.getConnection(url, user, password);
	
	                // Requête pour mettre à jour l'email du root
	                String query = "UPDATE compte_employe SET email = ? WHERE id = ?";
	                PreparedStatement statement = connection.prepareStatement(query);
	                statement.setString(1, newEmail);
	                statement.setInt(2, 1); // Exemple : ID du root
	
	                int rowsUpdated = statement.executeUpdate();
	                if (rowsUpdated > 0) {
	                    JOptionPane.showMessageDialog(this, "Email modifié avec succès !");
	                } else {
	                    JOptionPane.showMessageDialog(this, "Erreur : l'email n'a pas été modifié.");
	                }
	
	                // Fermeture des ressources
	                statement.close();
	                connection.close();
	            } catch (Exception ex) {
	                ex.printStackTrace();
	                JOptionPane.showMessageDialog(this, "Erreur lors de la modification de l'email.");
	            }
	        } else {
	            JOptionPane.showMessageDialog(this, "Veuillez entrer un email valide.");
	        }
	    });
	
	    JButton btnBack = new JButton("Retour");
	    btnBack.addActionListener(e -> showPanel("Home"));
	
	    panel.add(label, BorderLayout.NORTH);
	    panel.add(emailField, BorderLayout.CENTER);
	    panel.add(btnSave, BorderLayout.WEST);
	    panel.add(btnBack, BorderLayout.SOUTH);
	    return panel;
	}



private JPanel createChangePasswordPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    JLabel label = new JLabel("Nouveau mot de passe :");
    JPasswordField passwordField = new JPasswordField(20);

    JButton btnSave = new JButton("Enregistrer");
    btnSave.addActionListener(e -> {
        String newPassword = new String(passwordField.getPassword()).trim();
        if (!newPassword.isEmpty()) {
            try {
                // Connexion à la base de données
                String url = "jdbc:mysql://localhost:3306/m2l";
                String user = "Admin";
                String password = "root";
                Connection connection = DriverManager.getConnection(url, user, password);

                // Requête pour mettre à jour le mot de passe du root
                String query = "UPDATE compte_employe SET password = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, newPassword);
                statement.setInt(2, 1); // Exemple : ID du root

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Mot de passe modifié avec succès !");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur : le mot de passe n'a pas été modifié.");
                }

                // Fermeture des ressources
                statement.close();
                connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du mot de passe.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un mot de passe valide.");
        }
    });

    JButton btnBack = new JButton("Retour");
    btnBack.addActionListener(e -> showPanel("Home"));

    panel.add(label, BorderLayout.NORTH);
    panel.add(passwordField, BorderLayout.CENTER);
    panel.add(btnSave, BorderLayout.WEST);
    panel.add(btnBack, BorderLayout.SOUTH);
    return panel;
}


    private void showPanel(String panelName) {
        CardLayout layout = (CardLayout) mainPanel.getLayout();
        layout.show(mainPanel, panelName);
    }
}
//juste pour faire un commit
