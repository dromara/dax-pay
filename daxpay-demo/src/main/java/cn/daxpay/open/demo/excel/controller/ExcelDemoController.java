package cn.daxpay.open.demo.excel.controller;

import cn.daxpay.open.demo.excel.result.ExcelDemoResult;
import cn.daxpay.open.platform.common.excel.ExcelUtils;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
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
/// 2. 分页导出 — 模拟分页逐批取数写入, 适合大数据量场景
///
/// 数据均为内存 Mock, 不依赖数据库。
///
/// 鉴权：URL 前缀 `/demo/**` 在 dev 环境已加入白名单; 生产环境不白名单, 需正常登录鉴权
/// (导出为较重的服务端运算, 不作为匿名开放接口)。
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

    /// 分页导出：模拟分页逐批取数写入，与交易域各导出口径同构
    ///
    /// 传入超过 [ExcelUtils#EXPORT_MAX_ROWS] 的 total 可直接观察超限拦截效果(返回业务错误而非截断文件)。
    @Operation(summary = "分页导出（模拟分页逐批写入）")
    @GetMapping("/export/paged")
    public void pagedExport(
            @Parameter(description = "导出总条数，默认 5000") @RequestParam(value = "total", defaultValue = "5000") int total,
            HttpServletResponse response) {
        ExcelUtils.exportPaged(ExcelDemoResult.class, "分页导出演示", response,
                (pageNo, size) -> {
                    int start = (pageNo - 1) * size;
                    int batchSize = Math.max(0, Math.min(size, total - start));
                    return new PageResult<ExcelDemoResult>()
                            .setRecords(generateData(batchSize, start))
                            .setTotal(total)
                            .setSize(size)
                            .setCurrent(pageNo);
                },
                rows -> rows);
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
