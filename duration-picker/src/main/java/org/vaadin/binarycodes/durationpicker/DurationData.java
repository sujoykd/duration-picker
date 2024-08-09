package org.vaadin.binarycodes.durationpicker;

import java.time.Duration;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class DurationData {

    /* patterns for the manual input */
    private static final String DURATION_PATTERN_REGEX = "(?:(\\d+)d)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?(\\d+)?";
    private static final Pattern DURATION_PATTERN = Pattern.compile(DURATION_PATTERN_REGEX);
    private final Configuration configuration;

    private long days;
    private long hours;
    private long minutes;
    private long seconds;

    private boolean valid;

    public DurationData(Configuration configuration) {
        this.clear();
        this.configuration = configuration;
    }

    public DurationData(Configuration configuration, Duration duration) {
        this(configuration);
        this.days = duration.toDaysPart();
        this.hours = duration.toHoursPart();
        this.minutes = duration.toMinutesPart();
        this.seconds = duration.toSecondsPart();
    }

    public DurationData(Configuration configuration, String durationString) {
        this(configuration);
        this.valid = true;

        if (StringUtils.isBlank(durationString)) {
            return;
        }

        var matcher = DURATION_PATTERN.matcher(durationString);
        if (!matcher.matches()) {
            return;
        }

        var processData = new ProcessLocalData();
        processData = processMatchedGroup(processData, matcher.group(1), DurationUnit.DAYS);
        processData = processMatchedGroup(processData, matcher.group(2), DurationUnit.HOURS);
        processData = processMatchedGroup(processData, matcher.group(3), DurationUnit.MINUTES);
        processData = processMatchedGroup(processData, matcher.group(4), DurationUnit.SECONDS);

        /* handle the optional last number */
        if (processData.unmatchedUnit != null) {
            processMatchedGroup(processData, matcher.group(5), processData.unmatchedUnit);
        }
    }

    private int stepValueForUnit(DurationUnit unit) {
        return switch (unit) {
            case DAYS -> configuration.getDaysStepValue();
            case HOURS -> configuration.getHoursStepValue();
            case MINUTES -> configuration.getMinutesStepValue();
            case SECONDS -> configuration.getSecondsStepValue();
        };
    }

    private boolean isUnitExpected(DurationUnit unit) {
        return configuration.getUnits().contains(unit);
    }

    private ProcessLocalData processMatchedGroup(ProcessLocalData processData,
                                                 String matchedValue,
                                                 DurationUnit unit) {
        if (matchedValue == null || processData.valueRounded) {
            if (isUnitExpected(unit)) {
                /* if unit was expected but not present then the next unit should not be unlimited anymore */
                return new ProcessLocalData(processData.unmatchedUnit, processData.valueRounded, false);
            }
            return processData;
        }

        if (!isUnitExpected(unit)) {
            this.valid = false;
            return processData;
        }

        var stepValue = stepValueForUnit(unit);

        var value = Long.parseLong(matchedValue);
        var mod = value % stepValue;
        var valueToBeRounded = mod != 0;
        var finalValue = valueToBeRounded ? value + (stepValue - mod) : value;
        unit.getValueConsumer().accept(this, finalValue);

        if (!processData.allowUnlimited() && finalValue > unit.getMax()) {
            this.valid = false;
        }

        return new ProcessLocalData(unit.getNextUnit().orElse(null), valueToBeRounded, false);
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

    public boolean isValid() {
        return valid;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        if (days > 0) {
            /* handle rollover from hours */
            if (isUnitExpected(DurationUnit.DAYS)) {
                builder.append("%dd".formatted(this.days));
            } else {
                hours = hours + Duration.ofDays(days).toHours();
            }
        }
        if (hours > 0) {
            /* handle rollover from minutes */
            if (isUnitExpected(DurationUnit.HOURS)) {
                builder.append("%dh".formatted(this.hours));
            } else {
                minutes = minutes + Duration.ofHours(hours).toMinutes();
            }
        }
        if (minutes > 0) {
            /* handle rollover from seconds */
            if (isUnitExpected(DurationUnit.MINUTES)) {
                builder.append("%dm".formatted(this.minutes));
            } else {
                seconds = seconds + Duration.ofMinutes(minutes).toSeconds();
            }
        }
        if (seconds > 0) {
            builder.append("%ds".formatted(this.seconds));
        }
        return builder.toString();
    }

    private record ProcessLocalData(DurationUnit unmatchedUnit,
                                    boolean valueRounded,
                                    boolean allowUnlimited) {
        public ProcessLocalData() {
            this(null, false, true);
        }
    }
}
