package com.yuezhik.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SubjectData {
    @ExcelProperty(index=0)
    private String oneSubjectName;

    @ExcelProperty(index=1)
    private String twoSubjectName;
}
