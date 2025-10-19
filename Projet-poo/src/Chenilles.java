/**
 * La classe Chenilles représente un robot équipé de chenilles, capable de se déplacer sur
 * certains types de terrain (hors eau et roche) et d'éteindre des incendies en consommant de l'eau.
 * Ce robot possède un réservoir d'eau limité et une vitesse adaptée à chaque type de terrain.
 */

public class Chenilles extends Robot {
    private static final int CAPACITE_MAX = 2000;
    private static final int VOLUME_INTERVENTION = 100; // Intervention unitaire

    public Chenilles(Case position, int vitesse,String identifiant) {
        super(position, vitesse > 0 ? Math.min(vitesse, 80) : 60,identifiant);
        this.reservoir = CAPACITE_MAX;
    }

    @Override
    public long tempsRemplissage() {
        return 5 * 60;
    }

    @Override
    public double getVitesse(NatureTerrain nature) {
        if (nature == NatureTerrain.FORET) {
            return convertirVitesseEnMS(vitesseKmH * 0.5); // Réduction de 50% en forêt
        }
        return convertirVitesseEnMS(vitesseKmH);
    }
    
    @Override
    public double calculerTemps(Carte carte, Case destination) {
        NatureTerrain nature = destination.getNature();
        double vitesseMps = getVitesse(nature); // vitesse en m/s adaptée au terrain
        double distanceMetres = carte.getTailleCases();
        return distanceMetres / vitesseMps; // temps en secondes
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
        NatureTerrain nature = destination.getNature();
        // Le robot à chenilles peut se déplacer sur toutes les cases sauf l'eau et le rocher
        return nature != NatureTerrain.EAU && nature != NatureTerrain.ROCHE;
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
