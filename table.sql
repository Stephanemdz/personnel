CREATE TABLE ligue (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL, 
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_modification DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    date_suppression DATETIME,
    employe_id INT(11),
    FOREIGN KEY (employe_id) REFERENCES compte_employe(id) 
);

CREATE TABLE compte_employe (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    admin_ligue BOOLEAN DEFAULT FALSE, 
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(250) UNIQUE NOT NULL, 
    password CHAR(64) NOT NULL, 
    date_arrive DATE,
    date_depart DATE,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_modification DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    date_suppression DATETIME,
    ligue_id INT(11),
    FOREIGN KEY (ligue_id) REFERENCES ligue(id)
);
