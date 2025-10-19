/**
 * La classe EvenementDeplacement représente un événement de déplacement pour un robot.
 * Cet événement déplace un robot vers une case cible à une date spécifiée, si le terrain est praticable.
 */

public class EvenementDeplacement extends Evenement {
    private Robot robot;
    private Case destination;

    public EvenementDeplacement(long date, Robot robot, Case destination) {
        super(date);  // Appel du constructeur de Evenement
        this.robot = robot;
        this.destination = destination;
    }

    @Override
    public void execute() {
        if (robot.peutSeDeplacerVers(destination)) {
            robot.setPosition(destination);
            System.out.println("Robot " + robot.getIdentifiant() + " déplacé à la case "
                    + destination.getLigne() + ", " + destination.getColonne());
        } else {
            System.out.println("Déplacement impossible pour le robot " + robot.getIdentifiant()
                    + " : terrain non praticable.");
        }
    }
}
