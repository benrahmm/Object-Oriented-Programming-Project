import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * La classe abstraite Evenement représente un événement dans le système de simulation.
 * Chaque événement est associé à une date à laquelle il doit être exécuté.
 * Les sous-classes doivent implémenter la méthode {@link #execute()} pour définir le comportement spécifique de l'événement.
 */

public abstract class Evenement {
    private long date;

    public Evenement(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public abstract void execute();

}
