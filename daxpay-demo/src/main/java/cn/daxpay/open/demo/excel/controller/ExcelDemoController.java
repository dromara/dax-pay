package cn.daxpay.open.demo.excel.controller;

import cn.daxpay.open.demo.excel.result.ExcelDemoResult;
import cn.daxpay.open.platform.common.excel.ExcelUtils;
import cn.daxpay.open.platform.common.excel.ExcelWriterWrapper;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.idev.excel.write.metadata.WriteSheet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/// # Excel 导出演示接口
///
/// 演示两种导出模式:
/// 1. 简单导出 — 全量数据一次性写入, 适合小数据量
/// 2. 流式导出 — 模拟分页逐批写入, 适合大数据量场景
///
/// 数据均为内存 Mock, 不依赖数据库。
///
/// 鉴权：URL 前缀 `/demo/**` 已在白名单，类上叠加 `@IgnoreAuth` 双保险。
@IgnoreAuth
@Tag(name = "Excel 导出演示")
@RestController
@RequestMapping("/demo/excel")
@RequiredArgsConstructor
public class ExcelDemoController {

    private static final String[] CATEGORIES = {"饮品", "食品", "日用品", "电子产品", "文具"};
    private static final String[] STATUSES = {"在售", "下架", "售罄"};
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /// 简单导出：一次性生成固定条数的 Mock 数据
    @Operation(summary = "简单导出（全量一次性写入）")
    @GetMapping("/export/simple")
    public void simpleExport(
            @Parameter(description = "导出条数，默认 20") @RequestParam(value = "count", defaultValue = "20") int count,
            HttpServletResponse response) {
        List<ExcelDemoResult> data = generateData(count);
        ExcelUtils.export(data, "导出演示", ExcelDemoResult.class, response);
    }

    /// 流式导出：模拟分页逐批写入，演示大数据量场景
    ///
    /// 每页 500 条，分批写入 ExcelWriterWrapper，与 PayTradeAdminController 的导出逻辑同构。
    @Operation(summary = "流式导出（模拟分页逐批写入）")
    @GetMapping("/export/streaming")
    public void streamingExport(
            @Parameter(description = "导出总条数，默认 5000") @RequestParam(value = "total", defaultValue = "5000") int total,
            HttpServletResponse response) {
        ExcelUtils.export(ExcelDemoResult.class, "流式导出演示", response, wrapper -> {
            WriteSheet sheet = ExcelWriterWrapper.buildSheet("流式导出演示");
            int pageSize = 500;
            int written = 0;
            while (written < total) {
                int batchSize = Math.min(pageSize, total - written);
                List<ExcelDemoResult> batch = generateData(batchSize, written);
                wrapper.write(batch, sheet);
                written += batchSize;
            }
        });
    }

    /// 返回 Mock 数据条数（供前端预览后决定导出）
    @Operation(summary = "预览 Mock 数据")
    @GetMapping("/preview")
    public Result<List<ExcelDemoResult>> preview(
            @Parameter(description = "条数，默认 10") @RequestParam(value = "count", defaultValue = "10") int count) {
        return Res.ok(generateData(count));
    }

    /// 生成 Mock 数据（序号从 0 开始）
    private List<ExcelDemoResult> generateData(int count) {
        return generateData(count, 0);
    }

    /// 生成 Mock 数据（支持指定起始序号，用于分页场景各批次序号连续）
    private List<ExcelDemoResult> generateData(int count, int startSeq) {
        List<ExcelDemoResult> list = new ArrayList<>(count);
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < count; i++) {
            int seq = startSeq + i + 1;
            String category = CATEGORIES[i % CATEGORIES.length];
            list.add(new ExcelDemoResult()
                    .setProductCode(String.format("P%06d", seq))
                    .setProductName(category + "商品-" + seq)
                    .setCategory(category)
                    .setPrice(String.format("%.2f", (seq * 1.1 % 999) + 1))
                    .setStock(seq % 200)
                    .setStatus(STATUSES[i % STATUSES.length])
                    .setCreateTime(now.minusMinutes(count - i).format(FORMATTER)));
        }
        return list;
    }
}
