# Compilation des tests et du simulateur
all: testInvader testLecture TestEtape2 TestEtape3 ChefPompierTestElementaire ChefPompierTestEvoluee

# Compilation des classes existantes
testInvader:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/TestInvader.java

testLecture:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/TestLecteurDonnees.java

ChefPompierTestElementaire:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/ChefPompier.java src/ChefPompierTestElementaire.java src/LecteurDonnees.java src/Carte.java src/Simulateur.java

ChefPompierTestEvoluee:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/ChefPompier.java src/ChefPompierTestEvoluee.java src/LecteurDonnees.java src/Carte.java src/Simulateur.java

TestEtape3:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/Simulateur.java src/TestEtape3.java src/LecteurDonnees.java src/Carte.java

# Compilation du simulateur avec TestEtape2
TestEtape2:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/Simulateur.java src/TestEtape2.java src/LecteurDonnees.java

# Exécution de TestInvader
exeInvader: 
	java -classpath bin:lib/gui.jar TestInvader

# Exécution de TestLecteurDonnees avec un fichier .map en argument
exeLecture: 
	java -classpath bin TestLecteurDonnees cartes/carteSujet.map

# Exécution du simulateur avec un fichier .map et un fichier de scénario
exeEtape2:
	@if [ -z "$(file)" ]; then \
		echo "Usage: make exeEtape2 file=cartes/nom_du_fichier.map scenario=scenarios/nom_du_fichier_scenario"; \
	else \
		java -classpath bin:lib/gui.jar TestEtape2 $(file) $(scenario); \
	fi

# Exécution de Etape3 avec un fichier .map en argument
exeEtape3:
	@if [ -z "$(file)" ]; then \
		echo "Usage: make exeEtape3 file=cartes/nom_du_fichier.map"; \
	else \
		java -classpath bin:lib/gui.jar TestEtape3 $(file); \
	fi

# Exécution de ChefPompierTest
exeChefPompierTestElementaire:
	@if [ -z "$(file)" ]; then \
		echo "Usage: make exeChefPompierTestElementaire file=cartes/nom_du_fichier.map"; \
	else \
		java -classpath bin:lib/gui.jar ChefPompierTestElementaire $(file); \
	fi

# Exécution de ChefPompierTest
exeChefPompierTestEvoluee:
	@if [ -z "$(file)" ]; then \
		echo "Usage: make exeChefPompierTestEvoluee file=cartes/nom_du_fichier.map"; \
	else \
		java -classpath bin:lib/gui.jar ChefPompierTestEvoluee $(file); \
	fi


# Nettoyage des fichiers compilés
clean:
	rm -rf bin/*
