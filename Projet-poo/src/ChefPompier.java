import java.util.List;
import java.util.ArrayList;

/**
 * La classe ChefPompier représente une entité responsable de la gestion des robots pompiers.
 * Elle utilise différentes stratégies pour affecter les robots aux incendies et optimiser leurs déplacements et interventions.
 */

public class ChefPompier {
    /** Données de la simulation, contenant les informations sur les incendies, robots, et la carte */
    private DonneesSimulation donnees;

    /**
     * Constructeur de la classe ChefPompier.
     *
     * @param donnees Les données de simulation nécessaires pour gérer les robots et incendies.
     */

    public ChefPompier(DonneesSimulation donnees) {
        this.donnees = donnees;
    }

    /**
     * Stratégie élémentaire : affecte naïvement un robot disponible à chaque incendie actif.
     *
     * Cette stratégie parcourt la liste des incendies et tente de trouver un robot disponible
     * pouvant se déplacer vers l'incendie. Dès qu'un robot est affecté à un incendie, on passe au suivant.
     */

    public void strategieElementaire() {
        List<Incendie> incendies = donnees.getIncendies();
        
        for (Incendie incendie : incendies) {
            if (incendie.getIntensite() > 0) {
                for (Robot robot : donnees.getRobots()) {
                    if (robot.estDisponible() && robot.peutSeDeplacerVers(incendie.getPosition())) {
                        // Affecter le robot à l'incendie, ce qui programme le déplacement et l'intervention différée
                        robot.affecterIncendie(incendie);
                        break; // Passer au prochain incendie après avoir affecté un robot
                    }
                }
            }
        }
    }

    /**
     * Stratégie évoluée : affecte chaque robot disponible à l'incendie le plus proche et accessible.
     *
     * Cette stratégie améliore l'efficacité en minimisant les distances parcourues par les robots.
     * Elle utilise une liste pour suivre les incendies déjà assignés afin d'éviter des affectations multiples.
     */

    public void strategieEvoluee() {
        List<Incendie> incendies = donnees.getIncendies();
        List<Incendie> incendiesAssignes = new ArrayList<>();

        for (Robot robot : donnees.getRobots()) {
            if (robot.estDisponible()) {
                // Cherche l'incendie le plus proche et accessible
                Incendie incendieLePlusProche = robot.findNearestAccessibleIncendie(incendies);

                // Vérifie que l'incendie n'est pas déjà en cours de traitement
                if (incendieLePlusProche != null && !incendiesAssignes.contains(incendieLePlusProche)) {
                    // Affecte l'incendie trouvé au robot   
                    robot.affecterIncendie(incendieLePlusProche);

                    // Ajoute l'incendie à la liste des incendies traités
                    incendiesAssignes.add(incendieLePlusProche);
                }
            }
        }
    }
}
