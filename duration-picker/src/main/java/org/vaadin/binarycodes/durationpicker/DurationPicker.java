package org.vaadin.binarycodes.durationpicker;


import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

import com.vaadin.componentfactory.Popup;
import com.vaadin.componentfactory.PopupPosition;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

public class DurationPicker extends CustomField<Duration> {

    /* configurations */
    private final Configuration configuration;

    private final String textFieldId;
    private final Popup popup;
    private Registration popupCloseRegistration;

    /* the eventual value of this field */
    private DurationData value;

    public DurationPicker() {
        this(new Configuration(Arrays.asList(DurationUnit.values())));
    }

    public DurationPicker(final DurationUnit... units) {
        this(new Configuration(Arrays.asList(units)));
    }

    private DurationPicker(final Configuration configuration) {
        this.configuration = configuration;

        this.value = new DurationData();
        this.textFieldId = UUID.randomUUID().toString();

        this.popup = new Popup();
        this.popup.setFor(textFieldId);
        this.popup.setPosition(PopupPosition.BOTTOM);
        this.popup.setIgnoreTargetClick(true);
        this.add(popup);

        initView();
    }

    private void initView() {
        var field = new TextField("Duration");
        field.setId(textFieldId);
        field.setAllowedCharPattern("[0-9hdms]");
        field.addValueChangeListener(event -> this.value = new DurationData(event.getValue()));

        var popupButton = new Button(VaadinIcon.CLOCK.create(), clickEvent -> onPopupOpen(field));
        field.setSuffixComponent(popupButton);

        add(field);
    }

    private void onPopupOpen(TextField field) {
        this.popup.removeAll();
        if (this.popupCloseRegistration != null) {
            this.popupCloseRegistration.remove();
        }

        var dialog = new DurationPickerPopupView(value, configuration);
        this.popup.add(dialog);
        this.popup.show();

        this.popupCloseRegistration = popup.addPopupOpenChangedEventListener(event -> {
            value = dialog.getValue();
            field.setValue(value.toString());
            updateValue();
        });
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
        private final Configuration configuration;

        public Builder() {
            configuration = new Configuration();
        }

        public Builder days() {
            this.configuration.addUnit(DurationUnit.DAYS);
            return this;
        }

        public Builder hours() {
            this.configuration.addUnit(DurationUnit.HOURS);
            return this;
        }

        public Builder hours(int interval) {
            this.configuration.addUnit(DurationUnit.HOURS);
            this.configuration.setHourInterval(interval);
            return this;
        }

        public Builder minutes() {
            this.configuration.addUnit(DurationUnit.MINUTES);
            return this;
        }

        public Builder minutes(int interval) {
            this.configuration.addUnit(DurationUnit.MINUTES);
            this.configuration.setMinuteInterval(interval);
            return this;
        }

        public Builder seconds() {
            this.configuration.addUnit(DurationUnit.SECONDS);
            return this;
        }

        public Builder seconds(int interval) {
            this.configuration.addUnit(DurationUnit.SECONDS);
            this.configuration.setSecondsInterval(interval);
            return this;
        }

        public Builder customLabels(String daysLabel, String hoursLabel, String minutesLabel, String secondsLabel) {
            this.configuration.setDaysLabel(daysLabel);
            this.configuration.setHoursLabel(hoursLabel);
            this.configuration.setMinutesLabel(minutesLabel);
            this.configuration.setSecondsLabel(secondsLabel);
            return this;
        }

        public DurationPicker build() {
            return new DurationPicker(configuration);
        }
    }
}
