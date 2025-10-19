/**
 * La classe EvenementRemplissage représente un événement de remplissage du réservoir d'un robot.
 * Cet événement permet de recharger complètement le réservoir d'eau d'un robot à une date spécifiée.
 */

public class EvenementRemplissage extends Evenement {
    private Robot robot;

    public EvenementRemplissage(long date, Robot robot) {
        super(date);
        this.robot = robot;
    }

    @Override
    public void execute() {
        robot.remplirReservoir();
        System.out.println("Remplissage du réservoir du robot " + robot.getIdentifiant());
    }

    // Méthode pour obtenir le robot associé à cet événement
    public Robot getRobot() {
        return this.robot;
    }
}
