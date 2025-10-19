/**
 * La classe Pattes représente un robot à pattes capable de se déplacer sur presque tous
 * les terrains, sauf les zones d'eau. Ce type de robot possède un réservoir d'eau infini,
 * éliminant ainsi le besoin de remplissage.
 */

public class Pattes extends Robot {
    private static final int CAPACITE_MAX = Integer.MAX_VALUE;
    private static final int VOLUME_INTERVENTION = 100; // Intervention unitaire

    public Pattes(Case position,String identifiant) {
        super(position, 30,identifiant); // Vitesse par défaut
        this.reservoir = Integer.MAX_VALUE; // Capacité infinie pour la simulation
    }

    @Override
    public long tempsRemplissage() {
        return 0; // Indique qu'il n'a pas besoin de temps de remplissage
    }


    @Override
    public double getVitesse(NatureTerrain nature) {
        if (nature == NatureTerrain.ROCHE) {
            return convertirVitesseEnMS(10); // Vitesse réduite à 10 km/h sur le rocher
        }
        return convertirVitesseEnMS(vitesseKmH); // Vitesse normale sinon
    }

    @Override
    public double calculerTemps(Carte carte, Case destination) {
        NatureTerrain nature = destination.getNature();
        double vitesseMps = getVitesse(nature); // vitesse en m/s

        if (vitesseMps == 0) {
            return Double.POSITIVE_INFINITY; // Inaccessible pour ce terrain
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
        // Le robot à pattes peut se déplacer sur toutes les cases sauf l'eau
        return nature != NatureTerrain.EAU;
    }


    @Override
    public void remplirReservoir() {
        // Aucun remplissage nécessaire pour un robot à pattes
    }

    @Override
    public void deverserEau(int volume) {
        // Réservoir infini, pas de changement de niveau d’eau
    }
}
