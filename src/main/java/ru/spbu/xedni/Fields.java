package ru.spbu.xedni;

public enum Fields {

    URL("url"), TEXT("text");

    private final String name;

    Fields(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
