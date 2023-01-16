import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PaireChaineEntier {

    private String chaine;
    private int entier;


    public PaireChaineEntier(String chaine, int entier) {
        this.chaine = chaine;
        this.entier = entier;
    }


    public String getChaine() {
        return chaine;
    }

    public int getEntier() {
        return entier;
    }

    public void setChaine(String chaine) {
        this.chaine = chaine;
    }

    public void setEntier(int entier) {
        this.entier = entier;
    }

    public void afficher() {
        System.out.println("---------------------");
        System.out.println("texte :" + chaine);
        System.out.println("poids :" + entier);
        System.out.println();
        System.out.println("---------------------");
    }
}





