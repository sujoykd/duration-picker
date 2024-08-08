package org.vaadin.binarycodes.durationpicker;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

public enum DurationUnit {
    DAYS(ChronoUnit.DAYS, 0),
    HOURS(ChronoUnit.HOURS, 24),
    MINUTES(ChronoUnit.MINUTES, 60),
    SECONDS(ChronoUnit.SECONDS, 60);

    private static final DurationUnit[] staticValues = values();
    private final ChronoUnit chronoUnit;
    private final int max;

    DurationUnit(ChronoUnit chronoUnit, int max) {
        this.chronoUnit = chronoUnit;
        this.max = max;
    }

    public ChronoUnit getChronoUnit() {
        return chronoUnit;
    }

    public int getMax() {
        return max;
    }

    public Optional<ChronoUnit> getNextUnmatchedUnit() {
        var nextOrdinal = this.ordinal() + 1;
        if (nextOrdinal == staticValues.length) {
            return Optional.empty();
        }
        var next = DurationUnit.staticValues[nextOrdinal].getChronoUnit();
        return Optional.of(next);
    }
}
