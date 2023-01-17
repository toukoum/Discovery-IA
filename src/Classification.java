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


    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> cat_all, String nomFichier) {

        String resultat;
        int c_politique = 0;
        int c_economie = 0;
        int c_sports = 0;
        int c_environement = 0;
        int c_culture = 0;

        ArrayList<PaireChaineEntier> Score = new ArrayList<>();
        ArrayList<String> catégorie = new ArrayList<>(Arrays.asList("POLITIQUE", "ECONOMIE", "SPORTS", "ENVIRONNEMENT-SCIENCES", "CULTURE"));

        int i = 0;

        FileWriter file = null;
        try {
            file = new FileWriter(nomFichier);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        while (i < depeches.size()) {


            for (int j = 0; j < cat_all.size(); j++) {
                PaireChaineEntier paire_x = new PaireChaineEntier(catégorie.get(j), cat_all.get(j).score(depeches.get(i)));
                Score.add(paire_x);
            }

            resultat = UtilitairePaireChaineEntier.chaineMax(Score);
            try {
                // écrire dans un fichier réponse
                for (int k = 0; k < Score.size(); k++) {
                    System.out.println(Score.get(k).getChaine() + ":" + Score.get(k).getEntier());
                }
                file.write(depeches.get(i).getId() + ":"); // affichage de : 00X:
                file.write(resultat); // affichage de la catégorie ayant le score max de la dépèche i
                file.write('\n');


            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(resultat);
            System.out.println(depeches.get(i).getCategorie());
            if (resultat.compareTo(depeches.get(i).getCategorie()) == 0) {

                // Vérifie si la variable "resultat" est égale à la catégorie de la "depeche" actuelle dans la liste "depeches" à l'index "i"

                if (depeches.get(i).getCategorie().compareTo(catégorie.get(0)) == 0) {
                    // Incrémente le compteur c_politique si categorie de depeche == premier element de la liste categorie
                    c_politique += 1;
                } else if (depeches.get(i).getCategorie().compareTo(catégorie.get(1)) == 0) {
                    c_economie += 1;
                } else if (depeches.get(i).getCategorie().compareTo(catégorie.get(2)) == 0) {
                    c_sports += 1;
                } else if (depeches.get(i).getCategorie().compareTo(catégorie.get(3)) == 0) {
                    c_environement += 1;
                } else {
                    c_culture += 1;
                }
            }

            Score.clear();
            i++;
        }
        System.out.println(c_economie);
        ArrayList<Integer> Resultat_pourcentage = new ArrayList<>(Arrays.asList(c_politique, c_economie, c_sports, c_environement, c_culture));

        int l = 0; // initialise compteur l
        while (l < catégorie.size()) { // parcours tous elements de categorie
            try { //
                file.write(catégorie.get(l) + ":" + " ".repeat(30 - catégorie.get(l).length()));
                //  écrit la catégorie actuelle suivie d'un certain nombre d'espaces dans le fichier
                file.write(Integer.toString(Resultat_pourcentage.get(l)) + "%");
                // écrit le pourcentage de résultat correspondant à la catégorie actuelle dans le fichier
                file.write("\n");
                // ajoute un saut de ligne pour séparer les différentes catégories dans le fichier
            } catch (IOException e) {
                // gère les erreurs d'entrée/sortie
                throw new RuntimeException(e);
            }
            l++; // continue le parcours
        }

        try { // ferme le fichier une fois que toutes les données ont été écrites
            file.close();
        } catch (
                IOException e) { // gère les erreurs d'entrée/sortie
            throw new RuntimeException(e);
        }
        System.out.println("votre saisie a été écrite avec succès dans fichier-sortie.txt");


    }


    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();

        int i = 0;

        while (i < depeches.size()) {
            if (depeches.get(i).getCategorie().compareTo(categorie) == 0) {

                String contenu = depeches.get(i).getContenu();
                String chaine = contenu.toLowerCase();
                chaine = chaine.replace('\n', ' ');
                chaine = chaine.replace('.', ' ');
                chaine = chaine.replace(',', ' ');
                chaine = chaine.replace('\'', ' ');
                chaine = chaine.replace('\"', ' ');
                chaine = chaine.replace('(', ' ');
                chaine = chaine.replace(')', ' ');
                String[] tabchaine = chaine.split(" ");

                // parcours de tous les éléments de tabchaine
                for (int j = 0; j < tabchaine.length; j++) {
                    // si l'élément n'est pas null
                    if (!tabchaine[j].equals("")) {
                        //  vérification que le mot à l'index j de tabchaine n'est pas présent dans resultat
                        // si résultat est null forcément on ajoute la paire
                        if (resultat.size() == 0) {
                            PaireChaineEntier a_ajouter = new PaireChaineEntier(tabchaine[j], 0);
                            resultat.add(a_ajouter);

                        } else {
                            int k = 0;
                            boolean ajout = false;
                            while (k < resultat.size()) {
                                if (!tabchaine[j].equals(resultat.get(k).getChaine())) {

                                    ajout = true;
                                }
                                k++;
                            }

                            if (ajout) {
                                // ajout d'une nouvelle paire Chaine/Entier dans résultat; initialisation du score à zéro
                                PaireChaineEntier a_ajouter = new PaireChaineEntier(tabchaine[j], 0);
                                resultat.add(a_ajouter);

                            }

                        }
                    }

                }
            }
            i++;

        }


        return resultat;

    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {

        int i = 0;

        // parcours de tous les mots de la catégorie x
        while (i < dictionnaire.size()) {

            // parcours de toutes les depêches
            int j = 0;
            while (j < depeches.size()) {

                // parcours de tous les mots de chaque depêche
                int k = 0;
                while (k < depeches.get(j).getMots().size()) {

                    int score = dictionnaire.get(i).getEntier();
                    // si le mot du dico est égale au mot de la depêche
                    if ((dictionnaire.get(i).getChaine().compareTo(depeches.get(j).getMots().get(k)) == 0)){

                        //  la catégorie du mot de la depêche est categorie (paramètre)
                        if (depeches.get(j).getCategorie().compareTo(categorie) == 0){
                            dictionnaire.get(i).setEntier(score + 1);
                        }
                        else if (depeches.get(j).getCategorie().compareTo(categorie) != 0){
                            dictionnaire.get(i).setEntier(score -1);
                        }
                    }
                    k++;
                }
                j++;
            }
            i++;
        }

    }

    public static int poidsPourScore(int score) {
        int poids = 0;

        if (score > 0) {
            poids = 3;
        } else if (score == 0) {
            poids = 2;
        }
        else {
            poids = 1;
        }

        return poids;
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {

        ArrayList<PaireChaineEntier> newDico = initDico(depeches, categorie);
        calculScores(depeches, categorie, newDico);

        FileWriter file = null;
        try {
            file = new FileWriter(nomFichier);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int i = 0;

        while (i < newDico.size()) {
            try {
                file.write(newDico.get(i).getChaine() + ":" + poidsPourScore(newDico.get(i).getEntier()) + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            i++;
        }

        try {
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {


        //Chargement des dépêches en mémoire
        System.out.println("chargement des dépêches");
        ArrayList<Depeche> depeches = lectureDepeches("./depeches.txt");

//        for (int i = 0; i < depeches.size(); i++) {
//            depeches.get(i).afficher();
//        }


        Categorie lexique_sports = new Categorie("sports"); // Création d'un objet 'Catégorie' de nom 'sports'
        Categorie lexique_politique = new Categorie("polititque"); // Création d'un objet 'Catégorie' de nom 'politique'
        Categorie lexique_economie = new Categorie("économie"); // Création d'un objet 'Catégorie' de nom 'economie'
        Categorie lexique_environement_science = new Categorie("science"); // Création d'un objet 'Catégorie' de nom 'environement_science'
        Categorie lexique_culture = new Categorie("culture"); // Création d'un objet 'Catégorie' de nom 'culture'
//
//
        ArrayList<Categorie> cat_all = new ArrayList<>(Arrays.asList(lexique_politique, lexique_economie, lexique_sports, lexique_environement_science, lexique_culture));
//
        lexique_politique.initLexique("./lexique_politique");// Fonction initLexique appelé pour injecter les lexiques_politique dans politique
        lexique_economie.initLexique("./lexique_economie");// Fonction initLexique appelé pour injecter les lexiques_economie dans economie
        lexique_sports.initLexique("./lexique_sports");// Fonction initLexique appelé pour injecter les lexiques_sports dans sports
        lexique_environement_science.initLexique("lexique_environnement-sciences");// Fonction initLexique appelé pour injecter les lexiques_environement_science dans environement_science
        lexique_culture.initLexique("./lexique_culture_auto");// Fonction initLexique appelé pour injecter les lexiques_culture dans culture

//        int p = 0;
//        while (p < lexique_economie.getLexique().size()) {
//            System.out.println(lexique_economie.getLexique().get(p).getChaine() + " " + lexique_economie.getLexique().get(p).getEntier());
//           p++;
//        }
//
//
//        ArrayList<PaireChaineEntier> Score = new ArrayList<>();
//        ArrayList<String> catégorie = new ArrayList<>(Arrays.asList("lexique_culture", "lexique_politique", "lexique_économique", "lexique_sports", "lexique_environement_science"));
//        for (int i = 0; i < cat_all.size(); i++) {
//            PaireChaineEntier paire_x = new PaireChaineEntier(catégorie.get(i), cat_all.get(i).score(depeches.get(10)));
//            Score.add(paire_x);
//        }
//
//
//        int i = 0;// initialisation invariant
//
//        while (i < Score.size()) { // tant que i est < au vecteur lexique
//            Score.get(i).afficher(); // affichage de l'élément i avec la valeur du text et du poid
//            i++;
//        }
//
//        System.out.println("Catégorie ayant le score maximum: " + catégorie.get(calculScores(0,0,0))


        classementDepeches(depeches, cat_all, "fichier_sortie.txt");

//        afficahge de la fonction qui retourne l'entier associé à la chaine de caractères dans listePaires
//        int entPaire = UtilitairePaireChaineEntier.entierPourChaine(vlexique_culture, "art");
//        System.out.println(entPaire);


//        System.out.println(UtilitairePaireChaineEntier.chaineMax(vlexique_culture)); // appele la fonction qui renvoie la chaine avec le poids (entier) le plus grand

//        ----------------------------------------
//        PARTIE 2
//        ----------------------------------------

//        ArrayList<PaireChaineEntier> resultat = initDico(depeches, "CULTURE");
//        calculScores(depeches, "CULTURE", resultat);
//
//        int i = 0;
//        int max = 0;
//        while (i < resultat.size()) {
//            System.out.println(resultat.get(i).getChaine() + ": " + resultat.get(i).getEntier());
//
//            if (resultat.get(i).getEntier() > resultat.get(max).getEntier()) {
//                max = i;
//            }
//            i++;
//        }
//
//        System.out.println(resultat.size());
//        System.out.println(resultat.get(max).getChaine() + ": " + resultat.get(max).getEntier());


        generationLexique(depeches, "CULTURE", "lexique_culture_auto");


    }


}