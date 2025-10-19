/**
 * La classe EvenementIntervention représente un événement d'intervention pour un robot.
 * Cet événement est utilisé pour gérer les actions d'un robot visant à éteindre un incendie
 * sur une case donnée à une date spécifiée.
 */

public class EvenementIntervention extends Evenement {
    private Robot robot;
    private Case position;

    public EvenementIntervention(long date, Robot robot, Case position) {
        super(date);
        this.robot = robot;
        this.position = position;
    }

    @Override
    public void execute() {
        robot.verifierIntervention();
        System.out.println("Intervention du robot " + robot.getIdentifiant() + " sur la case "
                + position.getLigne() + ", " + position.getColonne());
    }

    // Méthode pour obtenir le robot associé à cet événement
    public Robot getRobot() {
        return this.robot;
    }
}
