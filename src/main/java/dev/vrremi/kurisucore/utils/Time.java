package dev.vrremi.kurisucore.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Time {

    public static long timeToMillis(String string) {
        Pattern inputPattern = Pattern.compile("(\\d*w)?(\\d*d)?(\\d*h)?(\\d*m)?(\\d*s)?");
        if (inputPattern.matcher(string).matches()) {
            List<String> numbers = new ArrayList<>(Arrays.asList(string.split("[wdhms]")));
            List<String> times = new ArrayList<>(Arrays.asList(string.split("\\d+")));
            times.removeIf(String::isEmpty);
            long time = 0;
            for (int i = 0; i < numbers.size(); i++) {
                String spec = times.get(i);
                int amount = Integer.parseInt(numbers.get(i));
                switch (spec) {
                    case "w":
                        time += (long) amount * 604800L;
                        break;
                    case "d":
                        time += (long) amount * 86400L;
                        break;
                    case "h":
                        time += (long) amount * 3600L;
                        break;
                    case "m":
                        time += (long) amount * 60L;
                        break;
                    case "s":
                        time += amount;
                        break;
                }
            }
            return time * 1000L;
        }
        return 0;
    }
    
}
