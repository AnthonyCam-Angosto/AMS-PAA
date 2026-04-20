package paa.controler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import paa.modele.plateau.Case;
import paa.modele.plateau.Plateau;
import paa.vue.PromotionPopupView;

public class PromotionControlleur implements EventHandler<ActionEvent> {
    private final Group group;
    private final PromotionPopupView popup;
    private final Case casePromotion;
    private final String type;

    public PromotionControlleur(Group group, PromotionPopupView popup, Case casePromotion, String type) {
        this.group = group;
        this.popup = popup;
        this.casePromotion = casePromotion;
        this.type = type;
    }
    

    private void finaliserPromotion(Group group, PromotionPopupView popup, Case casePromotion, String type) {
        group.getChildren().remove(popup);
        Plateau.getInstance().finPromotion(casePromotion, type);
    }

    @Override
    public void handle(ActionEvent arg0) {
        finaliserPromotion(group, popup, casePromotion, type);
    }
}
