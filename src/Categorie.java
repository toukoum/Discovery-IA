import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Categorie {
    // classe utilitaire avec diverse fonctions et méthodes

    private String nom; // le nom de la catégorie p.ex : sport, politique,...
    private ArrayList<PaireChaineEntier> lexique = new ArrayList<>(); //le lexique de la catégorie

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
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                int sep = ligne.indexOf(":");
                String text = ligne.substring(0, sep);
                int poids_int = Integer.parseInt(ligne.substring(sep + 1));
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
        int somme = 0;
        for (String mot : d.getMots()) {
            somme += UtilitairePaireChaineEntier.entierPourChaine(lexique, mot);
        }
        return somme;
    }



}
