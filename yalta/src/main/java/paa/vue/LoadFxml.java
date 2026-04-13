package paa.vue;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

/**
 * Interface pour les classes qui chargent leur contenu depuis un fichier FXML.
 * Fournit une méthode par défaut pour charger le FXML et des hooks pour personnaliser le processus de chargement.
 * Les classes implémentant cette interface doivent fournir le chemin du fichier FXML via getFxmlPath() et peuvent personnaliser le loader via configureLoader() et effectuer des actions après le chargement via afterLoad().
 */
public interface LoadFxml {

    /**
     * Charge le contenu depuis un fichier FXML.
     */
    default void loadFromFxml() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(getFxmlPath()));
        configureLoader(loader);
        try {
            Object loadedRoot = loader.load();
            afterLoad(loader, loadedRoot);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger " + getFxmlPath(), e);
        }
    }

    /**
     * Chemin de la ressource FXML a charger.
     */
    String getFxmlPath();

    /**
     * Hook de configuration du loader avant chargement.
     */
    default void configureLoader(FXMLLoader loader) {
        loader.setRoot(this);
        loader.setController(this);
    }

    /**
     * Hook appele apres chargement du FXML.
     */
    default void afterLoad(FXMLLoader loader, Object loadedRoot) {
        // Hook optionnel.
    }
}
