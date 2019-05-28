package com.ten.aditum.back.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 展示数量最多的X个标签，以及他们的数量
 */
@Data
@Accessors(chain = true)
public class BasicLabelData {

    private List<LabelCount> labelCountList;

    @Data
    public static class LabelCount {
        private String labelName;
        private int labelCount;
    }
}
