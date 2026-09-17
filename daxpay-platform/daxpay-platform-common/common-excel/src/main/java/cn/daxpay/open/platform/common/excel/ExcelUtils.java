package cn.daxpay.open.platform.common.excel;

import cn.hutool.core.util.IdUtil;
import org.apache.fesod.sheet.ExcelWriter;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.write.metadata.WriteSheet;
import org.apache.fesod.sheet.write.style.column.LongestMatchColumnWidthStyleStrategy;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/// # Excel 工具类
///
/// 提供 Excel 导出能力，支持简单导出（小数据量）和流式导出（大数据量分页写入）。
/// 基于 FastExcel 实现，仅覆盖导出场景。
public @UtilityClass class ExcelUtils {

    /** 导出分页大小 */
    public static final int EXPORT_PAGE_SIZE = 5000;

    /** 导出最大行数上限 */
    public static final int EXPORT_MAX_ROWS = 50000;

    /// 简单导出 — 全量数据一次性写入响应流
    ///
    /// @param data      导出数据
    /// @param sheetName 工作表名称（同时作为文件名前缀）
    /// @param clazz     带 `@ExcelProperty` 注解的导出类型
    /// @param response  HTTP 响应
    public <T> void export(List<T> data, String sheetName, Class<T> clazz, HttpServletResponse response) {
        try {
            resetResponse(sheetName, response);
            export(data, sheetName, clazz, response.getOutputStream());
        } catch (IOException e) {
            throw new BizInfoException("error.excel.exportFailed");
        }
    }

    /// 流式导出 — 通过 consumer 分页写入，适合大数据量场景
    ///
    /// <p>consumer 内部按页查询数据并调用 {@link ExcelWriterWrapper#write(Collection, WriteSheet)} 逐批写入，
    /// 避免一次性加载全量数据到内存。</p>
    ///
    /// @param clazz     带 `@ExcelProperty` 注解的导出类型
    /// @param sheetName 工作表名称（同时作为文件名前缀）
    /// @param response  HTTP 响应
    /// @param consumer  导出写入逻辑，接收 {@link ExcelWriterWrapper} 进行分页写入
    public <T> void export(Class<T> clazz, String sheetName, HttpServletResponse response,
                           Consumer<ExcelWriterWrapper<T>> consumer) {
        try {
            resetResponse(sheetName, response);
            export(clazz, sheetName, response.getOutputStream(), consumer);
        } catch (IOException e) {
            throw new BizInfoException("error.excel.exportFailed");
        }
    }

    /// 简单导出 — 全量数据一次性写入指定输出流
    ///
    /// @param data      导出数据
    /// @param sheetName 工作表名称
    /// @param clazz     带 `@ExcelProperty` 注解的导出类型
    /// @param os        输出流
    public <T> void export(List<T> data, String sheetName, Class<T> clazz, OutputStream os) {
        FesodSheet.write(os, clazz)
                .autoCloseStream(false)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet(sheetName)
                .doWrite(data);
    }

    /// 流式导出 — 全量数据写入指定输出流
    ///
    /// @param clazz     带 `@ExcelProperty` 注解的导出类型
    /// @param sheetName 工作表名称
    /// @param os        输出流
    /// @param consumer  导出写入逻辑，接收 {@link ExcelWriterWrapper} 进行分页写入
    public <T> void export(Class<T> clazz, String sheetName, OutputStream os,
                           Consumer<ExcelWriterWrapper<T>> consumer) {
        try (ExcelWriter writer = FesodSheet.write(os, clazz)
                .autoCloseStream(false)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .build()) {
            consumer.accept(ExcelWriterWrapper.of(writer));
        } catch (Exception e) {
            throw new BizInfoException("error.excel.exportFailed");
        }
    }

    /// 校验导出时间范围（必填，跨度 ≤ 90 天）
    ///
    /// @param start 开始时间
    /// @param end   结束时间
    public void validateExportTimeRange(OffsetDateTime start, OffsetDateTime end) {
        if (start == null || end == null) {
            throw new BizInfoException("error.excel.exportTimeRangeRequired");
        }
        if (ChronoUnit.DAYS.between(start, end) > 90) {
            throw new BizInfoException("error.excel.exportTimeRangeExceed");
        }
    }

    /// 编码文件名：UUID_名称.xlsx
    private String encodingFilename(String filename) {
        return IdUtil.fastSimpleUUID() + "_" + filename + ".xlsx";
    }

    /// 设置 Excel 下载响应头
    private void resetResponse(String sheetName, HttpServletResponse response) throws IOException {
        String filename = encodingFilename(sheetName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
    }
}
