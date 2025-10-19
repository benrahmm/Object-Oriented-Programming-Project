import java.util.ArrayList;
import java.util.List;

/**
 * La classe DonneesSimulation contient les données nécessaires pour exécuter une simulation.
 * Elle inclut la carte, les incendies, et les robots, et fournit des méthodes pour gérer ces éléments.
 */

public class DonneesSimulation {
    private Carte carte;
    private List<Incendie> incendies;
    private List<Robot> robots;

    public DonneesSimulation(Carte carte) {
        this.carte = carte;
        this.incendies = new ArrayList<>();
        this.robots = new ArrayList<>();
    }

    public Carte getCarte() {
        return carte;
    }

    public List<Incendie> getIncendies() {
        return incendies;
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public void ajouterIncendie(Incendie incendie) {
        this.incendies.add(incendie);
    }

    public void ajouterRobot(Robot robot) {
        this.robots.add(robot);
    }

    public Robot getRobotByType(String type) {
        for (Robot robot : robots) {
            if (robot.getClass().getSimpleName().equalsIgnoreCase(type)) {
                return robot;
            }
        }
        return null;  // Retourne null si aucun robot du type donné n'est trouvé
    }

    public Robot getRobotById(String identifiant) {
        for (Robot robot : this.robots) { // Supposons que robots est la liste des robots
            if (robot.getIdentifiant().equals(identifiant)) {
                return robot;
            }
        }
        return null; // Aucun robot trouvé avec cet identifiant
    }


    // Nouvelle méthode pour associer les incendies aux cases correspondantes
    public void associerIncendiesAuxCases() {
        for (Incendie incendie : incendies) {
            Case caseIncendie = incendie.getPosition();
            caseIncendie.setIncendie(incendie);  // Associe l'incendie à la case correspondante
        }
    }
}
