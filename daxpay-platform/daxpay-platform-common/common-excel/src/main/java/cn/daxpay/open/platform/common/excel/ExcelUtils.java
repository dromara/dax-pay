package cn.daxpay.open.platform.common.excel;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.BizWarnException;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.fesod.sheet.ExcelWriter;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.write.style.column.LongestMatchColumnWidthStyleStrategy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/// # Excel 导出工具类
///
/// 基于 Fesod(Apache 孵化中, 原 FastExcel) 实现, 仅覆盖导出场景。
///
/// ## 一次导出 = 内存完整生成 + 成功后落响应
/// 取数/转换/写表全部在内存流中完成, 只有全部成功后才写入 `HttpServletResponse`, 因此**失败不会留下半截 xlsx**:
/// 生成阶段任何异常都发生在响应提交之前, 前端能正常收到 JSON 错误(HTTP 200 + 业务错误码)。
/// 代价是导出内容需整体驻留内存(行数上限见 [EXPORT_MAX_ROWS]), 故用信号量限制并发导出数。
@Slf4j
public @UtilityClass class ExcelUtils {

    /// 导出分页大小
    public static final int EXPORT_PAGE_SIZE = 5000;

    /// 导出最大行数上限(超过直接失败, 不给用户被截断的文件)
    public static final long EXPORT_MAX_ROWS = 50000;

    /// 导出时间范围最大跨度(天)
    private static final long EXPORT_MAX_RANGE_DAYS = 90;

    /// 内存缓冲初始容量(减少扩容拷贝, 超出会按需增长)
    private static final int BUFFER_INITIAL_CAPACITY = 512 * 1024;

    /// 并发导出许可数(内存生成方案下限制同时进行的导出, 避免内存峰值叠加)
    private static final Semaphore EXPORT_PERMITS = new Semaphore(3);

    /// 获取导出许可的等待秒数, 超时视为系统繁忙
    private static final int PERMIT_WAIT_SECONDS = 5;

    /// 简单导出 — 全量数据一次性生成后写入响应(小数据量场景)
    ///
    /// @param data        导出数据
    /// @param fileNameKey 文件名/工作表名词条 key, 按当前请求语言解析, 未命中词条时回退原文
    /// @param clazz       带 `@ExcelProperty` 注解的导出类型(注解值为表头词条 key, 见 [ExcelHeadI18nHandler])
    /// @param response    HTTP 响应
    public <T> void export(List<T> data, String fileNameKey, Class<T> clazz, HttpServletResponse response) {
        String displayName = I18nUtil.get(fileNameKey);
        render(displayName, response, os -> FesodSheet.write(os, clazz)
                .autoCloseStream(false)
                // 全内存模式: 不落 POI 临时文件
                .inMemory(true)
                // 表头词条 key 按当前请求语言翻译
                .registerWriteHandler(ExcelHeadI18nHandler.INSTANCE)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet(displayName)
                .doWrite(data));
    }

    /// 分页导出 — 由本方法驱动分页循环, 调用方只提供「取一页」与「转成导出行」两个函数
    ///
    /// 首页取数后即校验总行数, 超过 [EXPORT_MAX_ROWS] 直接失败(此时响应尚未写出, 前端可正常收到错误提示)。
    ///
    /// @param clazz        带 `@ExcelProperty` 注解的导出行类型(注解值为表头词条 key, 见 [ExcelHeadI18nHandler])
    /// @param fileNameKey  文件名/工作表名词条 key, 按当前请求语言解析, 未命中词条时回退原文
    /// @param response     HTTP 响应
    /// @param pageFetcher  分页取数函数, 入参为 (页码, 每页条数)
    /// @param converter    领域结果 → 导出行 的转换函数
    public <T, R> void exportPaged(Class<T> clazz, String fileNameKey, HttpServletResponse response,
                                   BiFunction<Integer, Integer, PageResult<R>> pageFetcher,
                                   Function<List<R>, List<T>> converter) {
        String displayName = I18nUtil.get(fileNameKey);
        render(displayName, response, os -> writePaged(os, displayName, clazz, pageFetcher, converter));
    }

    /// 校验导出时间范围(必填, 起点不得晚于终点, 跨度 ≤ 90 天)
    ///
    /// @param start 开始时间
    /// @param end   结束时间
    public void validateExportTimeRange(OffsetDateTime start, OffsetDateTime end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            throw new BizInfoException("error.excel.exportTimeRangeRequired");
        }
        if (start.isAfter(end)) {
            throw new BizInfoException("error.excel.exportTimeRangeInvalid");
        }
        if (ChronoUnit.DAYS.between(start, end) > EXPORT_MAX_RANGE_DAYS) {
            throw new BizInfoException("error.excel.exportTimeRangeExceed");
        }
    }

    /// 内存完整生成, 成功后再落响应
    private void render(String sheetName, HttpServletResponse response, Consumer<OutputStream> generator) {
        // 内存生成期间持有许可, 避免并发导出叠加内存峰值
        acquirePermit();
        try {
            var buffer = new ByteArrayOutputStream(BUFFER_INITIAL_CAPACITY);
            generator.accept(buffer);
            // 生成成功才动响应, 此处之后不再有业务失败
            resetResponse(sheetName, response);
            buffer.writeTo(response.getOutputStream());
            response.flushBuffer();
        }
        catch (BizException e) {
            // 参数校验/超限/繁忙等业务异常原样抛出, 保留错误码与文案
            throw e;
        }
        catch (Exception e) {
            // 根因必须落盘: 导出失败多为枚举翻译或数据异常, 只记错误码会丢失现场
            log.error("Excel 导出失败, sheet={}", sheetName, e);
            throw new BizInfoException("error.excel.exportFailed");
        }
        finally {
            EXPORT_PERMITS.release();
        }
    }

    /// 分页写入: 首页即校验总量, 之后按页取数写入, 直到写完、最后一页或到达上限
    private <T, R> void writePaged(OutputStream os, String sheetName, Class<T> clazz,
                                   BiFunction<Integer, Integer, PageResult<R>> pageFetcher,
                                   Function<List<R>, List<T>> converter) {
        try (ExcelWriter excelWriter = FesodSheet.write(os, clazz)
                .autoCloseStream(false)
                // 全内存模式: 不落 POI 临时文件
                .inMemory(true)
                // 表头词条 key 按当前请求语言翻译(分页路径与简单导出路径同等注册)
                .registerWriteHandler(ExcelHeadI18nHandler.INSTANCE)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .build()) {
            ExcelWriterWrapper writer = ExcelWriterWrapper.of(excelWriter, sheetName);
            int pageNo = 1;
            long written = 0;
            while (true) {
                PageResult<R> page = pageFetcher.apply(pageNo, EXPORT_PAGE_SIZE);
                if (Objects.isNull(page)) {
                    break;
                }
                // 首页就能拿到总数, 超限时尚未写出任何内容, 直接失败而不是给用户半截文件
                if (pageNo == 1 && page.getTotal() > EXPORT_MAX_ROWS) {
                    throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "error.excel.exportRowLimitExceed", EXPORT_MAX_ROWS);
                }
                List<T> rows = converter.apply(page.getRecords());
                if (CollUtil.isEmpty(rows)) {
                    break;
                }
                writer.write(rows);
                written += rows.size();
                // 收尾: 已写满总数 / 尾页 / 到达上限(总数不可靠时兜底)
                if (written >= page.getTotal() || rows.size() < EXPORT_PAGE_SIZE || written >= EXPORT_MAX_ROWS) {
                    break;
                }
                pageNo++;
            }
        }
    }

    /// 获取导出许可, 超时视为系统繁忙
    private void acquirePermit() {
        try {
            if (!EXPORT_PERMITS.tryAcquire(PERMIT_WAIT_SECONDS, TimeUnit.SECONDS)) {
                throw new BizWarnException(CommonCode.FAIL_CODE, "error.excel.exportBusy");
            }
        }
        catch (InterruptedException e) {
            // 恢复中断标记, 交由上层线程池处理
            Thread.currentThread().interrupt();
            throw new BizWarnException(CommonCode.FAIL_CODE, "error.excel.exportBusy");
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
