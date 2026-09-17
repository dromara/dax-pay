package cn.daxpay.open.platform.common.excel;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// Excel 导出工具测试
///
/// 覆盖三件事: 时间范围校验、超限拦截不产出文件、分页循环与真实 xlsx 产出。
class ExcelUtilsTest {

    /// 导出用行类型(导出库要求 JavaBean 访问器)
    public static class DemoRow {

        @ExcelProperty("单号")
        private String no;

        public DemoRow() {
        }

        public DemoRow(String no) {
            this.no = no;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }
    }

    /// 把响应流接到内存缓冲
    private static ServletOutputStream outputStreamOf(ByteArrayOutputStream buffer) {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }

            @Override
            public void write(int b) {
                buffer.write(b);
            }
        };
    }

    /// 表头注解值为词条 key 的行类型(配合 mock MessageSource 验证翻译钩子)
    public static class KeyHeadRow {

        @ExcelProperty("export.test.orderNo")
        private String no;

        /// 非词条 key 的直写表头, 验证未命中词条时原样保留
        @ExcelProperty("纯文本表头")
        private String remark;

        public KeyHeadRow() {
        }

        public KeyHeadRow(String no, String remark) {
            this.no = no;
            this.remark = remark;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    @Test
    void validateExportTimeRangeShouldRejectMissingRange() {
        OffsetDateTime start = OffsetDateTime.of(2026, 9, 17, 0, 0, 0, 0, ZoneOffset.UTC);
        assertEquals("error.excel.exportTimeRangeRequired",
                assertThrows(BizException.class, () -> ExcelUtils.validateExportTimeRange(start, null))
                        .resolveMessageKey());
        assertEquals("error.excel.exportTimeRangeRequired",
                assertThrows(BizException.class, () -> ExcelUtils.validateExportTimeRange(null, null))
                        .resolveMessageKey());
    }

    @Test
    void validateExportTimeRangeShouldRejectReversedRange() {
        OffsetDateTime start = OffsetDateTime.of(2026, 9, 17, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime end = start.minusDays(1);
        assertEquals("error.excel.exportTimeRangeInvalid",
                assertThrows(BizException.class, () -> ExcelUtils.validateExportTimeRange(start, end))
                        .resolveMessageKey());
    }

    @Test
    void validateExportTimeRangeShouldRejectRangeOver90Days() {
        OffsetDateTime start = OffsetDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime end = start.plusDays(91);
        assertEquals("error.excel.exportTimeRangeExceed",
                assertThrows(BizException.class, () -> ExcelUtils.validateExportTimeRange(start, end))
                        .resolveMessageKey());
    }

    @Test
    void validateExportTimeRangeShouldPassExactly90Days() {
        OffsetDateTime start = OffsetDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        ExcelUtils.validateExportTimeRange(start, start.plusDays(90));
    }

    @Test
    void exportPagedShouldFailBeforeWritingAnythingWhenRowsExceedLimit() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        BizException ex = assertThrows(BizException.class, () -> ExcelUtils.exportPaged(
                DemoRow.class, "超限导出", response,
                (pageNo, size) -> new PageResult<DemoRow>()
                        .setRecords(List.of(new DemoRow("T1")))
                        .setTotal(ExcelUtils.EXPORT_MAX_ROWS + 1),
                rows -> rows));

        assertEquals("error.excel.exportRowLimitExceed", ex.resolveMessageKey());
        // 关键: 超限发生在写出响应之前, 前端拿到的是可解析的业务错误, 而不是打不开的半截文件
        verify(response, never()).getOutputStream();
        verify(response, never()).setContentType(anyString());
    }

    @Test
    void exportPagedShouldWriteRealXlsxAfterAllPagesSucceed() throws IOException {
        var buffer = new ByteArrayOutputStream();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(outputStreamOf(buffer));
        var fetchCount = new AtomicInteger();

        // 首页满页(EXPORT_PAGE_SIZE 条), 第二页仅 3 条 → 应取两页后收尾
        ExcelUtils.exportPaged(DemoRow.class, "单元测试", response,
                (pageNo, size) -> {
                    fetchCount.incrementAndGet();
                    int count = pageNo == 1 ? size : 3;
                    List<DemoRow> rows = new ArrayList<>(count);
                    for (int i = 0; i < count; i++) {
                        rows.add(new DemoRow("T" + i));
                    }
                    return new PageResult<DemoRow>()
                            .setRecords(rows)
                            .setTotal(size + 3L)
                            .setSize(size)
                            .setCurrent(pageNo);
                },
                rows -> rows);

        assertEquals(2, fetchCount.get());
        byte[] bytes = buffer.toByteArray();
        // xlsx 本质是 zip(PK\x03\x04), 校验魔数与体积证明确实产出了可打开的表格
        assertEquals('P', bytes[0]);
        assertEquals('K', bytes[1]);
        assertTrue(bytes.length > 1024, "导出的 xlsx 体积异常: " + bytes.length);
        verify(response).setContentType(contains("spreadsheetml"));
        verify(response).setHeader(eq("Content-Disposition"), contains("attachment"));
    }

    @Test
    void exportPagedShouldStopOnEmptyFirstPage() throws IOException {
        var buffer = new ByteArrayOutputStream();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(outputStreamOf(buffer));

        ExcelUtils.exportPaged(DemoRow.class, "空数据", response,
                (pageNo, size) -> new PageResult<DemoRow>().setRecords(List.of()).setTotal(0),
                rows -> rows);

        // 无数据也要产出仅含表头的合法 xlsx, 而不是报错或空响应
        byte[] bytes = buffer.toByteArray();
        assertEquals('P', bytes[0]);
        assertEquals('K', bytes[1]);
    }

    @Test
    void exportShouldTranslateHeadAndFileNameByMessageSource() throws IOException {
        MessageSource messageSource = mock(MessageSource.class);
        // 先定义兜底语义(未命中词条回退 defaultMessage, 与真实 MessageSource 行为一致), 再覆盖具体词条
        when(messageSource.getMessage(anyString(), any(), anyString(), any()))
                .thenAnswer(invocation -> invocation.getArgument(2));
        when(messageSource.getMessage(eq("export.test.orderNo"), any(), eq("export.test.orderNo"), any()))
                .thenReturn("Order No.");
        when(messageSource.getMessage(eq("export.test.fileName"), any(), eq("export.test.fileName"), any()))
                .thenReturn("Test Export");

        var buffer = new ByteArrayOutputStream();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(outputStreamOf(buffer));

        I18nUtil.setMessageSource(messageSource);
        try {
            ExcelUtils.export(List.of(new KeyHeadRow("T1", "r1")), "export.test.fileName", KeyHeadRow.class, response);
        }
        finally {
            // I18nUtil 是全局静态状态, 测试结束必须复位, 避免污染其它用例
            I18nUtil.setMessageSource(null);
        }

        // 读回 xlsx: headRowNumber(0) 关闭表头跳过, 首行即表头(词条 key 已翻译, 非 key 直写表头原样保留)
        List<Map<Integer, String>> rows = FesodSheet.read(new ByteArrayInputStream(buffer.toByteArray()))
                .sheet()
                .headRowNumber(0)
                .doReadSync();
        assertEquals("Order No.", rows.get(0).get(0));
        assertEquals("纯文本表头", rows.get(0).get(1));
        assertEquals("T1", rows.get(1).get(0));

        // 文件名/工作表名词条与表头走同一翻译管道(URLEncoder 将空格编码为 +)
        verify(response).setHeader(eq("Content-Disposition"), contains("Test+Export"));
    }

    @Test
    void exportPagedShouldTranslateHeadToo() throws IOException {
        MessageSource messageSource = mock(MessageSource.class);
        // 先定义兜底语义(未命中词条回退 defaultMessage), 再覆盖具体词条
        when(messageSource.getMessage(anyString(), any(), anyString(), any()))
                .thenAnswer(invocation -> invocation.getArgument(2));
        when(messageSource.getMessage(eq("export.test.orderNo"), any(), eq("export.test.orderNo"), any()))
                .thenReturn("Order No.");
        when(messageSource.getMessage(eq("export.test.fileName"), any(), eq("export.test.fileName"), any()))
                .thenReturn("Paged Export");

        var buffer = new ByteArrayOutputStream();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(outputStreamOf(buffer));

        I18nUtil.setMessageSource(messageSource);
        try {
            // 分页导出路径(业务接口实际走的路径), 表头翻译钩子必须与简单导出路径同等生效
            ExcelUtils.exportPaged(KeyHeadRow.class, "export.test.fileName", response,
                    (pageNo, size) -> new PageResult<KeyHeadRow>()
                            .setRecords(List.of(new KeyHeadRow("T1", "r1")))
                            .setTotal(1),
                    rows -> rows);
        }
        finally {
            // I18nUtil 是全局静态状态, 测试结束必须复位, 避免污染其它用例
            I18nUtil.setMessageSource(null);
        }

        List<Map<Integer, String>> rows = FesodSheet.read(new ByteArrayInputStream(buffer.toByteArray()))
                .sheet()
                .headRowNumber(0)
                .doReadSync();
        assertEquals("Order No.", rows.get(0).get(0));
        assertEquals("纯文本表头", rows.get(0).get(1));
    }
}
