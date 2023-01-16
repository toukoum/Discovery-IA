import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Categorie {
    // classe utilitaire avec diverse fonctions et méthodes

    private String nom; // le nom de la catégorie p.ex : sport, politique,...
    private ArrayList<PaireChaineEntier> lexique; //le lexique de la catégorie

    // constructeur
    public Categorie(String nom) {
        this.nom = nom;
    }


    public String getNom() {
        return nom;
    }


    public  ArrayList<PaireChaineEntier> getLexique() {
        return lexique;
    }


    // initialisation du lexique de la catégorie à partir du contenu d'un fichier texte
    public void initLexique(String nomFichier) {

        try {
            // lecture du fichier d'entrée
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                int sep = ligne.indexOf(":");

                String text = ligne.substring(0, sep+1);
                ligne = scanner.nextLine();
               /* while (scanner.next() != "\n") {
                    String poids = ligne.substring(sep + 1);
                    Integer.parseInt(poids);
                }*/

                String poids_str = ligne.substring(sep + 1);
                int poids_int = Integer.parseInt(poids_str); // besoin d'une initalisation int pour la conversion

                PaireChaineEntier unlexique = new PaireChaineEntier(text, poids_int);
                lexique.add(unlexique);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    //calcul du score d'une dépêche pour la catégorie
    public int score(Depeche d) {
        return 0;
    }


}
