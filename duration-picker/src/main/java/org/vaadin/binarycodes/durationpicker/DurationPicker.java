package org.vaadin.binarycodes.durationpicker;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.regex.Pattern;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;

public class DurationPicker extends CustomField<Duration> {
    private static final String DURATION_PATTERN_REGEX = "(?:(\\d+)d)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?(\\d+)?";
    private static final Pattern DURATION_PATTERN = Pattern.compile(DURATION_PATTERN_REGEX);

    private Duration duration;

    public DurationPicker() {
        reset();
        initView();
    }

    private void initView() {
        var field = new TextField("Duration");
        field.setAllowedCharPattern("[0-9hdms]");
        field.addValueChangeListener(event -> {
            parseDurationString(event.getValue());
        });

        var popupButton = new Button(VaadinIcon.TIMER.create(), event -> {
            field.setReadOnly(true);
            Notification.show("open popup button");
        });
        field.setSuffixComponent(popupButton);

        add(field);
    }

    @Override
    protected Duration generateModelValue() {
        return duration;
    }

    @Override
    protected void setPresentationValue(Duration duration) {
        var days = duration.get(ChronoUnit.DAYS);
        var hours = duration.get(ChronoUnit.HOURS);
        var minutes = duration.get(ChronoUnit.MINUTES);
        var seconds = duration.get(ChronoUnit.SECONDS);

        this.duration = Duration.ofDays(days).plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }


    private void parseDurationString(String value) {
        /* reset previous value if any */
        reset();

        var matcher = DURATION_PATTERN.matcher(value);

        if (matcher.matches()) {
            var wrapper = new Object() {
                ChronoUnit unmatchedUnit = null;
            };

            processMatchedGroup(matcher.group(1), DurationUnit.DAYS).ifPresent(u -> wrapper.unmatchedUnit = u);
            processMatchedGroup(matcher.group(2), DurationUnit.HOURS).ifPresent(u -> wrapper.unmatchedUnit = u);
            processMatchedGroup(matcher.group(3), DurationUnit.MINUTES).ifPresent(u -> wrapper.unmatchedUnit = u);
            processMatchedGroup(matcher.group(4), DurationUnit.SECONDS).ifPresent(u -> wrapper.unmatchedUnit = u);

            /* handle the optional last number */
            if (matcher.group(5) != null) {
                if (wrapper.unmatchedUnit != null) {
                    var lastMatchedValue = Integer.parseInt(matcher.group(5));
                    duration = duration.plus(lastMatchedValue, wrapper.unmatchedUnit);
                } else {
                    /* not expecting value without unit here */
                    reset();
                }
            }
        }
    }

    private Optional<ChronoUnit> processMatchedGroup(String matchedValue, DurationUnit unit) {
        if (matchedValue != null) {
            var value = Integer.parseInt(matchedValue);
            duration = duration.plus(value, unit.getChronoUnit());
            return unit.getNextUnmatchedUnit();
        }
        return Optional.empty();
    }

    private void reset() {
        duration = Duration.ofSeconds(0);
    }
}
