package org.vaadin.binarycodes.durationpicker.views;

import org.vaadin.binarycodes.durationpicker.DurationPicker;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
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
        allFields();
        onlyHourMinuteSeconds();
    }

    private void allFields() {
        var data = new DataBean();

        var durationPicker = new DurationPicker();

        var binder = new Binder<DataBean>();
        binder.forField(durationPicker)
                .asRequired()
                .bind(DataBean::getDuration, DataBean::setDuration);
        binder.setBean(data);

        var button = new Button("Click me", event -> {
            Notification.show(data.getDuration().toString());
        });

        add(durationPicker, button);
    }

    private void onlyHourMinuteSeconds() {
        var data = new DataBean();

        var durationPicker = new DurationPicker.Builder().hours().minutes().seconds().build();

        var binder = new Binder<DataBean>();
        binder.forField(durationPicker)
                .asRequired()
                .bind(DataBean::getDuration, DataBean::setDuration);
        binder.setBean(data);

        var button = new Button("Click me", event -> {
            Notification.show(data.getDuration().toString());
        });

        add(durationPicker, button);
    }

}
