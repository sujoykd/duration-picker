package org.vaadin.binarycodes.durationpicker;


import java.util.List;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;

@CssImport("./styles/duration-picker-popup-view.css")
public class DurationPickerPopupView extends FlexLayout {

    private final List<DurationUnit> durationUnits;
    private final int hourInterval;
    private final int minuteInterval;
    private final int secondsInterval;

    private final Binder<DurationData> binder;

    private final FlexLayout wrapper;

    public DurationPickerPopupView(DurationData durationData, List<DurationUnit> durationUnits, int hourInterval, int minuteInterval, int secondsInterval) {
        this.durationUnits = durationUnits;
        this.hourInterval = hourInterval;
        this.minuteInterval = minuteInterval;
        this.secondsInterval = secondsInterval;

        this.binder = new Binder<>(DurationData.class);
        this.binder.setBean(durationData);

        this.wrapper = new FlexLayout();
        this.wrapper.addClassNames(LumoUtility.Gap.MEDIUM);
        this.add(this.wrapper);

        init();
        addClassNames(
                LumoUtility.Padding.Horizontal.LARGE,
                LumoUtility.Padding.Vertical.MEDIUM
        );
    }

    private void init() {
        if (durationUnits.contains(DurationUnit.DAYS)) {
            var field = addInputField("DD", 1);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getDays, DurationData::setDays);
        }
        if (durationUnits.contains(DurationUnit.HOURS)) {
            var field = addInputField("HH", this.hourInterval);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getHours, DurationData::setHours);
        }
        if (durationUnits.contains(DurationUnit.MINUTES)) {
            var field = addInputField("MI", this.minuteInterval);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getMinutes, DurationData::setMinutes);
        }
        if (durationUnits.contains(DurationUnit.SECONDS)) {
            var field = addInputField("S", this.secondsInterval);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getSeconds, DurationData::setSeconds);
        }
    }

    private IntegerField addInputField(String label, int interval) {
        var field = new IntegerField(label);
        field.addThemeNames("duration-picker");
        field.setStepButtonsVisible(true);
        field.setStep(interval);
        field.setMin(0);

        field.addKeyPressListener(event -> {
            if (event.getKey() == Key.ARROW_UP) {
                field.setValue(field.getValue() + field.getStep());
            } else if (event.getKey() == Key.ARROW_DOWN) {
                field.setValue(field.getValue() - field.getStep());
            }
        });

        // Define the JavaScript code using a text block
        String jsCode = """
                const field = $0;
                field.addEventListener('wheel', function(event) {
                    event.preventDefault(); // Prevent default scrolling
                    const step = parseInt(field.step || 1);
                    const currentValue = parseInt(field.value || 0);
                    if (event.deltaY < 0) { // Mouse wheel up
                        field.value = currentValue + step;
                    } else { // Mouse wheel down
                        field.value = Math.max(currentValue - step, 0);
                    }
                    field.dispatchEvent(new Event('change')); // Fire change event
                });
                """;
        UI.getCurrent().getPage().executeJs(jsCode, field.getElement());

        this.wrapper.add(field);

        return field;
    }

    public DurationData getValue() {
        var data = binder.getBean();
        /* the object is recreated from its own representation of the Duration instance
           this is required to ensure proper rollover, that is 36h is converted to 1d12h */
        return new DurationData(data.getDuration());
    }

    public void fireCloseEvent() {
        var value = getValue();
        fireEvent(new DialogCloseEvent(this, value));
    }

    public Registration addDialogCloseEventListener(ComponentEventListener<DialogCloseEvent> listener) {
        return addListener(DialogCloseEvent.class, listener);
    }
}
