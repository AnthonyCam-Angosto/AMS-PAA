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

/** Représente une intelligence artificielle du jeu, basée sur Min-Max. */
public class IA extends Utilisateur {

    // La profondeur maximale du Min-Max
    private static final int PROFONDEUR_MINMAX = 4; //maximun 4 pour éviter les temps de calcul trop longs
    private final IAEngine iAEngine;

    public IA(Couleur couleur) {
        super(couleur);
        this.iAEngine = new AlphaBetaEngine(couleur);
    }

    /**
     * Fait jouer l'IA.
     */
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



    /**
     * Gère l'échec de l'IA en tentant de trouver le meilleur coup avec une profondeur augmentée.
     */
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

    /**
     * Trouve le meilleur coup en utilisant un thread pour chaque coup légal, afin de paralléliser le calcul de l'IA
     * @param plateau L'état actuel du plateau de jeu
     * @param profondeur La profondeur maximale du Min-Max
     * @return Le meilleur coup trouvé, ou null s'il n'y a aucun coup légal
     */
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
            long startTime = System.currentTimeMillis();
            List<Future<Integer>> results = executor.invokeAll(threads);
            shutdownAndAwaitTermination(executor);
            long endTime = System.currentTimeMillis();
            System.out.println("Temps de calcul : " + (endTime - startTime) + " ms");
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


    /**
     * Arrête proprement l'executor service en attendant la fin des tâches en cours.
     * @param executorService L'executor service à arrêter
     */
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

    /**
     * Fait jouer l'IA en trouvant le meilleur coup à jouer.
     * @param profondeur La profondeur maximale du Min-Max
     * @return true si un coup a été joué, false sinon
     */
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
