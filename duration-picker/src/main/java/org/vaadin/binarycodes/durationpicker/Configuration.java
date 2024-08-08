package org.vaadin.binarycodes.durationpicker;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private final List<DurationUnit> units;
    private int hourInterval;
    private int minuteInterval;
    private int secondsInterval;

    private String daysLabel;
    private String hoursLabel;
    private String minutesLabel;
    private String secondsLabel;

    public Configuration() {
        units = new ArrayList<>();
        hourInterval = 1;
        minuteInterval = 1;
        secondsInterval = 1;

        daysLabel = "DD";
        hoursLabel = "HH";
        minutesLabel = "MI";
        secondsLabel = "S";
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

    public int getHourInterval() {
        return hourInterval;
    }

    public void setHourInterval(int hourInterval) {
        this.hourInterval = hourInterval;
    }

    public int getMinuteInterval() {
        return minuteInterval;
    }

    public void setMinuteInterval(int minuteInterval) {
        this.minuteInterval = minuteInterval;
    }

    public int getSecondsInterval() {
        return secondsInterval;
    }

    public void setSecondsInterval(int secondsInterval) {
        this.secondsInterval = secondsInterval;
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
