package com.xunye.core.base;

import java.util.ArrayList;
import java.util.List;

import com.xunye.core.em.LabelValue;

public interface BaseEnum {

    String getLabel();

    Integer getValue();

    static List<LabelValue> makeLabelValues(BaseEnum[] enums) {
        List<LabelValue> labelValues = new ArrayList<>();
        LabelValue labelValue;
        for (BaseEnum baseEnum : enums) {
            labelValue = new LabelValue();
            labelValue.setLabel(baseEnum.getLabel());
            labelValue.setValue(String.valueOf(baseEnum.getValue()));
            labelValue.setKey(String.valueOf(baseEnum.getValue()));
            labelValues.add(labelValue);
        }
        return labelValues;
    }

    static Integer getEnumValue(BaseEnum baseEnum) {
        return baseEnum.getValue();
    }

}
