package paa.modele.Utilisateur;

public class Coup {
    public enum Type {
        NORMAL,
        ROQUE,
        PROMOTION
    }

    public String departId;
    public String arriveeId;
    public Type type;

    public Coup(String departId, String arriveeId) {
        this(departId, arriveeId, Type.NORMAL);
    }

    public Coup(String departId, String arriveeId, Type type) {
        this.departId = departId;
        this.arriveeId = arriveeId;
        this.type = type;
    }
}
