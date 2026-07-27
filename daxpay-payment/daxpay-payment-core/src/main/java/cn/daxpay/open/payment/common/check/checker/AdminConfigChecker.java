package cn.daxpay.open.payment.common.check.checker;

import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;

/// # 运营端配置检查器接口
///
/// 每个实现类负责一个维度的"平台级配置是否已完成"检测。
/// 实现类需标注 `@Component`, 由 [AdminConfigCheckService] 自动收集。
/// 已完成配置时返回 `null`, 未完成时返回 [ConfigCheckItem]。
///
/// routeName 契约: [ConfigCheckItem#routeName] 必须等于**运营端前端菜单的 path 字段**
/// (前端 `menu.api.ts` 中 `route.name = menu.path`), 而非 PascalCase 组件名,
/// 否则前端 `router.hasRoute()` 无法命中。可在 `iam_perm_menu.sql` 查 path 真值。
public interface AdminConfigChecker {

    /// 该检查器负责的分类
    ConfigCheckCategoryEnum getCategory();

    /// 执行检测
    ConfigCheckItem check();
}
