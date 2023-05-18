package br.com.core.utils.timings;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimingUtils {

    public static String getTimeRemaining(Duration duration) {
        Instant now = Instant.now();
        Instant expiration = now.plus(duration);
        Duration remaining = Duration.between(now, expiration);
        long days = remaining.toDays();
        long hours = remaining.toHours() % 24;
        long minutes = remaining.toMinutes() % 60;
        return String.format("%d dias, %d horas, %d minutos", days, hours, minutes);
    }


    public static Duration parseDuration(String input) {
        Pattern pattern = Pattern.compile("(\\d+)([dhm])");
        Matcher matcher = pattern.matcher(input);
        long millis = 0;

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "d":
                    millis += TimeUnit.DAYS.toMillis(value);
                    break;
                case "h":
                    millis += TimeUnit.HOURS.toMillis(value);
                    break;
                case "m":
                    millis += TimeUnit.MINUTES.toMillis(value);
                    break;
                // adicione outros casos para outras unidades de tempo, se necessário
            }
        }

        return Duration.ofMillis(millis);
    }
}
