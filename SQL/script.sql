-- Création de la base de données si elle n'existe pas déjà
CREATE DATABASE IF NOT EXISTS dataware_db;
USE dataware_db;

-- Création de la table Team pour stocker les informations des équipes
CREATE TABLE IF NOT EXISTS Team (
    id INT AUTO_INCREMENT PRIMARY KEY,    -- Identifiant unique pour chaque équipe
    name VARCHAR(255) NOT NULL UNIQUE     -- Nom de l'équipe, doit être unique
);

-- Création de la table Member pour les membres de l'équipe
CREATE TABLE IF NOT EXISTS Member (
    id INT AUTO_INCREMENT PRIMARY KEY,     -- Identifiant unique pour chaque membre
    first_name VARCHAR(255) NOT NULL,      -- Prénom du membre
    last_name VARCHAR(255) NOT NULL,       -- Nom de famille du membre
    email VARCHAR(255) NOT NULL UNIQUE,    -- Email unique pour chaque membre
    role ENUM('Project manager', 'Developer', 'Designer') NOT NULL -- Rôle dans l'équipe
);

-- Création de la table Project pour les projets gérés dans l'application
CREATE TABLE IF NOT EXISTS Project (
    id INT AUTO_INCREMENT PRIMARY KEY,              -- Identifiant unique du projet
    name VARCHAR(255) NOT NULL,                     -- Nom du projet
    description TEXT,                               -- Description détaillée du projet
    start_date DATE NOT NULL,                       -- Date de début du projet
    end_date DATE,                                  -- Date de fin (peut être NULL si non terminée)
    status ENUM('In preparation', 'In progress', 'Paused', 'Completed', 'Canceled') NOT NULL -- Statut actuel du projet
);

-- Création de la table Task pour les tâches liées à chaque projet
CREATE TABLE IF NOT EXISTS Task (
    id INT AUTO_INCREMENT PRIMARY KEY,          -- Identifiant unique de la tâche
    title VARCHAR(255) NOT NULL,                -- Titre de la tâche
    description TEXT,                           -- Description détaillée de la tâche
    priority ENUM('Low', 'Medium', 'High') NOT NULL, -- Priorité de la tâche
    status ENUM('To Do', 'Doing', 'Done') NOT NULL,  -- Statut de la tâche
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Date de création, par défaut la date actuelle
    due_date DATE NOT NULL,                     -- Date limite pour accomplir la tâche
    project_id INT,                             -- Référence au projet auquel la tâche est associée
    member_id INT,                              -- Référence au membre assigné à la tâche
    CONSTRAINT fk_projet FOREIGN KEY (project_id) REFERENCES Project(id) ON DELETE CASCADE ON UPDATE CASCADE, -- Clé étrangère liée au projet
    CONSTRAINT fk_membre FOREIGN KEY (member_id) REFERENCES Member(id) ON DELETE SET NULL ON UPDATE CASCADE  -- Clé étrangère liée au membre, mise à NULL si le membre est supprimé
);

-- Table pour relier les membres aux équipes (relation plusieurs-à-plusieurs)
CREATE TABLE IF NOT EXISTS Member_Team (
    member_id INT,                              -- Référence à l'identifiant du membre
    team_id INT,                                -- Référence à l'identifiant de l'équipe
    CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES Member(id) ON DELETE CASCADE ON UPDATE CASCADE, -- Clé étrangère membre
    CONSTRAINT fk_team FOREIGN KEY (team_id) REFERENCES Team(id) ON DELETE CASCADE ON UPDATE CASCADE,       -- Clé étrangère équipe
    PRIMARY KEY (member_id, team_id)            -- Clé primaire composite pour la relation
);

-- Table pour relier les projets aux équipes (relation plusieurs-à-plusieurs)
CREATE TABLE IF NOT EXISTS Project_Team (
    project_id INT,                             -- Référence à l'identifiant du projet
    team_id INT,                                -- Référence à l'identifiant de l'équipe
    CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES Project(id) ON DELETE CASCADE ON UPDATE CASCADE, -- Clé étrangère projet
    CONSTRAINT fk_team_project FOREIGN KEY (team_id) REFERENCES Team(id) ON DELETE CASCADE ON UPDATE CASCADE,  -- Clé étrangère équipe
    PRIMARY KEY (project_id, team_id)           -- Clé primaire composite pour la relation
);

-- Index sur le nom du projet pour améliorer les performances des requêtes
CREATE INDEX idx_project_name ON Project(name);

-- Attribution des privilèges à l'utilisateur 'web_user' pour interagir avec la base de données
GRANT SELECT, INSERT, UPDATE, DELETE ON dataware_db.* TO 'web_user'@'localhost' IDENTIFIED BY 'password';
