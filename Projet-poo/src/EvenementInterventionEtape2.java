/**
 * La classe EvenementInterventionEtape2 représente un événement d'intervention pour un robot
 * dans le cadre de l'étape 2. Cet événement permet à un robot d'effectuer une intervention
 * spécifique sur une case donnée pour éteindre un incendie.
 */

public class EvenementInterventionEtape2 extends Evenement {
    private Robot robot;
    private Case position;

    public EvenementInterventionEtape2(long date, Robot robot, Case position) {
        super(date);
        this.robot = robot;
        this.position = position;
    }

    @Override
    public void execute() {
        robot.intervenir(position);
        System.out.println("Intervention du robot " + robot.getIdentifiant() + " sur la case "
                + position.getLigne() + ", " + position.getColonne());
    }

    // Méthode pour obtenir le robot associé à cet événement
    public Robot getRobot() {
        return this.robot;
    }
}
