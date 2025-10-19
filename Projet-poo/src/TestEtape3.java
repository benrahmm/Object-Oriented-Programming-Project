/**
 * Classe TestEtape3.
 *
 * Cette classe teste l'étape 3 du projet, qui implémente le calcul et l'exécution
 * du plus court chemin pour un robot afin de se déplacer vers une case spécifique.
 *
 * Le test prend en entrée un fichier de carte et effectue les actions suivantes :
 * <ul>
 *   <li>Charge les données de simulation à partir du fichier de carte.</li>
 *   <li>Initialise un simulateur avec la carte chargée.</li>
 *   <li>Déplace un robot spécifique (identifié par son ID) vers une case de destination donnée.</li>
 * </ul>
 *
 * Usage :
 * <pre>
 * java TestEtape3 <fichierCarte>
 * </pre>
 */

public class TestEtape3 {
    public static void main(String[] args) {

        String fichierCarte = args[0];

        try {
            DonneesSimulation donnees = LecteurDonnees.creeDonnees(fichierCarte);
            Simulateur simulateur = new Simulateur(donnees, fichierCarte);
            simulateur.setTypeTest("Etape3");
            Carte carte = donnees.getCarte();

            // Utiliser un identifiant unique, par exemple "PATTES0"
            Robot robot = donnees.getRobotById("PATTES0");  // Exemple avec un robot de type PATTES

            // Définir la destination
            Case destination = carte.getCase(1, 5);

            // Déplacer le robot vers la destination avec une date de départ = 0
            if (robot != null) {
                robot.deplacerVers(destination, 0);
            } else {
                System.out.println("Erreur : Robot avec identifiant PATTES0 introuvable");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
