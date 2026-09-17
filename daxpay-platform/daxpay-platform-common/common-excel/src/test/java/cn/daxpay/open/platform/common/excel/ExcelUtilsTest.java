package cn.daxpay.open.platform.common.excel;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
}
