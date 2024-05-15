package cn.bootx.platform.baseapi.param.chinaword;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 敏感词导入参数
 * @author xxm
 * @since 2023/8/12
 */
@Data
public class ChinaWordImportParam {
    @ExcelProperty(value = "类型")
    private String type;
    @ExcelProperty("黑/白名单")
    private String whiteOrBlack;
    @ExcelProperty("敏感词")
    private String word;
}
