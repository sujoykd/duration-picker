package org.vaadin.addons.durationpicker;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class IntegerToLongConverter implements Converter<Integer, Long> {

    @Override
    public Result<Long> convertToModel(Integer fieldValue, ValueContext context) {
        /* convert Integer to Long */
        if (fieldValue == null) {
            return Result.ok(0L);
        }
        return Result.ok(fieldValue.longValue());
    }

    @Override
    public Integer convertToPresentation(Long modelValue, ValueContext context) {
        /* convert Long to Integer */
        if (modelValue == null) {
            return 0;
        }
        return modelValue.intValue();
    }
}