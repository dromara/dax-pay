package cn.daxpay.open.platform.common.excel;

import org.apache.fesod.sheet.ExcelWriter;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.write.metadata.WriteSheet;

import java.util.Collection;

/// # Excel 写出句柄(模块内部)
///
/// 由 [ExcelUtils] 创建并托管生命周期, 只暴露「按批写入」能力: 调用方拿不到 [ExcelWriter],
/// 也就无法提前关闭 IO 流; 底层 Fesod 类型只在本模块内出现, 后续换库时改动不外溢到业务模块。
public final class ExcelWriterWrapper {

    private final ExcelWriter excelWriter;

    private final WriteSheet writeSheet;

    private ExcelWriterWrapper(ExcelWriter excelWriter, WriteSheet writeSheet) {
        this.excelWriter = excelWriter;
        this.writeSheet = writeSheet;
    }

    /// 创建写出句柄(仅本模块可见)
    static ExcelWriterWrapper of(ExcelWriter excelWriter, String sheetName) {
        return new ExcelWriterWrapper(excelWriter, FesodSheet.writerSheet(sheetName).build());
    }

    /// 写入一批数据到本导出任务的工作表
    ///
    /// @param data 数据集合
    public void write(Collection<?> data) {
        excelWriter.write(data, writeSheet);
    }
}
