package org.vaadin.addons.durationpicker;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public enum DurationUnit {
    DAYS(ChronoUnit.DAYS, Integer.MAX_VALUE, DurationData::getDays, DurationData::setDays),
    HOURS(ChronoUnit.HOURS, 23, DurationData::getHours, DurationData::setHours),
    MINUTES(ChronoUnit.MINUTES, 59, DurationData::getMinutes, DurationData::setMinutes),
    SECONDS(ChronoUnit.SECONDS, 59, DurationData::getSeconds, DurationData::setSeconds);

    private static final DurationUnit[] staticValues = values();
    private final ChronoUnit chronoUnit;
    private final int max;
    private final Function<DurationData, Long> valueSupplier;
    private final BiConsumer<DurationData, Long> valueConsumer;

    DurationUnit(ChronoUnit chronoUnit, int max, Function<DurationData, Long> valueSupplier, BiConsumer<DurationData, Long> valueConsumer) {
        this.chronoUnit = chronoUnit;
        this.max = max;
        this.valueSupplier = valueSupplier;
        this.valueConsumer = valueConsumer;
    }

    public ChronoUnit getChronoUnit() {
        return chronoUnit;
    }

    public int getMax() {
        return max;
    }

    public Function<DurationData, Long> getValueSupplier() {
        return valueSupplier;
    }

    public BiConsumer<DurationData, Long> getValueConsumer() {
        return valueConsumer;
    }

    public Optional<DurationUnit> getNextUnit() {
        var nextOrdinal = this.ordinal() + 1;
        if (nextOrdinal == staticValues.length) {
            return Optional.empty();
        }
        var next = DurationUnit.staticValues[nextOrdinal];
        return Optional.of(next);
    }
}
