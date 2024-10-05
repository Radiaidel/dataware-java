# Application de Gestion de Projets, Tâches et Équipes

## Description du projet
Cette application web permet de gérer des projets, des tâches et des équipes de manière collaborative. Les utilisateurs peuvent gérer des projets, assigner des équipes aux projets, assigner des membres aux équipes et bien sûre assigner des tâches aux membres des équipes, suivre l’avancement des tâches, et bien plus encore. L'application est développée en Java 8 avec des technologies web modernes tout en respectant une architecture MVC stricte.

## Objectif général de l'application
L'objectif de cette application est de fournir un outil de gestion de projets permettant aux équipes de suivre leurs tâches, gérer les équipes, et visualiser les statistiques de projets. Cela inclut la possibilité d'ajouter, modifier et supprimer des projets, des tâches et des équipes, ainsi que de suivre l'avancement des projets et des tâches grâce à des statuts et des priorités.

## Technologies utilisées
- **Langage**: Java 8
- **Serveur d'applications**: Apache Tomcat
- **Frameworks**: Servlets, JSP, JSTL
- **Frontend**: HTML5, CSS3, Bootstrap 5
- **Gestion de dépendances**: Maven
- **Base de données**: MySQL avec JDBC
- **Tests unitaires**: JUnit
- **Gestion de projet**: JIRA avec la méthode Scrum
- **Version Control**: Git
- **Maquettage**: FIGMA
- **Système de Logging**: LOGGER
- **Configuration**: XML (web.xml)

## Structure du projet
Le projet est organisé suivant une architecture MVC stricte et en couches, avec les principales sections suivantes :
1. **Webapp** : JSP, et Bootstrap pour l'interface utilisateur.
2. **Model** : Classes Java représentant les entités (Projet, Tâche, Équipe, Membre) avec leurs attributs.
3. **Contrôleur** : Servlets pour gérer les requêtes et réponses HTTP, traitant les interactions utilisateur.
4. **Service** : Contient la logique métier et coordonne les opérations entre le contrôleur et la couche Repository.
5. **Repository** : Gère l'accès aux données, en effectuant des opérations CRUD sur la base de données.
6. **Util** : Classes de validation et autres fonctions réutilisables.
7. **Database** : Classe pour gérer la connexion avec la base données utilisant le design pattern Singleton.

## Description brève de l'architecture adoptée
L'application adopte une architecture MVC (Modèle-Vue-Contrôleur), avec une séparation claire entre les différentes couches :
- **Modèle** : Représente les entités du domaine métier telles que Projet, Tâche, Membre, et Équipe.
- **Vue** : Composée de JSP utilisant JSTL pour afficher dynamiquement les données.
- **Contrôleur** : Implémenté via des Servlets qui interceptent les requêtes HTTP et appellent les services appropriés.
- **Repository** : Représente la couche d'accès aux données et utilise JDBC pour interagir avec MySQL.
  

## Instructions d'installation et d'utilisation

### Prérequis
- **JDK 8** (Java 8)
- **Apache Tomcat 9** ou inférieur (compatible avec java 8)
- **MySQL** avec une base de données existante
- **Maven** pour la gestion des dépendances
- **Git** pour le contrôle de version

### Étapes pour configurer la base de données
1. Créez une base de données MySQL en exécutant les scripts SQL fournis dans le dossier `SQL/script.sql`.
2. Assurez-vous que la configuration de connexion à la base de données dans le fichier `application.properties` (situé dans le dossier `resources`) est correcte :
   ```properties
   db.url=jdbc:mysql://localhost:3306/nom_de_la_base_de_données
   db.username=votre_utilisateur
   db.password=votre_mot_de_passe
   
   
   
## Comment lancer l'application sur Tomcat
Clonez le projet à partir du dépôt Git :

git clone https://github.com/JavaAura/Talemsi-Sebti-Idelkadi-B1-S2

### Naviguez dans le répertoire du projet :

cd dataware-java

### Compilez et packagez le projet avec Maven :

mvn clean package

### Déploiement
 Déployez le fichier WAR généré (situé dans target/) sur le serveur Tomcat via l'interface d'administration ou manuellement en plaçant le fichier dans le dossier webapps de Tomcat.

###Démarrez Tomcat et accédez à l'application à l'adresse :

http://localhost:8080/dataware-java

## Captures d'écran

### Page de gestion des projets
![Gestion des projets](screenshots/projets.png)

### Page de gestion des tâches
![Gestion des tâches](screenshots/taches.png)

### Page de gestion des équipes
![Gestion des équipes](screenshots/equipes.png)

## Améliorations futures possibles
Intégration d'une fonctionnalité de commentaires pour chaque tâche.
Amélioration du tableau de bord de statistiques avec des graphiques.
Possibilité de glisser-déposer les tâches pour changer leur statut.
Ajout d'une API REST pour permettre l'intégration avec d'autres outils.
Gestion avancée des rôles et des permissions pour les membres de l'équipe.

## Auteurs et contact

Nom: [Idelkadi Radia]
GitHub: https://github.com/Radiaidel

-----------------------------------

Nom: [Talemsi Abdellah]
GitHub: https://github.com/ATalemsi

-----------------------------------
Nom: [Sebti Douae]
GitHub: https://github.com/Douaesb


