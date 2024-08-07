package org.vaadin.binarycodes.durationpicker;


import java.util.List;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class DurationPickerDialog extends Dialog {

    private final List<DurationUnit> durationUnits;
    private final int hourInterval;
    private final int minuteInterval;
    private final int secondsInterval;

    private final Binder<DurationData> binder;

    public DurationPickerDialog(DurationData durationData, List<DurationUnit> durationUnits, int hourInterval, int minuteInterval, int secondsInterval) {
        this.durationUnits = durationUnits;
        this.hourInterval = hourInterval;
        this.minuteInterval = minuteInterval;
        this.secondsInterval = secondsInterval;

        this.binder = new Binder<>(DurationData.class);
        this.binder.setBean(durationData);

        init();

        addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                fireCloseEvent();
            }
        });
    }

    private void init() {
        if (durationUnits.contains(DurationUnit.DAYS)) {
            var field = addInputField("Days", 1);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getDays, DurationData::setDays);
        }
        if (durationUnits.contains(DurationUnit.HOURS)) {
            var field = addInputField("Hours", this.hourInterval);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getHours, DurationData::setHours);
        }
        if (durationUnits.contains(DurationUnit.MINUTES)) {
            var field = addInputField("Minutes", this.minuteInterval);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getMinutes, DurationData::setMinutes);
        }
        if (durationUnits.contains(DurationUnit.SECONDS)) {
            var field = addInputField("Seconds", this.secondsInterval);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getSeconds, DurationData::setSeconds);
        }
    }

    private IntegerField addInputField(String label, int interval) {
        var field = new IntegerField(label);
        field.setThemeName("duration-picker");
        field.setStepButtonsVisible(true);
        field.setStep(interval);
        field.setMin(0);

        add(field);

        return field;
    }

    private DurationData getValue() {
        var data = binder.getBean();
        /* the object is recreated from its own representation of the Duration instance
           this is required to ensure proper rollover, that is 36h is converted to 1d12h */
        return new DurationData(data.getDuration());
    }

    private void fireCloseEvent() {
        var value = getValue();
        fireEvent(new DialogCloseEvent(this, value));
    }

    public Registration addDialogCloseEventListener(ComponentEventListener<DialogCloseEvent> listener) {
        return addListener(DialogCloseEvent.class, listener);
    }
}
