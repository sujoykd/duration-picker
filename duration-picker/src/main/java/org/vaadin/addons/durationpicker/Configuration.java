package org.vaadin.addons.durationpicker;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private static final int DEFAULT_STEP_VALUE = 1;

    private final List<DurationUnit> units;
    private int hoursStepValue;
    private int minutesStepValue;
    private int secondsStepValue;

    private String daysLabel;
    private String hoursLabel;
    private String minutesLabel;
    private String secondsLabel;

    public Configuration() {
        units = new ArrayList<>();
        hoursStepValue = DEFAULT_STEP_VALUE;
        minutesStepValue = DEFAULT_STEP_VALUE;
        secondsStepValue = DEFAULT_STEP_VALUE;

        daysLabel = "d";
        hoursLabel = "h";
        minutesLabel = "m";
        secondsLabel = "s";
    }

    public Configuration(List<DurationUnit> units) {
        this();
        this.units.addAll(units);
    }

    public void addUnit(DurationUnit durationUnit) {
        this.units.add(durationUnit);
    }

    public List<DurationUnit> getUnits() {
        return units;
    }

    public int getDaysStepValue() {
        return DEFAULT_STEP_VALUE;
    }

    public int getHoursStepValue() {
        return hoursStepValue;
    }

    public void setHoursStepValue(int hoursStepValue) {
        this.hoursStepValue = hoursStepValue;
    }

    public int getMinutesStepValue() {
        return minutesStepValue;
    }

    public void setMinutesStepValue(int minutesStepValue) {
        this.minutesStepValue = minutesStepValue;
    }

    public int getSecondsStepValue() {
        return secondsStepValue;
    }

    public void setSecondsStepValue(int secondsStepValue) {
        this.secondsStepValue = secondsStepValue;
    }

    public String getDaysLabel() {
        return daysLabel;
    }

    public void setDaysLabel(String daysLabel) {
        this.daysLabel = daysLabel;
    }

    public String getHoursLabel() {
        return hoursLabel;
    }

    public void setHoursLabel(String hoursLabel) {
        this.hoursLabel = hoursLabel;
    }

    public String getMinutesLabel() {
        return minutesLabel;
    }

    public void setMinutesLabel(String minutesLabel) {
        this.minutesLabel = minutesLabel;
    }

    public String getSecondsLabel() {
        return secondsLabel;
    }

    public void setSecondsLabel(String secondsLabel) {
        this.secondsLabel = secondsLabel;
    }
}
