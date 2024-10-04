package com.dataware.model.enums;

public enum TaskStatus {
    TO_DO("To Do"),
    DOING("Doing"),
    DONE("Done");

    private final String label;

    TaskStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    // Méthode pour obtenir l'énumération à partir d'une chaîne
    public static TaskStatus fromString(String status) {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.label.equalsIgnoreCase(status)) {
                return taskStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
