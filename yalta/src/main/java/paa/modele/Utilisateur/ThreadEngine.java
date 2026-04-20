package paa.modele.Utilisateur;

import java.util.concurrent.Callable;

import paa.modele.plateau.Plateau;


/**
 * Classe représentant un thread pour exécuter l'algorithme MinMax de l'IA.
 */
public class ThreadEngine implements Callable<Integer> {
    private final Plateau plateau;
    private final Coup startcoup;
    private final int profondeur;
    private final IAEngine iaEngine;

    public ThreadEngine(Plateau plateau, Coup startcoup, int profondeur, IAEngine iaEngine) {
        this.plateau = plateau;
        this.startcoup = startcoup;
        this.profondeur = profondeur;
        this.iaEngine = iaEngine;
    }

    @Override
    public Integer call() {
        return iaEngine.evaluerCoup(plateau, startcoup, profondeur);
    }
}
