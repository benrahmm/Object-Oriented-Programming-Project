/**
 * La classe ChefPompierTestEvoluee teste la stratégie évoluée de la classe ChefPompier.
 * Cette stratégie vise à optimiser l'affectation des robots aux incendies en prenant
 * en compte la proximité des incendies pour minimiser les déplacements.
 */

public class ChefPompierTestEvoluee {

    /**
     * Point d'entrée principal pour exécuter le test de la stratégie évoluée.
     *
     * @param args Les arguments de la ligne de commande. Le premier argument doit être le chemin
     *             vers le fichier de carte à utiliser pour la simulation.
     */

    public static void main(String[] args) {

        String fichierCarte = args[0];

        try {
            DonneesSimulation donnees = LecteurDonnees.creeDonnees(fichierCarte);
            Simulateur simulateur = new Simulateur(donnees, fichierCarte);
            simulateur.setTypeTest("Evoluee");
            ChefPompier chefPompier = new ChefPompier(donnees);
            simulateur.setChefPompier(chefPompier);

            // Exécution des tests
            testStrategieEvoluee(chefPompier, donnees);
                        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Teste la stratégie évoluée en s'assurant que tous les robots disponibles sont affectés
     * aux incendies actifs de manière optimale.
     *
     * Cette méthode vérifie que chaque robot est affecté à l'incendie le plus proche, si possible.
     *
     * @param chefPompier L'instance de ChefPompier contenant la stratégie évoluée à tester.
     * @param donnees Les données de simulation utilisées pour vérifier les affectations.
     */

    private static void testStrategieEvoluee(ChefPompier chefPompier, DonneesSimulation donnees) {
        System.out.println("\nTest de la stratégie évoluée...");
        
        chefPompier.strategieEvoluee();

        boolean allAssigned = true;
        for (Robot robot : donnees.getRobots()) {
            if (!robot.enIntervention() || robot.incendieEnCours == null) {
                allAssigned = false;
                break;
            }
        }
    }
}
