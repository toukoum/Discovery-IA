import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ExempleEcritureFichier
{

    public static void main(String[] args) {

        Scanner lecteur = new Scanner((System.in));
        System.out.println("entrez votre nom :");
        String s= lecteur.nextLine();
        try {
            FileWriter file = new FileWriter("fichier-sortie.txt");
            file.write("chaine saisie :\n");
            file.write(s+"\n");
            file.close();
            System.out.println("votre saisie a été écrite avec succès dans fichier-sortie.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
