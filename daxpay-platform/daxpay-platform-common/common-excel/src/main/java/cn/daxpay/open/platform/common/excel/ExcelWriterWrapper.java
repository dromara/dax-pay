package cn.daxpay.open.platform.common.excel;

import org.apache.fesod.sheet.ExcelWriter;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.write.builder.ExcelWriterSheetBuilder;
import org.apache.fesod.sheet.write.metadata.WriteSheet;

import java.util.Collection;

/// # ExcelWriter 安全包装器
///
/// 提供受控的写出接口，避免直接暴露 {@link ExcelWriter} 导致 IO 流被提前关闭等不可控问题。
/// 仅在 {@link ExcelUtils#export(Class, String, java.io.OutputStream, java.util.function.Consumer)}
/// 的 consumer 回调中使用，writer 的生命周期由 ExcelUtils 统一管理。
///
/// @param excelWriter 底层 FastExcel 写出器
public record ExcelWriterWrapper<T>(ExcelWriter excelWriter) {

    /// 写入一批数据到指定 Sheet
    ///
    /// @param data       数据集合
    /// @param writeSheet 目标工作表
    public void write(Collection<T> data, WriteSheet writeSheet) {
        excelWriter.write(data, writeSheet);
    }

    /// 创建 ExcelWriterWrapper 实例
    public static <T> ExcelWriterWrapper<T> of(ExcelWriter excelWriter) {
        return new ExcelWriterWrapper<>(excelWriter);
    }

    /// 构建 WriteSheet（按名称）
    ///
    /// @param sheetName 工作表名称
    public static WriteSheet buildSheet(String sheetName) {
        return sheetBuilder(sheetName).build();
    }

    /// 构建 WriteSheet（按编号 + 名称）
    ///
    /// @param sheetNo   工作表编号
    /// @param sheetName 工作表名称
    public static WriteSheet buildSheet(int sheetNo, String sheetName) {
        return sheetBuilder(sheetNo, sheetName).build();
    }

    /// 获取 Sheet 构建器（按名称），支持链式配置
    public static ExcelWriterSheetBuilder sheetBuilder(String sheetName) {
        return FesodSheet.writerSheet(sheetName);
    }

    /// 获取 Sheet 构建器（按编号 + 名称），支持链式配置
    public static ExcelWriterSheetBuilder sheetBuilder(int sheetNo, String sheetName) {
        return FesodSheet.writerSheet(sheetNo, sheetName);
    }
}
