import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Classification {

    private static ArrayList<Depeche> lectureDepeches(String nomFichier) {
        //creation d'un tableau de dépêches
        ArrayList<Depeche> depeches = new ArrayList<>();
        try {
            // lecture du fichier d'entrée
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String id = ligne.substring(3);
                ligne = scanner.nextLine();
                String date = ligne.substring(3);
                ligne = scanner.nextLine();
                String categorie = ligne.substring(3);
                ligne = scanner.nextLine();
                String lignes = ligne.substring(3);
                while (scanner.hasNextLine() && !ligne.equals("")) {
                    ligne = scanner.nextLine();
                    if (!ligne.equals("")) {
                        lignes = lignes + '\n' + ligne;
                    }
                }
                Depeche uneDepeche = new Depeche(id, date, categorie, lignes);
                depeches.add(uneDepeche);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return depeches;
    }


    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier) {
    }

    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        return resultat;

    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
    }

    public static int poidsPourScore(int score) {
        return 0;
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {

    }

    public static void main(String[] args) {



        //Chargement des dépêches en mémoire
        System.out.println("chargement des dépêches");
        ArrayList<Depeche> depeches = lectureDepeches("./depeches.txt");

        for (int i = 0; i < depeches.size(); i++) {
            depeches.get(i).afficher();
        }


        System.out.println("Chargement des Scores de la dépèche 0 par catégorie");
        Categorie lexique_culture = new Categorie("culture"); // Création d'un objet 'Catégorie' de nom 'culture'
        Categorie lexique_sports = new Categorie("sports"); // Création d'un objet 'Catégorie' de nom 'culture'
        Categorie lexique_politique = new Categorie("polititque"); // Création d'un objet 'Catégorie' de nom 'culture'
        Categorie lexique_économique = new Categorie("économique"); // Création d'un objet 'Catégorie' de nom 'culture'
        Categorie lexique_environement_science = new Categorie("science"); // Création d'un objet 'Catégorie' de nom 'culture'


        ArrayList<Categorie> cat_all = new ArrayList<>(Arrays.asList(lexique_culture, lexique_politique, lexique_économique, lexique_sports, lexique_environement_science));


        ArrayList<PaireChaineEntier> vlexique_culture = lexique_culture.getLexique();// Création d'un vecteur(liste) de type <PaireChaineEntier> pour get le nom/Chaine et entier de chaque lexique
        ArrayList<PaireChaineEntier> vlexique_sport = lexique_sports.getLexique();// Création d'un vecteur(liste) de type <PaireChaineEntier> pour get le nom/Chaine et entier de chaque lexique
        ArrayList<PaireChaineEntier> vlexique_politique = lexique_politique.getLexique();// Création d'un vecteur(liste) de type <PaireChaineEntier> pour get le nom/Chaine et entier de chaque lexique
        ArrayList<PaireChaineEntier> vlexique_économique = lexique_économique.getLexique();// Création d'un vecteur(liste) de type <PaireChaineEntier> pour get le nom/Chaine et entier de chaque lexique
        ArrayList<PaireChaineEntier> vlexique_environement_science = lexique_environement_science.getLexique();// Création d'un vecteur(liste) de type <PaireChaineEntier> pour get le nom/Chaine et entier de chaque lexique


        lexique_culture.initLexique("./lexique_culture");// Fonction initLexique appelé pour injecter les lexiques_culture dans culture
        lexique_sports.initLexique("./lexique_sports");// Fonction initLexique appelé pour injecter les lexiques_culture dans culture
        lexique_politique.initLexique("./lexique_politique");// Fonction initLexique appelé pour injecter les lexiques_culture dans culture
        lexique_économique.initLexique("./lexique_economie");// Fonction initLexique appelé pour injecter les lexiques_culture dans culture
        lexique_environement_science.initLexique("lexique_environnement-sciences");// Fonction initLexique appelé pour injecter les lexiques_culture dans culture


        ArrayList<PaireChaineEntier> Score = new ArrayList<>();
        ArrayList<String> catégorie = new ArrayList<>(Arrays.asList("lexique_culture", "lexique_politique", "lexique_économique", "lexique_sports", "lexique_environement_science"));
        for (int i = 0; i < cat_all.size(); i++) {
            // Prend la categorie a l'index, puis prend tous les index des categories et leurs scores en fonction de la 10 eme categorie
            PaireChaineEntier paire_x = new PaireChaineEntier(catégorie.get(i), cat_all.get(i).score(depeches.get(10)));
            Score.add(paire_x);// Ajoute le score
        }


        int i = 0;// initialisation invariant

        while (i < Score.size()) { // tant que i est < au vecteur lexique
            Score.get(i).afficher(); // affichage de l'élément i avec la valeur du text et du poid
            i++;
        }
        // Appele la fonction Score de Utilitaire pour avoir categorie au score max dans l'index 10
        System.out.println("Catégorie ayant le score maximum: " + UtilitairePaireChaineEntier.chaineMax(Score));


        Scanner lecteur = new Scanner(System.in);
        // Initialisation des variables
        String chaine_nom;
        int cat;

        System.out.println("Choissir un mot pour trouver son poids : "); // get le result indiquer par l'utilisateur
        chaine_nom = lecteur.nextLine();

        System.out.println("Choissir sa categorie : 1 Culture 2 Sports 3 Politique 4 Economie 5 Environement-Science");
        // get la categorie choisit par l'utilisateur;
        cat = lecteur.nextInt(); lecteur.nextLine();

        while(cat < 1 || cat > 5){ // Si nombre hors de la plage indiquer, renvoyer le message (error)
            System.out.println("Erreur, veuillez choisir un nombre entre 1 et 5");
            cat = lecteur.nextInt(); lecteur.nextLine();
        }
        if (cat == 1) {

            //afficahge de la fonction qui retourne l'entier associé à la chaine de caractères dans listePaires
            int entPaire = UtilitairePaireChaineEntier.entierPourChaine(vlexique_culture, chaine_nom);
            System.out.println("result Culture :" + "\n" + entPaire);

        } else if (cat == 2) {

            //afficahge de la fonction qui retourne l'entier associé à la chaine de caractères dans listePaires
            int entPaire = UtilitairePaireChaineEntier.entierPourChaine(vlexique_sport, chaine_nom);
            System.out.println("result Sports :" + "\n" + entPaire);
        } else if (cat == 3) {

            //afficahge de la fonction qui retourne l'entier associé à la chaine de caractères dans listePaires
            int entPaire = UtilitairePaireChaineEntier.entierPourChaine(vlexique_politique, chaine_nom);
            System.out.println("result Politique :" + "\n" + entPaire);
        } else if (cat == 4) {

            //afficahge de la fonction qui retourne l'entier associé à la chaine de caractères dans listePaires
            int entPaire = UtilitairePaireChaineEntier.entierPourChaine(vlexique_économique, chaine_nom);
            System.out.println("result Economie :" + "\n" + entPaire);
        } else if (cat == 5) {

            //afficahge de la fonction qui retourne l'entier associé à la chaine de caractères dans listePaires
            int entPaire = UtilitairePaireChaineEntier.entierPourChaine(vlexique_environement_science, chaine_nom);
            System.out.println("result Environement-Science :" + "\n" + entPaire);
        }
        int entPaire = UtilitairePaireChaineEntier.entierPourChaine(vlexique_environement_science, chaine_nom); // Variable
        if (entPaire == 0){// si poids = 0
            System.out.println("Vous n'avez acun mot/lexique dans cette categorie");
        }

        //afficahge de la fonction qui retourne l'entier associé à la chaine de caractères dans listePaires
        //int entPaire = UtilitairePaireChaineEntier.entierPourChaine(vlexique_culture, chaine_nom);
       // System.out.println(entPaire);



//        System.out.println(UtilitairePaireChaineEntier.chaineMax(vlexique_culture)); // appele la fonction qui renvoie la chaine avec le poids (entier) le plus grand





    }


}

