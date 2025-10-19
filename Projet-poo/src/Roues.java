/**
 * Classe représentant un robot à roues.
 *
 * Le robot à roues peut se déplacer uniquement sur des terrains de type TERRAIN_LIBRE
 * ou HABITAT. Il dispose d'une vitesse constante sur ces terrains et peut intervenir
 * sur des incendies en déversant de l'eau. La capacité de son réservoir est limitée
 * à 5000 litres.
 */

public class Roues extends Robot {
    private static final int CAPACITE_MAX = 5000;
    private static final int VOLUME_INTERVENTION = 100; // Intervention unitaire

    public Roues(Case position, int vitesse,String identifiant) {
        super(position, vitesse > 0 ? vitesse : 80, identifiant);
        this.reservoir = CAPACITE_MAX;
    }

    @Override
    public long tempsRemplissage() {
        return 10 * 60;
    }


    @Override
    public double getVitesse(NatureTerrain nature) {
        // Robot à roues a une vitesse constante sur terrain libre ou habitat
        if (nature == NatureTerrain.TERRAIN_LIBRE || nature == NatureTerrain.HABITAT) {
            return convertirVitesseEnMS(vitesseKmH);
        }
        return 0; // Vitesse nulle si le terrain n'est pas accessible
    }

    @Override
    public double calculerTemps(Carte carte, Case destination) {
        NatureTerrain nature = destination.getNature();
        double vitesseMps = getVitesse(nature); // vitesse en m/s
        if (vitesseMps == 0) {
            return Double.POSITIVE_INFINITY; // Inaccessible, donc temps infini
        }
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
        NatureTerrain nature = destination.getNature();
        // Le robot à roues peut se déplacer uniquement sur du terrain libre ou habitat
        return nature == NatureTerrain.TERRAIN_LIBRE || nature == NatureTerrain.HABITAT;
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
