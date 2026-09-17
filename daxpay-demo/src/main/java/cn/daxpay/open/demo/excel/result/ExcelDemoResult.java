package cn.daxpay.open.demo.excel.result;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Excel 导出演示结果
///
/// 模拟商品实体，演示 @ExcelProperty / @ColumnWidth 注解用法。
/// 字段覆盖编号、文本、金额、状态、时间等常见列类型。
@Data
@Accessors(chain = true)
@Schema(title = "Excel 导出演示")
public class ExcelDemoResult {

    @ExcelProperty("商品编码")
    @ColumnWidth(16)
    private String productCode;

    @ExcelProperty("商品名称")
    @ColumnWidth(24)
    private String productName;

    @ExcelProperty("分类")
    @ColumnWidth(12)
    private String category;

    @ExcelProperty("单价(元)")
    @ColumnWidth(14)
    private String price;

    @ExcelProperty("库存")
    @ColumnWidth(10)
    private Integer stock;

    @ExcelProperty("状态")
    @ColumnWidth(10)
    private String status;

    @ExcelProperty("创建时间")
    @ColumnWidth(22)
    private String createTime;
}
