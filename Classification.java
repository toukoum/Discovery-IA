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

        FileWriter file; // declare file type FileWriter
        try {
            file = new FileWriter(nomFichier); // Creation nouveau fichier
        } catch (IOException e) {//Si exception
            throw new RuntimeException(e);// exception si temps trop long, program continue quand meme
        }
        // initialisation des categories
        int c_politique = 0;
        int c_economie = 0;
        int c_sports = 0;
        int c_environement = 0;
        int c_culture = 0;


        for (Depeche depeche : depeches) {//parcours vecteur [depeches]
            ArrayList<PaireChaineEntier> score = new ArrayList<>(); // declaration liste score
            for (Categorie categorie : cat_all) {//parcours vecteur [cat_all]
                PaireChaineEntier paire = new PaireChaineEntier(categorie.getNom(), categorie.score(depeche)); // initialisation liste paire
                score.add(paire); // addition de paire
            }
            String resultat = UtilitairePaireChaineEntier.chaineMax(score); // variable res = chainemax score
            try {
                file.write(depeche.getId() + ":" + resultat + "\n"); // ecriture numero depeche + score
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (resultat.compareTo(depeche.getCategorie()) == 0) { // si res = cat_initial
                if (depeche.getCategorie().compareTo("POLITIQUE") == 0) { //si cat est politique
                    c_politique += 1;
                } else if (depeche.getCategorie().compareTo("ECONOMIE") == 0) {//si cat est eco
                    c_economie += 1;
                } else if (depeche.getCategorie().compareTo("SPORTS") == 0) {//si cat est sport
                    c_sports += 1;
                } else if (depeche.getCategorie().compareTo("ENVIRONNEMENT-SCIENCES") == 0) {//si cat est envir
                    c_environement += 1;
                } else if (depeche.getCategorie().compareTo("CULTURE") == 0) {//si cat est cult
                    c_culture += 1;
                }
            }
        }

        ArrayList<Integer> liste_compteur = new ArrayList<>(Arrays.asList(c_politique, c_economie, c_sports, c_environement, c_culture));

        try {
            file.write("POLITIQUE:" + " ".repeat(21) + c_politique + "%\n");
            file.write("ECONOMIE:" + " ".repeat(22) + c_economie + "%\n");
            file.write("SPORTS:" + " ".repeat(24) + c_sports + "%\n");
            file.write("ENVIRONNEMENT-SCIENCES:" + " ".repeat(8) + c_environement + "%\n");
            file.write("CULTURE:" + " ".repeat(23) + c_culture + "%\n");
            file.write("MOYENNE:" + " ".repeat(23) + UtilitairePaireChaineEntier.moyenne(liste_compteur) + "%\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();

        // parcours de toutes les dépêches
        for (Depeche depeche : depeches) {

            if (depeche.getCategorie().equals(categorie)) {
                ArrayList<String> contenu = depeche.getMots();

                // delete de tout les mots non intéréssant de chaque depêche  "[a-zA-Z]{2,}+\\.?"
                for (int mot = 0; mot < contenu.size(); mot ++) {
                    if ((contenu.get(mot).matches("\\W*")) || (contenu.get(mot).matches("\\d*")) || (contenu.get(mot).matches("[a-zA-Z]"))) {
                        contenu.remove(contenu.get(mot));
                        mot-=1;
                    }
                }

                // parcours de tous les mots de chaque dépêche
                for (String mot : contenu) {
                    boolean motExiste = false;
                    // parcours tous les mots déjà présents dans resultat
                    for (PaireChaineEntier paire : resultat) {
                        if (paire.getChaine().equals(mot)) {
                            motExiste = true;
                            break;
                        }
                    }
                    if (!motExiste) {
                        PaireChaineEntier a_ajouter = new PaireChaineEntier(mot, 0);
                        resultat.add(a_ajouter);
                    }
                }
            }
        }
        return resultat;
    }


    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        for (PaireChaineEntier paire : dictionnaire) { // parcours vecteurs dictionnaire
            for (Depeche depeche : depeches) { // parcours vecteurs depeches
                if (depeche.getCategorie().equals(categorie) && depeche.getMots().contains(paire.getChaine())) {
                    paire.setEntier(paire.getEntier() + 1); // si cat de depeche = cat_initia et mots de depeches contient chaine de paire
                } else if (!depeche.getCategorie().equals(categorie) && depeche.getMots().contains(paire.getChaine())) {
                    paire.setEntier(paire.getEntier() - 2);
                }
            }
        }
    }

    public static void triVecteurFusion(ArrayList<PaireChaineEntier> atrie) {
        if (atrie.size() > 1) {
            ArrayList<PaireChaineEntier> gauche = new ArrayList<>();
            ArrayList<PaireChaineEntier> droite = new ArrayList<>();
            int milieu = atrie.size() / 2;
            for (int i = 0; i < milieu; i++) {
                gauche.add(atrie.get(i));
            }
            for (int i = milieu; i < atrie.size(); i++) {
                droite.add(atrie.get(i));
            }
            triVecteurFusion(gauche);
            triVecteurFusion(droite);
            fusion(gauche, droite, atrie);//recursivite fonction fusion
        }
    }

    private static void fusion(ArrayList<PaireChaineEntier> gauche, ArrayList<PaireChaineEntier> droite, ArrayList<PaireChaineEntier> atrie) {
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < gauche.size() && j < droite.size()) {
            if (gauche.get(i).getChaine().compareTo(droite.get(j).getChaine()) < 0) {
                atrie.set(k++, gauche.get(i++));
            } else {
                atrie.set(k++, droite.get(j++));
            }
        }
        while (i < gauche.size()) {
            atrie.set(k++, gauche.get(i++));
        }
        while (j < droite.size()) {
            atrie.set(k++, droite.get(j++));
        }
    }


    public static int poidsPourScore(int score) {
        int poids;

        if (score > 2) {
            poids = 3;
        } else if ((score >= 0)) {
            poids = 2;
        } else {
            poids = 1;
        }

        return poids;
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        ArrayList<PaireChaineEntier> newDico = initDico(depeches, categorie); // initialise liste dictionnaire des depeches d'une cat (creation lexique)
        calculScores(depeches, categorie, newDico);// calc des depeches
        triVecteurFusion(newDico);// trie dictionnaire

        try (FileWriter file = new FileWriter(nomFichier)) {
            for (PaireChaineEntier paire : newDico) {
                file.write(paire.getChaine() + ":" + poidsPourScore(paire.getEntier()) + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        Scanner lecteur = new Scanner(System.in);

        //Chargement des dépêches en mémoire
        System.out.println("-------------------------------------------------------\nDEBUT DU PROGRAMME DE PRESENTATION\n-------------------------------------------------------\n");
        System.out.println("---------\nPARTIE I\n---------\n");

        long startTime = System.currentTimeMillis();
        ArrayList<Depeche> depeches = lectureDepeches("depeches.txt");
        ArrayList<Depeche> depeches_test = lectureDepeches("./test.txt");


        Categorie lexique_sports = new Categorie("SPORTS"); // Création d'un objet 'Catégorie' de nom 'sports'
        Categorie lexique_politique = new Categorie("POLITIQUE"); // Création d'un objet 'Catégorie' de nom 'politique'
        Categorie lexique_economie = new Categorie("ECONOMIE"); // Création d'un objet 'Catégorie' de nom 'economie'
        Categorie lexique_environement_science = new Categorie("ENVIRONNEMENT-SCIENCES"); // Création d'un objet 'Catégorie' de nom 'environement_science'
        Categorie lexique_culture = new Categorie("CULTURE"); // Création d'un objet 'Catégorie' de nom 'culture'

        System.out.println("1- INITIALISATION DES LEXIQUES\n------------------------------\n");

        ArrayList<Categorie> cat_all = new ArrayList<>(Arrays.asList(lexique_politique, lexique_economie, lexique_sports, lexique_environement_science, lexique_culture));

        lexique_politique.initLexique("./lexique_politique");// Fonction initLexique appelé pour injecter les lexiques_politique dans politique
        lexique_economie.initLexique("./lexique_economie");// Fonction initLexique appelé pour injecter les lexiques_economie dans economie
        lexique_sports.initLexique("./lexique_sports");// Fonction initLexique appelé pour injecter les lexiques_sports dans sports
        lexique_environement_science.initLexique("lexique_environnement-sciences");// Fonction initLexique appelé pour injecter les lexiques_environement_science dans environement_science
        lexique_culture.initLexique("./lexique_culture");// Fonction initLexique appelé pour injecter les lexiques_culture dans culture
        long endTime = System.currentTimeMillis();


        System.out.println("Lexique économie :");
        int p = 0;
        while (p < lexique_economie.getLexique().size()) {
            System.out.println(lexique_economie.getLexique().get(p).getChaine() + ": " + lexique_economie.getLexique().get(p).getEntier());
            p++;
        }


        System.out.println("\n2- CLASSEMENT DES DEPECHES\n---------------------------\n");
        long startTime3 = System.currentTimeMillis();
        classementDepeches(depeches, cat_all, "fichier-sortie-manuel.txt");
        long endTime3 = System.currentTimeMillis();

        System.out.println(" Les dépêches ont bien été classifiées, tous les résultats sont dans le fichier : fichier-sortie-manuel.txt");

        long init = (endTime - startTime);
        long classi = (endTime3 - startTime3);
        System.out.println("\n Le temps de l'initialisation des lexiques à pris :" + init + "ms");
        System.out.println(" Le temps de la classification des lexiques à pris :" + classi + "ms");
        System.out.println(" Temps total : " + (init+classi) + "ms");

        String encore;
        do {
            System.out.print("\nCONTINUEZ (o/n):");
            encore = lecteur.nextLine();

        } while (encore.compareTo("o") != 0);



//        ----------------------------------------
//        PARTIE 2
//        ----------------------------------------

        System.out.println("---------\nPARTIE II\n---------\n");

        long startTime2 = System.currentTimeMillis();

        generationLexique(depeches, "ENVIRONNEMENT-SCIENCES", "lexique_environnement-sciences_auto");
        generationLexique(depeches, "POLITIQUE", "lexique_politique_auto");
        generationLexique(depeches, "ECONOMIE", "lexique_economie_auto");
        generationLexique(depeches, "SPORTS", "lexique_sports_auto");
        generationLexique(depeches, "CULTURE", "lexique_culture_auto");

        lexique_politique.initLexique("lexique_politique_auto");// Fonction initLexique appelé pour injecter les lexiques_politique dans politique
        lexique_economie.initLexique("lexique_economie_auto");// Fonction initLexique appelé pour injecter les lexiques_economie dans economie
        lexique_sports.initLexique("lexique_sports_auto");// Fonction initLexique appelé pour injecter les lexiques_sports dans sports
        lexique_environement_science.initLexique("lexique_environnement-sciences_auto");// Fonction initLexique appelé pour injecter les lexiques_environement_science dans environement_science
        lexique_culture.initLexique("lexique_culture_auto");// Fonction initLexique appelé pour injecter les lexiques_culture dans culture

        long endTime2 = System.currentTimeMillis();


        System.out.println("1- INITIALISATION DES LEXIQUES\n------------------------------\n");
        System.out.println("Les lexiques ont bien été initialisés de façon automatique.\n");


        System.out.println("2- CLASSEMENT DES DEPECHES\n---------------------------\n");

        long startTime4 = System.currentTimeMillis();
        classementDepeches(depeches_test, cat_all, "fichier_sortie-automatique.txt");
        long endTime4 = System.currentTimeMillis();

        System.out.println("les dépêches ont bien été classifié, tous les résultats sont dans le fichier : fichier-sortie-automatique");

        init = (endTime2 - startTime2);
        classi = (endTime4 - startTime4);
        System.out.println("\n Le temps de l'initialisation des lexiques à pris :" + init + "ms");
        System.out.println(" Le temps de la classification des lexiques à pris :" + classi + "ms");
        System.out.println(" Temps total : " + (init+classi) + "ms");


        // temps traitement avant amélioration : 2234ms
        // temps traitement après ajout de tri fusion : 1645ms


    }


}