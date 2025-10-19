import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

/**
 * Classe TestLecteurDonnees.
 *
 * Cette classe permet de tester la lecture d'un fichier de données
 * contenant les informations nécessaires à la simulation. Elle utilise
 * la classe {@link LecteurDonnees} pour lire le fichier et afficher
 * les données contenues.
 *
 * Usage :
 * <pre>
 * java TestLecteurDonnees <nomDeFichier>
 * </pre>
 */

public class TestLecteurDonnees {

    /**
     * Point d'entrée principal pour tester la lecture d'un fichier de données.
     *
     * @param args Les arguments de la ligne de commande :
     * <ul>
     *   <li><code>args[0]</code> : Chemin vers le fichier de données à lire.</li>
     * </ul>
     * Si le fichier est manquant ou invalide, un message d'erreur est affiché.
     */

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            LecteurDonnees.lire(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }

}

