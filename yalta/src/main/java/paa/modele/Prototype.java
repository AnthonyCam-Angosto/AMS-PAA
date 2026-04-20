package paa.modele;

public interface Prototype<T> {
    /**
     * Crée une copie de l'objet actuel. La copie doit être indépendante de l'original, c'est-à-dire que les modifications apportées à la copie ne doivent pas affecter l'original et vice versa. Cette méthode est particulièrement utile pour les simulations de l'IA, où il est nécessaire de créer des copies temporaires de l'état du jeu pour évaluer différentes stratégies sans altérer l'état réel du jeu.
     * @return une nouvelle instance de T qui est une copie de l'objet actuel
     */
    public T copy();
}
