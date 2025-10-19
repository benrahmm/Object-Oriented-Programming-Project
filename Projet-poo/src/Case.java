/**
 * La classe Case représente une case individuelle sur une carte.
 * Une case est définie par ses coordonnées (ligne, colonne), sa nature (type de terrain),
 * et un incendie éventuel qui peut s'y trouver.
 */

public class Case {
    private int ligne;
    private int colonne;
    private NatureTerrain nature;
    private Incendie incendie;

    public Case(int ligne, int colonne, NatureTerrain nature) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.nature = nature;
        this.incendie = null; // Par défaut, sans incendie
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public NatureTerrain getNature() {
        return nature;
    }

    public void setIncendie(Incendie incendie) {
        this.incendie = incendie;
    }

    public Incendie getIncendie() {
        return incendie;
    }

    // Vérifie si la case contient de l'eau
    public boolean estCaseEau() {
        return this.nature == NatureTerrain.EAU;
    }

    // Calcule la distance entre deux cases
    public double calculerDistance(Case autre) {
        int dx = this.ligne - autre.getLigne();
        int dy = this.colonne - autre.getColonne();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
