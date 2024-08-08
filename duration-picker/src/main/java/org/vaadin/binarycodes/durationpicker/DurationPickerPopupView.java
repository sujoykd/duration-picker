package org.vaadin.binarycodes.durationpicker;


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

    private final Configuration configuration;

    private final Binder<DurationData> binder;

    private final FlexLayout wrapper;


    public DurationPickerPopupView(DurationData value, Configuration configuration) {
        this.configuration = configuration;

        this.binder = new Binder<>(DurationData.class);
        this.binder.setBean(value);

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
        var durationUnits = configuration.getUnits().stream().sorted().toList();

        if (durationUnits.contains(DurationUnit.DAYS)) {
            var field = addInputField(configuration.getDaysLabel(), 1, 0);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getDays, DurationData::setDays);
        }
        if (durationUnits.contains(DurationUnit.HOURS)) {
            var field = addInputField(configuration.getHoursLabel(), configuration.getHourInterval(), 0);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getHours, DurationData::setHours);
        }
        if (durationUnits.contains(DurationUnit.MINUTES)) {
            var field = addInputField(configuration.getMinutesLabel(), configuration.getMinuteInterval(), 0);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getMinutes, DurationData::setMinutes);
        }
        if (durationUnits.contains(DurationUnit.SECONDS)) {
            var field = addInputField(configuration.getSecondsLabel(), configuration.getSecondsInterval(), 0);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getSeconds, DurationData::setSeconds);
        }
    }

    private IntegerField addInputField(String label, int interval, int max) {
        var field = new IntegerField(label);
        field.addThemeNames("duration-picker");
        field.setStepButtonsVisible(true);
        field.setStep(interval);
        field.setMin(0);
        if (max > 0) {
            field.setMax(max);
        }

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
