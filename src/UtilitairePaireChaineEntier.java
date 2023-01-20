import java.util.ArrayList;

public class UtilitairePaireChaineEntier {

    public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        //indice de chaine dans listePaires si chaine est présente et -1 sinon

        int i = 0;

        while (i < listePaires.size() && listePaires.get(i).getChaine().compareTo(chaine) != 0) {
            i++;
        }

        if (i < listePaires.size() && listePaires.get(i).getChaine().compareTo(chaine) == 0) {
            return i;
        }
        else {
            return -1;
        }
    }

    public static int entierPourChaineItt(ArrayList<PaireChaineEntier> listePaires, String chaine) {//iteratif

        for (PaireChaineEntier paire : listePaires) {//parcours vecteur listePaires
            if (paire.getChaine().equals(chaine)) {
                return paire.getEntier();//fonction simple donc return dans for. gagne lignes d'écritures
            }
        }
        return 0;// if chaine pas listePaire, return 0
    }

    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine, int gauche, int droite) {//rech dicho
        if (gauche > droite) { // verif si extreme gauche n'est pas > extreme droite
            return 0;
        }

            int milieu = (gauche + droite) / 2; //prend val de score
            int compare = chaine.compareTo(listePaires.get(milieu).getChaine());

            if (compare == 0) {
                return listePaires.get(milieu).getEntier(); //si mot = mot_milieu retourner val
            } else if (compare < 0) {
                return entierPourChaine(listePaires, chaine, gauche, milieu - 1); // si mot < mot_milieu va gauche
            } else {
                return entierPourChaine(listePaires, chaine, milieu + 1, droite); // si mot < mot_milieu va droite
            }
        }



    public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
        int ind_max = 0;//debut du vecteur eniter i (0)
        for (int i = 1; i < listePaires.size(); i++) {//Parcours vecteur listePaires
            if (listePaires.get(i).getEntier() > listePaires.get(ind_max).getEntier()) { // si entier i(1) > entier i(0)
                ind_max = i; // i(0) = i(1) donc avancer
            }
        }
        return listePaires.get(ind_max).getChaine(); //sinon return [mot de ind_max]
    }


    public static float moyenne(ArrayList<Integer> liste_compteur) {
        int somme = 0;
        for (int i : liste_compteur) { // parcours vecteur liste_compteur pour i < liste_compteur
            somme += i;
        }
        return (float) somme / liste_compteur.size(); // return val[moyenne]
    }


}
//recherche ittérative => recherche dicho