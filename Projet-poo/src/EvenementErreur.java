/**
 * La classe EvenementErreur représente un événement d'affichage d'une erreur.
 * Cet événement est utilisé pour notifier des erreurs ou des problèmes survenus
 * à une date donnée lors de la simulation.
 */

public class EvenementErreur extends Evenement {
    private String message;

    public EvenementErreur(long date, String message) {
        super(date);
        this.message = message;
    }

    @Override
    public void execute() {
        System.out.println(message);
    }
}
