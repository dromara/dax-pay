package cn.daxpay.open.payment.common.check.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/// # 配置检查汇总结果
///
/// 聚合所有检查器的输出, 供工作台 Widget 渲染。
/// `categoryCounts` 用于按分类聚合显示数量; `items` 为明细列表。
@Data
@Accessors(chain = true)
public class ConfigCheckResult {

    /// 未配置明细列表
    private List<ConfigCheckItem> items;

    /// 未配置总数
    private int totalCount;

    /// 按分类统计数量(key = category code, value = 数量)
    private Map<String, Integer> categoryCounts;

    public static ConfigCheckResult empty() {
        return new ConfigCheckResult()
                .setItems(List.of())
                .setTotalCount(0)
                .setCategoryCounts(Map.of());
    }
}
