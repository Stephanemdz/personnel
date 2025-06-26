CREATE TABLE ligue (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_modification DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    date_suppression DATETIME,
    admin_ligue INT(11)
);

CREATE TABLE compte_employe (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    is_admin BOOLEAN DEFAULT FALSE,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255),
    email VARCHAR(250) UNIQUE,
    password CHAR(64) NOT NULL,
    dateArrivee DATE,
    dateDepart DATE,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_modification DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    date_suppression DATETIME,
    ligue_id INT(11),
    FOREIGN KEY (ligue_id) REFERENCES ligue(id)
);


ALTER TABLE ligue
ADD CONSTRAINT FK_admin_ligue
FOREIGN KEY (admin_ligue) REFERENCES compte_employe(id);
