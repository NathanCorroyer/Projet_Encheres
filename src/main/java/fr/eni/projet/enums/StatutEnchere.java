package fr.eni.projet.enums;

public enum StatutEnchere {
	PAS_COMMENCEE(0),
    EN_COURS(1),
    CLOTUREE(2),
    LIVREE(3),
	ANNULEE(100);

    private final int value;

    // Constructeur
    StatutEnchere(int value) {
        this.value = value;
    }

    // Méthode pour récupérer la valeur
    public int getValue() {
        return value;
    }
        
    public static StatutEnchere fromValue(int value) {
        for (StatutEnchere statut : StatutEnchere.values()) {
            if (statut.value == value) {
                return statut;
            }
        }
        throw new IllegalArgumentException("Valeur inconnue : " + value);
    }
}
