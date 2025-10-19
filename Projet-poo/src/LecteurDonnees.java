import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;



/**
 * Lecteur de cartes au format spécifié dans le sujet.
 * Cette classe est modifiée pour créer et retourner un objet
 * DonneesSimulation à partir d'un fichier.
 */
public class LecteurDonnees {

    // Scanner pour lire les données
    private static Scanner scanner;

    /**
     * Crée une instance de DonneesSimulation à partir d'un fichier de données.
     * @param fichierDonnees nom du fichier à lire
     * @return DonneesSimulation l'objet contenant toutes les données lues
     * @throws FileNotFoundException si le fichier est introuvable
     * @throws DataFormatException si le format du fichier est incorrect
     */
    public static DonneesSimulation creeDonnees(String fichierDonnees)
            throws FileNotFoundException, DataFormatException {
        LecteurDonnees lecteur = new LecteurDonnees(fichierDonnees);
        Carte carte = lecteur.lireCarteEtCreer();
        DonneesSimulation donnees = new DonneesSimulation(carte);

        lecteur.lireIncendiesEtAjouter(donnees);
        donnees.associerIncendiesAuxCases();
        lecteur.lireRobotsEtAjouter(donnees);
        scanner.close();
        return donnees;
    }

    public static void lire(String fichierDonnees)
        throws FileNotFoundException, DataFormatException {
        System.out.println("\n == Lecture du fichier" + fichierDonnees);
        LecteurDonnees lecteur = new LecteurDonnees(fichierDonnees);
        lecteur.lireCarte();
        lecteur.lireIncendies();
        lecteur.lireRobots();
        scanner.close();
        System.out.println("\n == Lecture terminee");
    }

    // --- Méthodes existantes de LecteurDonnees ---
    private LecteurDonnees(String fichierDonnees) throws FileNotFoundException {
        scanner = new Scanner(new File(fichierDonnees));
        scanner.useLocale(Locale.US);
    }

    // Méthode pour lire la carte et créer l'objet Carte
    private Carte lireCarteEtCreer() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();
            int tailleCases = scanner.nextInt();

            System.out.println("Carte " + nbLignes + "x" + nbColonnes + ", taille des cases = " + tailleCases);

            Carte carte = new Carte(nbLignes, nbColonnes, tailleCases);

            for (int lig = 0; lig < nbLignes; lig++) {
                for (int col = 0; col < nbColonnes; col++) {
                    ignorerCommentaires();
                    String natureStr = scanner.next();
                    System.out.println("Case (" + lig + "," + col + ") : " + natureStr);
                    NatureTerrain nature = NatureTerrain.valueOf(natureStr);
                    carte.ajouterCase(lig, col, nature);
                }
            }
            return carte;

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide pour la carte.");
        }
    }



    // Méthode pour lire les incendies et les ajouter dans DonneesSimulation
    private void lireIncendiesEtAjouter(DonneesSimulation donnees) throws DataFormatException {
        ignorerCommentaires();
        int nbIncendies = scanner.nextInt();
        System.out.println("Nb d'incendies = " + nbIncendies);

        for (int i = 0; i < nbIncendies; i++) {
            ignorerCommentaires();
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            System.out.println("Incendie " + i + " : position (" + lig + "," + col + "), intensité = " + intensite);

            Case caseIncendie = donnees.getCarte().getCase(lig, col);
            Incendie incendie = new Incendie(caseIncendie, intensite);
            donnees.ajouterIncendie(incendie);
        }
    }


    // Méthode pour lire les robots et les ajouter dans DonneesSimulation
    private void lireRobotsEtAjouter(DonneesSimulation donnees) throws DataFormatException {
        ignorerCommentaires();
        int nbRobots;
        int droneCounter = 0;
        int rouesCounter = 0;
        int chenillesCounter = 0;
        int pattesCounter = 0;

        try {
            nbRobots = scanner.nextInt();
            scanner.nextLine(); // Passer à la ligne suivante après le nombre de robots
            System.out.println("Nb de robots = " + nbRobots);

            for (int i = 0; i < nbRobots; i++) {
                ignorerCommentaires();

                // Lire la ligne complète pour le robot
                String ligne = scanner.nextLine().trim();
                String[] elements = ligne.split("\\s+");  // Diviser la ligne par les espaces

                if (elements.length < 3 || elements.length > 4) {
                    throw new DataFormatException("Erreur de format : données manquantes ou en trop pour un robot.");
                }

                // Lecture des coordonnées et du type
                int lig = Integer.parseInt(elements[0]);
                int col = Integer.parseInt(elements[1]);
                String type = elements[2];
                System.out.println("Lecture du robot de type " + type + " à la position (" + lig + "," + col + ")");

                // Initialiser la vitesse selon la présence du 4ème élément
                int vitesse;
                if (elements.length == 4) {
                    // Si une vitesse est spécifiée, la lire
                    vitesse = Integer.parseInt(elements[3]);
                    System.out.println("Vitesse lue : " + vitesse);
                } else {
                    // Sinon, utiliser la vitesse par défaut pour les types qui en nécessitent une
                    vitesse = Robot.getDefaultSpeed(type);
                    System.out.println("Aucune vitesse spécifiée; vitesse par défaut appliquée : " + vitesse);
                }

                // Déterminer un identifiant unique pour chaque robot de ce type
                String identifiant;
                switch (type) {
                    case "DRONE":
                        identifiant = "DRONE" + droneCounter++;
                        break;
                    case "ROUES":
                        identifiant = "ROUES" + rouesCounter++;
                        break;
                    case "CHENILLES":
                        identifiant = "CHENILLES" + chenillesCounter++;
                        break;
                    case "PATTES":
                        identifiant = "PATTES" + pattesCounter++;
                        break;
                    default:
                        throw new DataFormatException("Type de robot inconnu : " + type);
                }

                // Création du robot selon le type avec l'identifiant
                Case position = donnees.getCarte().getCase(lig, col);
                Robot robot;
                switch (type) {
                    case "DRONE":
                        robot = new Drone(position, vitesse, identifiant);
                        break;
                    case "ROUES":
                        robot = new Roues(position, vitesse, identifiant);
                        break;
                    case "CHENILLES":
                        robot = new Chenilles(position, vitesse, identifiant);
                        break;
                    case "PATTES":
                        robot = new Pattes(position, identifiant);  // Pas de vitesse pour PATTES
                        break;
                    default:
                        throw new DataFormatException("Type de robot inconnu : " + type);
                }

                robot.setCarte(donnees.getCarte());
                donnees.ajouterRobot(robot);
                System.out.println("Robot ajouté avec succès : " + type + " avec identifiant " + identifiant);
            }
        } catch (InputMismatchException e) {
            throw new DataFormatException("Erreur de format : données manquantes ou invalides pour un robot.");
        } catch (NoSuchElementException e) {
            throw new DataFormatException("Erreur de format : données manquantes ou invalides pour un robot.");
        }
    }

    /**
     * Lit et affiche les donnees de la carte.
     * @throws ExceptionFormatDonnees
     */
    private void lireCarte() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();
            int tailleCases = scanner.nextInt();	// en m
            System.out.println("Carte " + nbLignes + "x" + nbColonnes
                    + "; taille des cases = " + tailleCases);

            for (int lig = 0; lig < nbLignes; lig++) {
                for (int col = 0; col < nbColonnes; col++) {
                    lireCase(lig, col);
                }
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbLignes nbColonnes tailleCases");
        }
        // une ExceptionFormat levee depuis lireCase est remontee telle quelle
    }




    /**
     * Lit et affiche les donnees d'une case.
     */
    private void lireCase(int lig, int col) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Case (" + lig + "," + col + "): ");
        String chaineNature = new String();
        //		NatureTerrain nature;

        try {
            chaineNature = scanner.next();
            // si NatureTerrain est un Enum, vous pouvez recuperer la valeur
            // de l'enum a partir d'une String avec:
            //			NatureTerrain nature = NatureTerrain.valueOf(chaineNature);

            verifieLigneTerminee();

            System.out.print("nature = " + chaineNature);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de case invalide. "
                    + "Attendu: nature altitude [valeur_specifique]");
        }

        System.out.println();
    }


    /**
     * Lit et affiche les donnees des incendies.
     */
    private void lireIncendies() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbIncendies = scanner.nextInt();
            System.out.println("Nb d'incendies = " + nbIncendies);
            for (int i = 0; i < nbIncendies; i++) {
                lireIncendie(i);
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbIncendies");
        }
    }


    /**
     * Lit et affiche les donnees du i-eme incendie.
     * @param i
     */
    private void lireIncendie(int i) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Incendie " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            if (intensite <= 0) {
                throw new DataFormatException("incendie " + i
                        + "nb litres pour eteindre doit etre > 0");
            }
            verifieLigneTerminee();

            System.out.println("position = (" + lig + "," + col
                    + ");\t intensite = " + intensite);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        }
    }


    /**
     * Lit et affiche les donnees des robots.
     */
    private void lireRobots() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbRobots = scanner.nextInt();
            System.out.println("Nb de robots = " + nbRobots);
            for (int i = 0; i < nbRobots; i++) {
                lireRobot(i);
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbRobots");
        }
    }


    /**
     * Lit et affiche les donnees du i-eme robot.
     * @param i
     */
    private void lireRobot(int i) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Robot " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            System.out.print("position = (" + lig + "," + col + ");");
            String type = scanner.next();

            System.out.print("\t type = " + type);


            // lecture eventuelle d'une vitesse du robot (entier)
            System.out.print("; \t vitesse = ");
            String s = scanner.findInLine("(\\d+)");	// 1 or more digit(s) ?
            // pour lire un flottant:    ("(\\d+(\\.\\d+)?)");

            if (s == null) {
                System.out.print("valeur par defaut");
            } else {
                int vitesse = Integer.parseInt(s);
                System.out.print(vitesse);
            }
            verifieLigneTerminee();

            System.out.println();

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de robot invalide. "
                    + "Attendu: ligne colonne type [valeur_specifique]");
        }
    }

    // --- Méthodes d'aide pour ignorer les commentaires et vérifier les lignes ---

    /** Ignore toute (fin de) ligne commencant par '#' */
    private void ignorerCommentaires() {
        while (scanner.hasNext("#.*")) {
            scanner.nextLine();
        }
    }

    /** Vérifie qu'il n'y a plus rien à lire sur cette ligne (int ou float). */
    private void verifieLigneTerminee() throws DataFormatException {
        if (scanner.findInLine("(\\d+)") != null) {
            throw new DataFormatException("Format invalide, données en trop.");
        }
    }
}
