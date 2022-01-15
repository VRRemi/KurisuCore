package dev.vrremi.kurisucore.utils;

public class Args {

    public static boolean matches(String argument, String... matches) {
        for (String match : matches) {
            if (argument.equalsIgnoreCase(match)) {
                return true;
            }
        }
        return false;
    }
}