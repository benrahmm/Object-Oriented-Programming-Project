/**
 * La classe Drone représente un robot volant capable de se déplacer sur toutes les cases
 * sans restriction de terrain. Il possède un grand réservoir d'eau pour éteindre les incendies
 * et une vitesse élevée.
 */

public class Drone extends Robot {
    private static final int CAPACITE_MAX = 10000; // capacité du réservoir en litres
    private static final int VOLUME_INTERVENTION = 10000; // Intervention unitaire


    public Drone(Case position, int vitesse,String identifiant) {
        super(position, vitesse > 0 ? Math.min(vitesse, 150) : 100, identifiant);
        this.reservoir = CAPACITE_MAX;
    }

    @Override
    public long tempsRemplissage() {
        return 30 * 60;
    }


    @Override
    public double getVitesse(NatureTerrain nature) {
        return convertirVitesseEnMS(vitesseKmH); // Vitesse normale sinon
    }

    @Override
    public double calculerTemps(Carte carte, Case destination) {
        double vitesseMps = convertirVitesseEnMS(this.vitesseKmH); // vitesse uniforme pour le drone en m/s
        double distanceMetres = carte.getTailleCases();

        return distanceMetres / vitesseMps;
    }

    @Override
    public void intervenir(Case position) {
        Incendie incendie = position.getIncendie();

        if (incendie == null || incendie.getIntensite() <= 0) {
            System.out.println("Aucun incendie à éteindre ici.");
            return;
        }

        int totalEauUtilisee = 0;  // Compteur pour l'eau utilisée

        // Boucle pour continuer l'intervention jusqu'à extinction ou réservoir vide
        while (incendie.getIntensite() > 0 && reservoir > 0) {
            int eauUtilisee = Math.min(VOLUME_INTERVENTION, incendie.getIntensite());
            reservoir -= eauUtilisee;
            incendie.diminueIntensite(eauUtilisee);
            totalEauUtilisee += eauUtilisee;
        }

        // Affichage d'un seul message avec le total de litres utilisés
        System.out.println("Intervention réussie : incendie réduit de " + totalEauUtilisee + " litres au total.");

        // Vérification si l'incendie est éteint et mise à jour de la carte
        if (incendie.getIntensite() <= 0) {
            System.out.println("Incendie éteint");
            this.enIntervention = false;
        } else if (reservoir <= 0) {
            System.out.println("Intervention arrêtée : réservoir vide.");
        }
    }

    @Override
    public boolean peutSeDeplacerVers(Case destination) {
        // Le drone peut se déplacer sur toutes les cases, quelle que soit leur nature
        return true;
    }


    @Override
    public void remplirReservoir() {
        this.reservoir = CAPACITE_MAX;
    }

    @Override
    public void deverserEau(int volume) {
        this.reservoir = Math.max(0, this.reservoir - volume);
    }
}
