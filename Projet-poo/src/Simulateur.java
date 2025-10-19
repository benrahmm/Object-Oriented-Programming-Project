import gui.GUISimulator;
import gui.ImageElement;
import gui.Simulable;
import java.awt.Color;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.io.File;
import gui.Rectangle;
import gui.Oval;
import gui.Text;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.zip.DataFormatException;

/**
 * Classe Simulateur.
 *
 * Cette classe implémente l'interface {@link Simulable} pour gérer les actions
 * simulées d'une carte, des robots et des incendies dans un environnement graphique.
 * Elle s'occupe également de gérer les événements et de synchroniser l'affichage.
 */


public class Simulateur implements Simulable {
    private GUISimulator gui;
    private DonneesSimulation donnees;
    private int cellSize;
    private int margin = 20;
    private PriorityQueue<Evenement> evenements;
    private long dateSimulation;
    private String cheminFichierDonnees;
    private String cheminFichierScenario;
    private ChefPompier chefPompier;
    private String typetest;


    // Chemins d'images pour chaque type de terrain et robot
    private final String TERRAIN_LIBRE_IMG = "images/terrain_libre.png";
    private final String ROCHE_IMG = "images/roche.png";
    private final String EAU_IMG = "images/eau.png";
    private final String FORET_IMG = "images/foret.png";
    private final String HABITAT_IMG = "images/habitat.png";
    private final String DRONE_IMG = "images/drone.png";
    private final String ROUES_IMG = "images/roues.png";
    private final String CHENILLES_IMG = "images/chenille.png";
    private final String PATTES_IMG = "images/pattes.png";
    private final String FIRE_IMG = "images/fire.png";

    /**
     * Constructeur principal avec un fichier de scénario.
     *
     * @param donnees Les données de simulation.
     * @param cheminFichierDonnees Chemin du fichier contenant les données de simulation.
     * @param cheminFichierScenario Chemin du fichier de scénario (peut être null).
     */

    public Simulateur(DonneesSimulation donnees, String cheminFichierDonnees, String cheminFichierScenario) {
        this.donnees = donnees;
        this.cellSize = 50;
        this.cheminFichierDonnees = cheminFichierDonnees;
        this.cheminFichierScenario = cheminFichierScenario;
        this.dateSimulation = -1;
        this.evenements = new PriorityQueue<>(Comparator.comparingLong(Evenement::getDate));

        int width = donnees.getCarte().getNbColonnes() * cellSize + 2 * margin;
        int height = donnees.getCarte().getNbLignes() * cellSize + 2 * margin;

        this.gui = new GUISimulator(width, height, Color.BLACK);
        this.gui.setSimulable(this);
        afficherElements(this.gui);
    }

    /**
     * Constructeur secondaire sans fichier de scénario.
     *
     * @param donnees Les données de simulation.
     * @param cheminFichierDonnees Chemin du fichier contenant les données de simulation.
     */


    public Simulateur(DonneesSimulation donnees, String cheminFichierDonnees) {
        this.donnees = donnees;
        this.cellSize = 50;
        this.cheminFichierDonnees = cheminFichierDonnees;
        this.dateSimulation = -1;
        this.evenements = new PriorityQueue<>(Comparator.comparingLong(Evenement::getDate));

        int width = donnees.getCarte().getNbColonnes() * cellSize + 2 * margin;
        int height = donnees.getCarte().getNbLignes() * cellSize + 2 * margin;

        this.gui = new GUISimulator(width, height, Color.BLACK);
        this.gui.setSimulable(this);
        for (Robot robot: donnees.getRobots()){
            robot.setSimulateur(this);
        }
        afficherElements(this.gui);
    }

    public long getDateActuelle() {
        return dateSimulation; // Assurez-vous que `dateSimulation` est bien l’attribut stockant la date actuelle
    }

    public DonneesSimulation getDonnees() {
        return this.donnees; // Assurez-vous que `donnees` est bien l’attribut contenant les données de simulation
    }

    public void setChefPompier(ChefPompier chefPompier){
        this.chefPompier = chefPompier;
    }

    public void setTypeTest(String typetest){
        this.typetest = typetest;
    }

    public void afficherElements(GUISimulator gui) {
        gui.reset();

        // Affichage des cases de la carte avec des images de fond
        for (int i = 0; i < donnees.getCarte().getNbLignes(); i++) {
            for (int j = 0; j < donnees.getCarte().getNbColonnes(); j++) {
                NatureTerrain nature = donnees.getCarte().getCase(i, j).getNature();
                String imagePath;

                // Choisir l'image selon le type de terrain
                switch (nature) {
                    case TERRAIN_LIBRE: imagePath = TERRAIN_LIBRE_IMG; break;
                    case ROCHE: imagePath = ROCHE_IMG; break;
                    case EAU: imagePath = EAU_IMG; break;
                    case FORET: imagePath = FORET_IMG; break;
                    case HABITAT: imagePath = HABITAT_IMG; break;
                    default: imagePath = null;
                }
                if (imagePath != null) {
                    int x = j * cellSize + margin;
                    int y = i * cellSize + margin;
                    gui.addGraphicalElement(new ImageElement(x, y, imagePath, cellSize, cellSize, gui));
                }
            }
        }

        // Affichage des incendies
        for (Incendie incendie : donnees.getIncendies()) {
            int x = incendie.getPosition().getColonne() * cellSize + margin;
            int y = incendie.getPosition().getLigne() * cellSize + margin;
            if (incendie.getIntensite() > 0) {
                gui.addGraphicalElement(new ImageElement(x, y, FIRE_IMG, 5*cellSize / 6, 5*cellSize / 6, gui));
            }
        }

        // Affichage des robots
        for (Robot robot : donnees.getRobots()) {
            int x = robot.getPosition().getColonne() * cellSize + margin;
            int y = robot.getPosition().getLigne() * cellSize + margin;
            String robotImagePath;

            if (robot instanceof Drone) {
                robotImagePath = DRONE_IMG;
            } else if (robot instanceof Roues) {
                robotImagePath = ROUES_IMG;
            } else if (robot instanceof Chenilles) {
                robotImagePath = CHENILLES_IMG;
            } else if (robot instanceof Pattes) {
                robotImagePath = PATTES_IMG;
            } else {
                robotImagePath = null;
            }

            if (robotImagePath != null) {
                gui.addGraphicalElement(new ImageElement(x, y, robotImagePath, 3*cellSize / 4, 3*cellSize / 4, gui));
            }
        }
    }

    public void chargeScenario(String fichierScenario) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fichierScenario));
        while (scanner.hasNext()) {
            try {
                long date = scanner.nextLong();
                String identifiantRobot = scanner.next(); // Utiliser l'identifiant unique
                String action = scanner.next();

                // Récupérer le robot par identifiant unique
                Robot robot = donnees.getRobotById(identifiantRobot);

                if (robot == null) {
                    System.out.println("Erreur : Robot avec identifiant " + identifiantRobot + " introuvable");
                    continue;
                }

                // Gérer l'action en fonction de la commande du scénario
                if (action.equals("MOVE")) {
                    int x2 = scanner.nextInt();
                    int y2 = scanner.nextInt();
                    if (donnees.getCarte().isInBounds(x2, y2)) {
                        Case destination = donnees.getCarte().getCase(x2, y2);
                        ajouteEvenement(new EvenementDeplacement(date, robot, destination));
                    } else {
                        ajouteEvenement(new EvenementErreur(date, "Erreur : Case (" + x2 + ", " + y2 + ") hors limites."));
                    }
                } else if (action.equals("INTERVENE")) {
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
                    if (donnees.getCarte().isInBounds(x, y)) {
                        Case position = donnees.getCarte().getCase(x, y);
                        ajouteEvenement(new EvenementInterventionEtape2(date, robot, position));
                    } else {
                        ajouteEvenement(new EvenementErreur(date, "Erreur : Case (" + x + ", " + y + ") hors limites."));
                    }
                } else if (action.equals("REFILL")) {
                    ajouteEvenement(new EvenementRemplissage(date, robot));
                }
            } catch (Exception e) {
                ajouteEvenement(new EvenementErreur(dateSimulation, "Erreur de format dans le fichier de scénario."));
            }
        }
        scanner.close();
    }



    public void ajouteEvenement(Evenement e) {
        evenements.add(e);
    }

    public void incrementeDate() {
        dateSimulation++;
        while (!evenements.isEmpty() && evenements.peek().getDate() <= dateSimulation) {
            evenements.poll().execute();
        }
    }

    @Override
    public void next() {

        if (dateSimulation%5 == 0){
            if (this.typetest.equals("Elementaire")){
                this.chefPompier.strategieElementaire();
            }
            if (this.typetest.equals("Evoluee")){
                this.chefPompier.strategieEvoluee();
            }
        }
        incrementeDate();
        afficherElements(this.gui);
    }

    @Override
    public void restart() {
        dateSimulation = 0;
        evenements.clear();

        try {
            DonneesSimulation nouvellesDonnees = LecteurDonnees.creeDonnees(cheminFichierDonnees);
            this.donnees = nouvellesDonnees;
            this.chefPompier = new ChefPompier(donnees);
            for (Robot robot: donnees.getRobots()){
            robot.setSimulateur(this);
            }
            if (cheminFichierScenario != null) {
                chargeScenario(cheminFichierScenario);
            }
            System.out.println("Simulation réinitialisée.");
        } catch (FileNotFoundException | DataFormatException e) {
            System.out.println("Erreur lors de la réinitialisation : " + e.getMessage());
        }
        afficherElements(this.gui);
    }
}
