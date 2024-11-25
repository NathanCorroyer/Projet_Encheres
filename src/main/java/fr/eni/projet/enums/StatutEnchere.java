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
}
