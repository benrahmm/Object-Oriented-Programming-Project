/**
 * La classe Incendie représente un incendie sur une carte, avec une position et une intensité.
 * Un incendie peut être réduit ou éteint grâce à l'intervention d'un robot.
 */
public class Incendie {
    private Case position;
    private int intensite;

    public Incendie(Case position, int intensite) {
        this.position = position;
        this.intensite = intensite;
    }

    public Case getPosition() {
        return position;
    }

    public int getIntensite() {
        return intensite;
    }

    public void diminueIntensite(int volume) {
        intensite -= volume;
        if (intensite < 0) {
            intensite = 0;
        }
    }
}
