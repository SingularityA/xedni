package ru.spbu.xedni;

import java.nio.charset.StandardCharsets;

public class Helpers {
    public static String utf8(String str) {
        return new String(str.getBytes(), StandardCharsets.UTF_8);
    }
}
