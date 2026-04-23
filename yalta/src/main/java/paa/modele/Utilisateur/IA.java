package paa.modele.Utilisateur;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import paa.modele.Couleur;
import paa.modele.plateau.Plateau;

/**
 * Représente une intelligence artificielle du jeu, basée sur Min-Max.
 */
public class IA extends Utilisateur {

    // La profondeur maximale du Min-Max
    private static final int PROFONDEUR_MINMAX = 3; //maximun 3 pour éviter les temps de calcul trop longs
    private final IAEngine iAEngine;

    public IA(Couleur couleur) {
        super(couleur);
        this.iAEngine = new MinimaxEngine(couleur);
    }

    @Override
    public void jouer() {
        Thread thread = new Thread(() -> {
            boolean coupJoue = jouerMeilleurCoup(PROFONDEUR_MINMAX);
            if (!coupJoue) {
                throw new RuntimeException("IA " + getCouleur() + " : aucun coup légal trouvé.");
            }
        });
        thread.start();
    }



    @Override
    public void echec() {
        Thread thread = new Thread(() -> {
            boolean coupJoue = jouerMeilleurCoup(PROFONDEUR_MINMAX+1);
            if (!coupJoue) {
                throw new RuntimeException("IA " + getCouleur() + " : aucun coup legal trouvé.");
            }
        });
        thread.start();
    }

    private Coup trouverMeilleurCoupThread(Plateau plateau, int profondeur) {
        List<Coup> coups = iAEngine.getLegalMoves(plateau, getCouleur());
        if (coups.isEmpty()) {
            return null;
        }

        int meilleurScore = Integer.MIN_VALUE;
        Coup meilleur = null;

        int nbProcs = Runtime.getRuntime().availableProcessors();
        if (coups.size() < nbProcs) {
            nbProcs = coups.size();
        }
        ExecutorService executor = new ThreadPoolExecutor(nbProcs, coups.size(), 1, TimeUnit.HOURS, new LinkedBlockingDeque<>());
        ArrayList<Callable<Integer>> threads = new ArrayList<>();
        for (Coup coup : coups) {
            ThreadEngine thread = new ThreadEngine(plateau, coup, profondeur, iAEngine);
            threads.add(thread);
        }
        try {
            List<Future<Integer>> results = executor.invokeAll(threads);
            shutdownAndAwaitTermination(executor);
            for (int i = 0; i < results.size(); i++) {
                try {
                    int threadScore = results.get(i).get();
                    //System.out.println("Score du thread : " + threadScore);
                    if (threadScore > meilleurScore) {
                        meilleurScore = threadScore;
                        meilleur = coups.get(i);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return meilleur;
                } catch (ExecutionException e) {
                    throw new IllegalStateException("Erreur lors du calcul Minimax d'un thread", e);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
        return meilleur;
    }


    private static void shutdownAndAwaitTermination(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            } 
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private boolean jouerMeilleurCoup(int profondeur) {
        Plateau etatInitial = Plateau.getInstance().copy();
        Coup meilleurCoup = trouverMeilleurCoupThread(etatInitial, profondeur);
        if (meilleurCoup == null) {
            System.out.println("IA " + getCouleur() + " : aucun coup légal trouvé.");
            return false;
        }

        Plateau plateauReel = Plateau.getInstance();
        Platform.runLater(() -> iAEngine.appliquerCoup(plateauReel, meilleurCoup, false));
        return true;
    }
}
