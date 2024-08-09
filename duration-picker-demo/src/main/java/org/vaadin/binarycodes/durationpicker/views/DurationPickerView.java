package org.vaadin.binarycodes.durationpicker.views;

import java.util.stream.Stream;

import org.vaadin.binarycodes.durationpicker.DurationPicker;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLayout;

@PageTitle("Duration Picker Demo")
@Route(value = "durationPicker")
@RouteAlias(value = "")
public class DurationPickerView extends VerticalLayout implements RouterLayout {

    public DurationPickerView() {
        var readOnlyPicker = new DurationPicker.Builder().hours(2).minutes(10).seconds(30).customLabels("D", "H", "M", "S").build();
        readOnlyPicker.setReadOnly(true);

        var disabledPicker = new DurationPicker.Builder().hours(2).minutes(10).seconds(30).customLabels("D", "H", "M", "S").build();
        disabledPicker.setEnabled(false);

        Stream.of(
                new DurationPicker(),
                new DurationPicker.Builder().hours().minutes().seconds().build(),
                new DurationPicker.Builder().hours(2).minutes(10).seconds(30).customLabels("D", "H", "M", "S").build(),
                readOnlyPicker,
                disabledPicker
        ).forEach(this::commonSetup);
    }

    private void commonSetup(DurationPicker durationPicker) {
        var data = new DataBean();

        var binder = new Binder<DataBean>();
        binder.forField(durationPicker)
                .asRequired()
                .bind(DataBean::getDuration, DataBean::setDuration);
        binder.setBean(data);

        var button = new Button("Click me", event -> {
            if (binder.validate().isOk()) {
                Notification.show(data.getDuration().toString());
            }
        });

        var layout = new HorizontalLayout(durationPicker, button);
        layout.setAlignItems(Alignment.BASELINE);
        add(layout);
    }
}
