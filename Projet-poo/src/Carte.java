import java.util.ArrayList;
import java.util.List;

/**
 * La classe Carte représente une carte composée de cases organisées en grille.
 * Elle contient des informations sur le nombre de lignes, de colonnes, et la taille de chaque case.
 * Elle permet de gérer et de manipuler les cases de la carte, y compris l'ajout de cases, la vérification des voisins,
 * et la recherche de cases contenant de l'eau.
 */

public class Carte {
    private int nbLignes;
    private int nbColonnes;
    private int tailleCases;
    private Case[][] cases;

    public Carte(int nbLignes, int nbColonnes, int tailleCases) {
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.tailleCases = tailleCases;
        this.cases = new Case[nbLignes][nbColonnes];
    }

    public void ajouterCase(int ligne, int colonne, NatureTerrain nature) {
        this.cases[ligne][colonne] = new Case(ligne, colonne, nature);
    }

    public Case getCase(int ligne, int colonne) {
        return cases[ligne][colonne];
    }

    public int getNbLignes() {
        return nbLignes;
    }

    public int getNbColonnes() {
        return nbColonnes;
    }

    public int getTailleCases() {
        return tailleCases;
    }

    // Vérifie si un voisin existe dans une direction donnée
    public boolean voisinExiste(Case src, Direction dir) {
        int lig = src.getLigne();
        int col = src.getColonne();
        switch (dir) {
            case NORD: return lig > 0;
            case SUD: return lig < nbLignes - 1;
            case EST: return col < nbColonnes - 1;
            case OUEST: return col > 0;
            default: return false;
        }
    }

    // Retourne la liste des voisins accessibles d'une case
    public List<Case> getVoisins(Case src) {
        int lig = src.getLigne();
        int col = src.getColonne();

        List<Case> listVoisins = new ArrayList<>();

        if (voisinExiste(src, Direction.NORD)) listVoisins.add(cases[lig - 1][col]);
        if (voisinExiste(src, Direction.SUD)) listVoisins.add(cases[lig + 1][col]);
        if (voisinExiste(src, Direction.EST)) listVoisins.add(cases[lig][col + 1]);
        if (voisinExiste(src, Direction.OUEST)) listVoisins.add(cases[lig][col - 1]);

        return listVoisins;
    }

    // Vérifie si les coordonnées (ligne, colonne) sont dans les limites de la carte
    public boolean isInBounds(int ligne, int colonne) {
        return ligne >= 0 && ligne < nbLignes && colonne >= 0 && colonne < nbColonnes;
    }

    // Retourne une liste de toutes les cases contenant de l'eau
    public List<Case> getCasesEau() {
        List<Case> casesEau = new ArrayList<>();
        for (int i = 0; i < this.nbLignes; i++) {
            for (int j = 0; j < this.nbColonnes; j++) {
                Case caseCourante = this.getCase(i, j);
                if (caseCourante.getNature() == NatureTerrain.EAU) {
                    casesEau.add(caseCourante);
                }
            }
        }
        return casesEau;
    }
}
