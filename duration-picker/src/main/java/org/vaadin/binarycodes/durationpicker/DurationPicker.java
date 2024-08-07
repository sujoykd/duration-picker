package org.vaadin.binarycodes.durationpicker;


import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;

public class DurationPicker extends CustomField<Duration> {


    /* configurations */
    private final List<DurationUnit> units;
    private final int hourInterval;
    private final int minuteInterval;
    private final int secondsInterval;

    /* the eventual value of this field */
    private DurationData value;

    public DurationPicker() {
        this(1, 1, 1, Arrays.asList(DurationUnit.values()));
    }

    public DurationPicker(DurationUnit... units) {
        this(1, 1, 1, Arrays.asList(units));
    }

    private DurationPicker(int hourInterval, int minuteInterval, int secondsInterval, List<DurationUnit> units) {
        this.hourInterval = hourInterval;
        this.minuteInterval = minuteInterval;
        this.secondsInterval = secondsInterval;
        this.units = units;

        this.value = new DurationData();
        initView();
    }

    private void initView() {
        var field = new TextField("Duration");
        field.setAllowedCharPattern("[0-9hdms]");
        field.addValueChangeListener(event -> this.value = new DurationData(event.getValue()));

        var popupButton = new Button(VaadinIcon.TIMER.create(), clickEvent -> {

            var dialog = new DurationPickerDialog(value, units, hourInterval, minuteInterval, secondsInterval);
            dialog.open();

            var registration = dialog.addDialogCloseEventListener(event -> {
                value = event.getValue();
                field.setValue(value.toString());
                Notification.show("Received: " + value);
            });

            dialog.addOpenedChangeListener(event -> {
                field.setReadOnly(event.isOpened());
            });


        });
        field.setSuffixComponent(popupButton);

        add(field);
    }

    @Override
    protected Duration generateModelValue() {
        return value.getDuration();
    }

    @Override
    protected void setPresentationValue(Duration duration) {
        this.value = new DurationData(duration);
    }

    public static class Builder {
        private final List<DurationUnit> units;
        private int hourInterval;
        private int minuteInterval;
        private int secondsInterval;

        public Builder() {
            units = new ArrayList<>();
            hourInterval = 1;
            minuteInterval = 1;
            secondsInterval = 1;
        }

        public Builder days() {
            this.units.add(DurationUnit.DAYS);
            return this;
        }

        public Builder hours() {
            this.units.add(DurationUnit.HOURS);
            return this;
        }

        public Builder hours(int interval) {
            this.units.add(DurationUnit.HOURS);
            this.hourInterval = interval;
            return this;
        }

        public Builder minutes() {
            this.units.add(DurationUnit.MINUTES);
            return this;
        }

        public Builder minutes(int interval) {
            this.units.add(DurationUnit.MINUTES);
            this.minuteInterval = interval;
            return this;
        }

        public Builder seconds() {
            this.units.add(DurationUnit.SECONDS);
            return this;
        }

        public Builder seconds(int interval) {
            this.units.add(DurationUnit.SECONDS);
            this.secondsInterval = interval;
            return this;
        }

        public DurationPicker build() {
            return new DurationPicker(hourInterval, minuteInterval, secondsInterval, units);
        }
    }
}
