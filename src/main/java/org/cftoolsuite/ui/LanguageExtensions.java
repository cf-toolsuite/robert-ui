package org.cftoolsuite.ui;

public record LanguageExtensions(String language, String extensions) {
    @Override
    public String toString() {
        return language;
    }
}
