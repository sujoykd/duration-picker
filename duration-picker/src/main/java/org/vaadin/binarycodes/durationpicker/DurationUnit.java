package org.vaadin.binarycodes.durationpicker;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

public enum DurationUnit {
    DAYS(ChronoUnit.DAYS),
    HOURS(ChronoUnit.HOURS),
    MINUTES(ChronoUnit.MINUTES),
    SECONDS(ChronoUnit.SECONDS);

    private static final DurationUnit[] staticValues = values();
    private final ChronoUnit chronoUnit;

    DurationUnit(ChronoUnit chronoUnit) {
        this.chronoUnit = chronoUnit;
    }

    public ChronoUnit getChronoUnit() {
        return chronoUnit;
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
