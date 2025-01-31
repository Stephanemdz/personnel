CREATE TABLE compte_employe (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    admin_ligue BOOLEAN,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    email VARCHAR(500),
    password CHAR(64),
    date_arrive DATE,
    date_depart DATE
);
CREATE TABLE ligue(
    id_ligue INT(11) PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255)
);
