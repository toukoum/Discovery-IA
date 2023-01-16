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

    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        int i = 0;// inariant

        while (i < listePaires.size() && listePaires.get(i).getChaine().compareTo(chaine) != 0) { // boucle parcours du vecteur tant que parametre != chaine soit le nom
            i++;// incrémentation
        }

        if (i < listePaires.size() && listePaires.get(i).getChaine().compareTo(chaine) == 0) { // si i < v.size(évite les index out of bound) et parametre chaine == chaine soit le nom
            return listePaires.get(i).getEntier();// retourner à l'index de liste pair son entier (son poids)
        }
        else {// sinon retourner 0
            return 0;
        }
    }


    public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
        int ind_max = 0;// indice max : on commence à la première chaine
        int i = 1;// i = 1 car on commence a comparé ind_max au suivant et non à lui même

        while (i < listePaires.size()) { // tant que i < vecteur qui contient les lexiques
            if (listePaires.get(i).getEntier() > listePaires.get(ind_max).getEntier()) { // si l'index du Vecteur qui est un (entier) est > à l'index_max du Vecteur qui est un aussi (entier)
                // alors ind_max = index de celui qui à un poids plus grand
                ind_max = i;
            }
            // incrémenter pour aller au suivant
            i++;
        }

        return listePaires.get(ind_max).getChaine(); // retourner à l'indice_max du Vecteur sa chaine (mot) ayant le plus grand poids(entier)
    }


    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {

        int i = 0;
        int somme = 0;

        while (i < listePaires.size()) {
            somme += listePaires.get(i).getEntier();
        }

        return (somme / listePaires.size());
    }

}
