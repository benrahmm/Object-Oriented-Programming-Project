import gui.GUISimulator;
import java.awt.Color;

/**
 * Classe TestEtape2.
 *
 * Cette classe permet de tester l'étape 2 du projet, qui consiste à charger une carte
 * et un scénario optionnel, puis à initialiser une simulation graphique avec les robots
 * et les incendies positionnés.
 *
 * Le test prend en entrée un fichier de carte et, éventuellement, un fichier de scénario.
 *
 * Usage :
 * <pre>
 * java TestEtape2 <fichierCarte> [<fichierScenario>]
 * </pre>
 */

public class TestEtape2 {

    /**
     * Point d'entrée du programme.
     *
     * @param args Les arguments de la ligne de commande :
     * <ul>
     *   <li><code>args[0]</code> : Chemin vers le fichier de carte.</li>
     *   <li><code>args[1]</code> (optionnel) : Chemin vers le fichier de scénario.</li>
     * </ul>
     */

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage : java Main <fichierCarte> <fichierScenario>");
            return;
        }

        String fichierCarte = args[0];
        String fichierScenario = args.length > 1 ? args[1] : null;

        try {
            DonneesSimulation donnees = LecteurDonnees.creeDonnees(fichierCarte);
            Simulateur simulateur = new Simulateur(donnees, fichierCarte, fichierScenario);
            simulateur.setTypeTest("Etape2");

            if (fichierScenario != null) {
                simulateur.chargeScenario(fichierScenario);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des données : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
