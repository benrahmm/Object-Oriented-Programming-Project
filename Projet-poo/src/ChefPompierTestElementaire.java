import java.util.List;
import java.util.ArrayList;

/**
 * La classe ChefPompierTestElementaire teste la stratégie élémentaire de la classe ChefPompier.
 * Elle charge les données de simulation à partir d'un fichier de carte, configure un simulateur,
 * et vérifie que la stratégie élémentaire affecte correctement les robots aux incendies.
 */

public class ChefPompierTestElementaire {

    /**
     * Point d'entrée principal pour exécuter le test de la stratégie élémentaire.
     *
     * @param args Les arguments de la ligne de commande. Le premier argument doit être le chemin
     *             vers le fichier de carte à utiliser pour la simulation.
     */

    public static void main(String[] args) {

        String fichierCarte = args[0];

        try {
            DonneesSimulation donnees = LecteurDonnees.creeDonnees(fichierCarte);
            Simulateur simulateur =  new Simulateur(donnees, fichierCarte);
            simulateur.setTypeTest("Elementaire");
            ChefPompier chefPompier = new ChefPompier(donnees);
            simulateur.setChefPompier(chefPompier);

            // Exécution des tests
            testStrategieElementaire(chefPompier, donnees);
                        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Teste la stratégie élémentaire en s'assurant que tous les robots disponibles sont affectés
     * aux incendies actifs.
     *
     * @param chefPompier L'instance de ChefPompier contenant la stratégie à tester.
     * @param donnees Les données de simulation utilisées pour vérifier les affectations.
     */

    private static void testStrategieElementaire(ChefPompier chefPompier, DonneesSimulation donnees) {
        System.out.println("Test de la stratégie élémentaire...");
        
        chefPompier.strategieElementaire();

        boolean allAssigned = true;
        for (Robot robot : donnees.getRobots()) {
            if (!robot.enIntervention() || robot.incendieEnCours == null) {
                allAssigned = false;
                break;
            }
        }

    }
}
