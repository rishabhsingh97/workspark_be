package com.workspark.nominationservice.enums;

/**
 * Enum to manage application-wide constant messages.
 */
public enum Constants {

    CATEGORY_NOT_FOUND("Category not found with ID: %s"),
    QUESTION_NOT_FOUND("Question not found for type: %s");

    private final String message;

    // Constructor for the enum
    Constants(String message) {
        this.message = message;
    }

    /**
     * Returns the formatted message with arguments.
     *
     * @param args arguments to format the message
     * @return formatted message
     */
    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
