import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Comparator;

/**
 * Classe abstraite représentant un robot.
 *
 * Les robots sont capables de se déplacer sur une carte, d'intervenir sur des incendies
 * et de gérer leur réservoir d'eau. Chaque type de robot hérite de cette classe et définit
 * ses comportements spécifiques.
 */

public abstract class Robot {
    protected String identifiant;  // Attribut pour l'identifiant unique
    protected Case position;
    protected int reservoir;
    protected double vitesseKmH; // Vitesse en km/h
    protected Carte carte;
    protected boolean enIntervention;
    protected Incendie incendieEnCours;
    protected Simulateur simulateur;

    // Constructeur modifié pour inclure l'identifiant unique
    public Robot(Case position, int vitesse, String identifiant) {
        this.position = position;
        this.vitesseKmH = vitesse;
        this.identifiant = identifiant;  // Initialiser l'identifiant
        this.enIntervention = false;
        this.incendieEnCours = null;
    }

    public Incendie findNearestAccessibleIncendie(List<Incendie> incendies) {
        Incendie incendieProche = null;
        double minDistance = Double.MAX_VALUE;

        // Trie les incendies par proximité croissante
        incendies.sort(Comparator.comparingDouble(incendie -> this.getPosition().calculerDistance(incendie.getPosition())));

        // Parcourt les incendies triés pour trouver le plus proche accessible
        for (Incendie incendie : incendies) {
            if (incendie.getIntensite() > 0 && this.peutSeDeplacerVers(incendie.getPosition())) {  // Vérifie l'intensité et l'accessibilité
                double distance = this.getPosition().calculerDistance(incendie.getPosition());
                if (distance < minDistance) {
                    minDistance = distance;
                    incendieProche = incendie;
                }
                // Sort de la boucle dès qu'un incendie accessible est trouvé
                break;
            }
        }
        return incendieProche; // Retourne l'incendie le plus proche et accessible, ou null si aucun n'est accessible
    }


    public Case findNearestAccessibleCaseForRefill(List<Case> casesEau) {
        Case caseProche = null;
        double minDistance = Double.MAX_VALUE;

        for (Case caseEau : casesEau) {
            if (this.peutSeDeplacerVers(caseEau)) {
                double distance = this.getPosition().calculerDistance(caseEau);
                if (distance < minDistance) {
                    minDistance = distance;
                    caseProche = caseEau;
                }
            } else {
                // Cherche une case voisine praticable si la case d'eau elle-même est inaccessible
                for (Case voisin : this.getCarte().getVoisins(caseEau)) {
                    if (this.peutSeDeplacerVers(voisin)) {
                        double distance = this.getPosition().calculerDistance(voisin);
                        if (distance < minDistance) {
                            minDistance = distance;
                            caseProche = voisin;
                        }
                    }
                }
            }
        }
        return caseProche;
    }


    public long tempsDeplacement(Case depart, Case destination) {
        return (long) calculerTemps(carte, destination); // Convertissez en long si `calculerTemps` renvoie un `double`
    }

    public abstract long tempsRemplissage();


    public void setCarte(Carte carte) {
        this.carte = carte;
    }

    public void setSimulateur(Simulateur simulateur) {
        this.simulateur = simulateur;
    }

    public boolean enIntervention() {
        return enIntervention;
    }

    public Case getPosition() {
        return position;
    }

    public Carte getCarte() {
        return carte;
    }

    public Simulateur getSimulateur() {
        return simulateur;
    }

    public void setPosition(Case position) {
        this.position = position;
    }

    public int getReservoir() {
        return this.reservoir;
    }


    // Nouvelle méthode pour obtenir l'identifiant unique
    public String getIdentifiant() {
        return identifiant;
    }

    public abstract double getVitesse(NatureTerrain nature);
    public abstract double calculerTemps(Carte carte, Case destination);

    protected double convertirVitesseEnMS(double vitesseKmH) {
        return vitesseKmH * 1000 / 3600;
    }

    public String getType() {
        return this.getClass().getSimpleName().toUpperCase();
    }

    public static int getDefaultSpeed(String type) {
        switch (type) {
            case "DRONE": return 150;
            case "ROUES": return 80;
            case "CHENILLES": return 60;
            case "PATTES": return 30;
            default: return -1;
        }
    }

    public abstract boolean peutSeDeplacerVers(Case destination);
    public abstract void intervenir(Case position);
    public abstract void remplirReservoir();
    public abstract void deverserEau(int volume);

    public List<Case> calculerPlusCourtChemin(Carte carte, Case destination) {
        // Algorithme de Dijkstra pour trouver le plus court chemin
        Map<Case, Double> distance = new HashMap<>();
        PriorityQueue<Case> queue = new PriorityQueue<>(Comparator.comparingDouble(distance::get));
        Map<Case, Case> predecesseur = new HashMap<>();

        distance.put(this.position, 0.0);
        queue.add(this.position);

        while (!queue.isEmpty()) {
            Case current = queue.poll();
            if (current.equals(destination)) break;

            for (Case voisin : this.getCarte().getVoisins(current)) {
                if (!this.peutSeDeplacerVers(voisin)) continue;
                double tentativeDistance = distance.get(current) + calculerTemps(carte, voisin);

                if (tentativeDistance < distance.getOrDefault(voisin, Double.POSITIVE_INFINITY)) {
                    distance.put(voisin, tentativeDistance);
                    predecesseur.put(voisin, current);
                    queue.add(voisin);
                }
            }
        }

        List<Case> chemin = new LinkedList<>();
        Case step = destination;
        while (step != null) {
            chemin.add(0, step);
            step = predecesseur.get(step);
        }
        return chemin;
    }

    public void programmerDeplacements(List<Case> chemin, long dateDebut) {
        long dateActuelle = dateDebut;
        for (Case prochaineCase : chemin) {
            EvenementDeplacement evenement = new EvenementDeplacement(dateActuelle, this, prochaineCase);
            this.getSimulateur().ajouteEvenement(evenement);
            dateActuelle += tempsDeplacement(this.position, prochaineCase); // Temps pour aller de case en case
            this.position = prochaineCase; // Met à jour la position temporaire pour le prochain déplacement
        }
    }
    

    public void deplacerVers(Case destination, long dateDebut) {
        programmerDeplacements(calculerPlusCourtChemin(carte, destination), dateDebut);
    }

    public void affecterIncendie(Incendie incendie) {
        if (estDisponible()) {
            this.incendieEnCours = incendie;
            this.enIntervention = true;

            // Programme les déplacements vers l'incendie
            List<Case> chemin = calculerPlusCourtChemin(carte, incendie.getPosition());
            long dateDebut = simulateur.getDateActuelle(); // Utilise la date actuelle de la simulation
            programmerDeplacements(chemin, dateDebut);

            // Calcule la date d'arrivée pour programmer l'intervention
            long dateArrivee = dateDebut;
            for (Case caseSuivante : chemin) {
                dateArrivee += calculerTemps(carte, caseSuivante);
            }

            // Programme l'événement d'intervention à la date d'arrivée
            EvenementIntervention evenementIntervention = new EvenementIntervention(dateArrivee, this, incendie.getPosition());
            simulateur.ajouteEvenement(evenementIntervention);
        }
    }


    public void verifierIntervention() {
        if (incendieEnCours == null || incendieEnCours.getIntensite() <= 0) {
            terminerIntervention();
            return;
        }
    
        if (reservoir > 0) {
            intervenir(incendieEnCours.getPosition());
    
            if (incendieEnCours.getIntensite() > 0) {
                long prochaineIntervention = simulateur.getDateActuelle() + 5;
                simulateur.ajouteEvenement(new EvenementIntervention(prochaineIntervention, this, incendieEnCours.getPosition()));
            }
        } else {
            Case caseEauProche = findNearestAccessibleCaseForRefill(simulateur.getDonnees().getCarte().getCasesEau());
            if (caseEauProche != null) {
                List<Case> cheminAller = calculerPlusCourtChemin(simulateur.getDonnees().getCarte(), caseEauProche);
                programmerDeplacements(cheminAller, simulateur.getDateActuelle());
    
                long dateRemplissage = simulateur.getDateActuelle() + tempsRemplissage();
                simulateur.ajouteEvenement(new EvenementRemplissage(dateRemplissage, this));
    
                List<Case> cheminRetour = calculerPlusCourtChemin(simulateur.getDonnees().getCarte(), incendieEnCours.getPosition());
                programmerDeplacements(cheminRetour, dateRemplissage);
    
                long dateRetour = dateRemplissage + cheminRetour.size() * tempsDeplacement(this.position, incendieEnCours.getPosition());
                simulateur.ajouteEvenement(new EvenementIntervention(dateRetour, this, incendieEnCours.getPosition()));
            }
        }
    }

    public void terminerIntervention() {
        this.enIntervention = false;
    }

    public boolean estDisponible() {
        return !this.enIntervention;
    }
}
