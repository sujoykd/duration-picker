package org.vaadin.binarycodes.durationpicker;

import com.vaadin.flow.component.ComponentEvent;

public class DialogCloseEvent extends ComponentEvent<DurationPickerDialog> {
    private final DurationData value;

    public DialogCloseEvent(DurationPickerDialog source, DurationData value) {
        super(source, false);
        this.value = value;
    }

    public DurationData getValue() {
        return value;
    }
}