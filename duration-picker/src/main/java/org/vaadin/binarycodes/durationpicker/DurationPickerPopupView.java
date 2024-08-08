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

        var allowUnlimited = true;
        if (durationUnits.contains(DurationUnit.DAYS)) {
            var field = addInputField(configuration.getDaysLabel(), 1, 0);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getDays, DurationData::setDays);
            allowUnlimited = false;
        }
        if (durationUnits.contains(DurationUnit.HOURS)) {
            var max = allowUnlimited ? 0 : DurationUnit.HOURS.getMax();
            var field = addInputField(configuration.getHoursLabel(), configuration.getHourInterval(), max);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getHours, DurationData::setHours);
            allowUnlimited = false;
        }
        if (durationUnits.contains(DurationUnit.MINUTES)) {
            var max = allowUnlimited ? 0 : DurationUnit.MINUTES.getMax();
            var field = addInputField(configuration.getMinutesLabel(), configuration.getMinuteInterval(), max);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getMinutes, DurationData::setMinutes);
            allowUnlimited = false;
        }
        if (durationUnits.contains(DurationUnit.SECONDS)) {
            var max = allowUnlimited ? 0 : DurationUnit.SECONDS.getMax();
            var field = addInputField(configuration.getSecondsLabel(), configuration.getSecondsInterval(), max);
            binder.forField(field).withConverter(new IntegerToLongConverter()).bind(DurationData::getSeconds, DurationData::setSeconds);
            allowUnlimited = false;
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

        field.getElement().addEventListener("mouseWheelUp", event -> {
            var updatedValue = field.getValue() + field.getStep();
            if (updatedValue >= field.getMin() && updatedValue <= field.getMax()) {
                field.setValue(updatedValue);
            }
        });

        field.getElement().addEventListener("mouseWheelDown", event -> {
            var updatedValue = field.getValue() - field.getStep();
            if (updatedValue >= field.getMin() && updatedValue <= field.getMax()) {
                field.setValue(updatedValue);
            }
        });

        var dispatchMouseWheelEvents = """
                const field = $0;
                field.addEventListener('wheel', function(event) {
                    event.preventDefault(); // Prevent default scrolling
                    if (event.deltaY < 0) { // Mouse wheel up
                       field.dispatchEvent(new Event('mouseWheelUp'));
                    } else { // Mouse wheel down
                        field.dispatchEvent(new Event('mouseWheelDown'));
                    }
                });
                """;
        UI.getCurrent().getPage().executeJs(dispatchMouseWheelEvents, field.getElement());

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
