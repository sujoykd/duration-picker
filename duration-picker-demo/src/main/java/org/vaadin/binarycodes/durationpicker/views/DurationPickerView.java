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

    private final Binder<DataBean> binder;
    private final DataBean data;

    public DurationPickerView() {
        data = new DataBean();

        var durationPicker = new DurationPicker();


        binder = new Binder<>();
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
