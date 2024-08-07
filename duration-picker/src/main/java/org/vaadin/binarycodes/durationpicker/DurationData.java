package org.vaadin.binarycodes.durationpicker;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class DurationData {
    /* patterns for the manual input */
    private static final String DURATION_PATTERN_REGEX = "(?:(\\d+)d)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?(\\d+)?";
    private static final Pattern DURATION_PATTERN = Pattern.compile(DURATION_PATTERN_REGEX);

    private long days;
    private long hours;
    private long minutes;
    private long seconds;

    public DurationData() {
        this.clear();
    }

    public DurationData(Duration duration) {
        this.days = duration.toDaysPart();
        this.hours = duration.toHoursPart();
        this.minutes = duration.toMinutesPart();
        this.seconds = duration.toSecondsPart();
    }

    public DurationData(String durationString) {
        /* reset previous value if any */
        clear();

        if (StringUtils.isBlank(durationString)) {
            return;
        }

        var matcher = DURATION_PATTERN.matcher(durationString);
        if (!matcher.matches()) {
            return;
        }

        var wrapper = new Object() {
            Consumer<Long> unmatchedValueConsumer = null;
        };

        processMatchedGroup(matcher.group(1), this::setDays, this::setHours).ifPresent(u -> wrapper.unmatchedValueConsumer = u);
        processMatchedGroup(matcher.group(2), this::setHours, this::setMinutes).ifPresent(u -> wrapper.unmatchedValueConsumer = u);
        processMatchedGroup(matcher.group(3), this::setMinutes, this::setSeconds).ifPresent(u -> wrapper.unmatchedValueConsumer = u);
        processMatchedGroup(matcher.group(4), this::setSeconds, null).ifPresent(u -> wrapper.unmatchedValueConsumer = u);

        /* handle the optional last number */
        if (matcher.group(5) != null) {
            if (wrapper.unmatchedValueConsumer != null) {
                var lastMatchedValue = Long.parseLong(matcher.group(5));
                wrapper.unmatchedValueConsumer.accept(lastMatchedValue);
            } else {
                /* not expecting value without unit here */
                clear();
            }
        }
    }

    private Optional<Consumer<Long>> processMatchedGroup(String matchedValue, Consumer<Long> valueConsumer, Consumer<Long> unmatchedValueConsumer) {
        if (matchedValue != null) {
            var value = Long.parseLong(matchedValue);
            valueConsumer.accept(value);
            return unmatchedValueConsumer != null ? Optional.of(unmatchedValueConsumer) : Optional.empty();
        }
        return Optional.empty();
    }

    public void clear() {
        this.days = 0;
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
    }

    public Duration getDuration() {
        return Duration.ofDays(days).plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        if (days > 0) {
            builder.append("%dd".formatted(this.days));
        }
        if (hours > 0) {
            builder.append("%dh".formatted(this.hours));
        }
        if (minutes > 0) {
            builder.append("%dm".formatted(this.minutes));
        }
        if (seconds > 0) {
            builder.append("%ds".formatted(this.seconds));
        }
        return builder.toString();
    }
}
