# Projet Java UML : Chat System
Par Walid EL-ASSIMY et Ghizlane BADAOUI, 4A IR

## Structure du code :
Le code est organisé selon l'architecture MVC :
- `Modèle` : contient la classe User.
- `Vue` : contient les deux contrôleurs des deux interfaces graphiques.
- `Contrôleur` : est divisé en 3 packets :
  - `Database` : pour tout échange ou accès à la base de données.
  - `Protocoles` : contient la classe Broadcast.
  - `Threads` : contient les classes extends de Thread. 

Autres éléments :
  - `_Main` : est la classe main du projet qui fait appel à Main.
  - `Main` : est aussi la classe main du projet, mais elle extends de Application.
  - `Rapport.pdf` : est le rapport du projet pour la partie modélisation et conception (dans `/rendu`).
  - `/rendu/diagrams` : contient les diagrams réalisés.
  - `/rendu/jar` : contient les fichiers .jar utilisés pour ce projet.
  - `/out/artifacts/ChatSystem_jar` : contient le fichier .jar à lancer accompagné d'une base de données initialisée.
  - `/src/test/java` : contient les tests unitaires.
  - `/src/main/ressources` : contient le fichier CSS et les images utilisés pour les interfaces graphiques et aussi les fichiers .fxml.
  - `maven.yml` : pour la partie déploiement et vérification des tests unitaires (dans `.github/workflows`).

## Exécution:
Pour lancer l'application en tant qu'utilisateur :
- Faire un `double clic` sur ChatSystem.jar qui se trouve dans `/out/artifacts/ChatSystem_jar` (un JDK 18 est requis), ou
- Ouvrir un terminal depuis `/out/artifacts/ChatSystem_jar` et exécuter la commande suivante : `java -jar ChatSystem.jar`

Pour lancer l'application depuis un terminal :
- Exécuter la classe `_Main` de `src/main/java/com/example/chatsystem`
  (la classe _Main fait appel à la classe Main de même packet, on l'a utilisé pour générer le .jar, puisqu'il est impossible de le faire avec Main qui extends de Application)