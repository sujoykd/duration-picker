package org.vaadin.binarycodes.durationpicker;

import com.vaadin.flow.component.ComponentEvent;

public class DialogCloseEvent extends ComponentEvent<DurationPickerPopupView> {
    private final DurationData value;

    public DialogCloseEvent(DurationPickerPopupView source, DurationData value) {
        super(source, false);
        this.value = value;
    }

    public DurationData getValue() {
        return value;
    }
}