package com.xunye.core.em;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelValue implements Serializable {

    public static LabelValue instance(String label, String value) {
        LabelValue labelValue = new LabelValue();
        labelValue.setLabel(label);
        labelValue.setValue(value);

        labelValue.setKey(value);
        labelValue.setId(value);
        return labelValue;
    }

    /**
     * 为了兼容之前的四个参数
     */
    public LabelValue(String id, String key, String label, String value) {
        this.id = id;
        this.key = key;
        this.label = label;
        this.value = value;
    }

    private String id = "";
    private String key = "";

    private String label = "-";
    private String value = "";

    private List<LabelValue> children=new ArrayList<>();
    public String getKey() {
        return value;
    }

}
