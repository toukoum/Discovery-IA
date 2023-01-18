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

        FileWriter file = null;
        try {
            file = new FileWriter(nomFichier);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int c_politique = 0;
        int c_economie = 0;
        int c_sports = 0;
        int c_environement = 0;
        int c_culture = 0;


        for (Depeche depeche : depeches) {
            ArrayList<PaireChaineEntier> score = new ArrayList<>();
            for (Categorie categorie : cat_all) {
                PaireChaineEntier paire = new PaireChaineEntier(categorie.getNom(), categorie.score(depeche));
                score.add(paire);
            }
            String resultat = UtilitairePaireChaineEntier.chaineMax(score);
            try {
                file.write(depeche.getId() + ":" + resultat + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (resultat.compareTo(depeche.getCategorie()) == 0) {
                if (depeche.getCategorie().compareTo("POLITIQUE") == 0) {
                    c_politique += 1;
                } else if (depeche.getCategorie().compareTo("ECONOMIE") == 0) {
                    c_economie += 1;
                } else if (depeche.getCategorie().compareTo("SPORTS") == 0) {
                    c_sports += 1;
                } else if (depeche.getCategorie().compareTo("ENVIRONNEMENT-SCIENCES") == 0) {
                    c_environement += 1;
                } else if (depeche.getCategorie().compareTo("CULTURE") == 0) {
                    c_culture += 1;
                }
            }
        }

        ArrayList<Integer> liste_compteur = new ArrayList<>(Arrays.asList(c_politique, c_economie, c_sports, c_environement, c_culture));
        for (Integer compteur : liste_compteur) {
            System.out.println(compteur);
        }
        try {
            file.write("POLITIQUE:" + c_politique + "%\n");
            file.write("ECONOMIE:" + c_economie + "%\n");
            file.write("SPORTS:" + c_sports + "%\n");
            file.write("ENVIRONNEMENT-SCIENCES:" + c_environement + "%\n");
            file.write("CULTURE:" + c_culture + "%\n");
            file.write("MOYENNE:" + UtilitairePaireChaineEntier.moyenne(liste_compteur) + "%\n");
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
        for (PaireChaineEntier paire : dictionnaire) {
            for (Depeche depeche : depeches) {
                if (depeche.getCategorie().equals(categorie) && depeche.getMots().contains(paire.getChaine())) {
                    paire.setEntier(paire.getEntier() + 1);
                } else if (!depeche.getCategorie().equals(categorie) && depeche.getMots().contains(paire.getChaine())) {
                    paire.setEntier(paire.getEntier() - 2);
                }
            }
        }
    }

    public static void triVecteurFusion(ArrayList<PaireChaineEntier> atrie) {
        if (atrie.size() > 1) {
            ArrayList<PaireChaineEntier> gauche = new ArrayList<PaireChaineEntier>();
            ArrayList<PaireChaineEntier> droite = new ArrayList<PaireChaineEntier>();
            int milieu = atrie.size() / 2;
            for (int i = 0; i < milieu; i++) {
                gauche.add(atrie.get(i));
            }
            for (int i = milieu; i < atrie.size(); i++) {
                droite.add(atrie.get(i));
            }
            triVecteurFusion(gauche);
            triVecteurFusion(droite);
            fusion(gauche, droite, atrie);
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
        } else if ((score >= 0) && (score <= 2)) {
            poids = 2;
        } else {
            poids = 1;
        }

        return poids;
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        ArrayList<PaireChaineEntier> newDico = initDico(depeches, categorie);
        calculScores(depeches, categorie, newDico);
        triVecteurFusion(newDico);

        try (FileWriter file = new FileWriter(nomFichier)) {
            for (PaireChaineEntier paire : newDico) {
                file.write(paire.getChaine() + ":" + poidsPourScore(paire.getEntier()) + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        //Chargement des dépêches en mémoire
        System.out.println("chargement des dépêches");
        ArrayList<Depeche> depeches = lectureDepeches("depeches.txt");
        ArrayList<Depeche> depeches_test = lectureDepeches("./test.txt");


        Categorie lexique_sports = new Categorie("SPORTS"); // Création d'un objet 'Catégorie' de nom 'sports'
        Categorie lexique_politique = new Categorie("POLITIQUE"); // Création d'un objet 'Catégorie' de nom 'politique'
        Categorie lexique_economie = new Categorie("ECONOMIE"); // Création d'un objet 'Catégorie' de nom 'economie'
        Categorie lexique_environement_science = new Categorie("ENVIRONNEMENT-SCIENCES"); // Création d'un objet 'Catégorie' de nom 'environement_science'
        Categorie lexique_culture = new Categorie("CULTURE"); // Création d'un objet 'Catégorie' de nom 'culture'


        ArrayList<Categorie> cat_all = new ArrayList<>(Arrays.asList(lexique_politique, lexique_economie, lexique_sports, lexique_environement_science, lexique_culture));
//
//        lexique_politique.initLexique("./lexique_politique");// Fonction initLexique appelé pour injecter les lexiques_politique dans politique
//        lexique_economie.initLexique("./lexique_economie");// Fonction initLexique appelé pour injecter les lexiques_economie dans economie
//        lexique_sports.initLexique("./lexique_sports");// Fonction initLexique appelé pour injecter les lexiques_sports dans sports
//        lexique_environement_science.initLexique("lexique_environnement-sciences");// Fonction initLexique appelé pour injecter les lexiques_environement_science dans environement_science
//        lexique_culture.initLexique("./lexique_culture");// Fonction initLexique appelé pour injecter les lexiques_culture dans culture

//        int p = 0;
//        while (p < lexique_economie.getLexique().size()) {
//            System.out.println(lexique_economie.getLexique().get(p).getChaine() + ":" + lexique_economie.getLexique().get(p).getEntier());
//           p++;
//        }
//
//
//        ArrayList<PaireChaineEntier> Score = new ArrayList<>();
//        ArrayList<String> catégorie = new ArrayList<>(Arrays.asList("lexique_politique", "lexique_économie", "lexique_sports", "lexique_environement_science", "lexique_culture"));
//        for (int i = 0; i < cat_all.size(); i++) {
//            PaireChaineEntier paire_x = new PaireChaineEntier(catégorie.get(i), cat_all.get(i).score(depeches.get(201)));
//            Score.add(paire_x);
//        }
//
////
////
//        int i = 0;// initialisation invariant
//
//        while (i < Score.size()) { // tant que i est < au vecteur lexique
//            Score.get(i).afficher(); // affichage de l'élément i avec la valeur du text et du poid
//            i++;
//        }
//
//
//        System.out.println(UtilitairePaireChaineEntier.chaineMax(Score));


//        classementDepeches(depeches, cat_all, "fichier_sortie.txt");

//        afficahge de la fonction qui retourne l'entier associé à la chaine de caractères dans listePaires
//        int entPaire = UtilitairePaireChaineEntier.entierPourChaine(vlexique_culture, "art");
//        System.out.println(entPaire);


//        System.out.println(UtilitairePaireChaineEntier.chaineMax(vlexique_culture)); // appele la fonction qui renvoie la chaine avec le poids (entier) le plus grand

//        ----------------------------------------
//        PARTIE 2
//        ----------------------------------------

//        ArrayList<PaireChaineEntier> resultat = initDico(depeches, "SPORTS");
//        calculScores(depeches, "SPORTS", resultat);
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



        classementDepeches(depeches_test, cat_all, "fichier_sortie.txt");


        long endTime = System.currentTimeMillis();
        System.out.println("votre saisie a été réalisée en : " + (endTime - startTime) + "ms");




        // temps traitement avant amélioration : 2234ms

    }


}