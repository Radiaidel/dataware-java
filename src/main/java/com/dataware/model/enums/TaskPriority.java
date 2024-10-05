package com.dataware.model.enums;

public enum TaskPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String label;

    TaskPriority(String label) {
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
    public static TaskPriority fromString(String priority) {
        for (TaskPriority taskPriority : TaskPriority.values()) {
            if (taskPriority.label.equalsIgnoreCase(priority)) {
                return taskPriority;
            }
        }
        throw new IllegalArgumentException("Unknown priority: " + priority);
    }
}
