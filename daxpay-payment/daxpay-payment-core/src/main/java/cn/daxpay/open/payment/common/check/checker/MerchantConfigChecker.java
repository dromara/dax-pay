package cn.daxpay.open.payment.common.check.checker;

import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.payment.common.check.service.MerchantConfigCheckService;

/// # 商户端配置检查器接口
///
/// 每个实现类负责一个维度的"商户级配置是否已完成"检测。
/// 实现类需标注 `@Component`, 由 [MerchantConfigCheckService] 自动收集。
/// 已完成配置时返回 `null`, 未完成时返回 [ConfigCheckItem]。
///
/// routeName 契约: [ConfigCheckItem#routeName] 必须等于**商户端前端菜单的 path 字段**
/// (商户端前端 `menu.api.ts` 中 `route.name = menu.path`)。
/// TODO 商户端前端尚未开发, 现有实现下发的 PascalCase 组件名待商户端路由确定后统一对齐。
public interface MerchantConfigChecker {

    /// 该检查器负责的分类
    ConfigCheckCategoryEnum getCategory();

    /// 执行检测(按商户号隔离)
    ConfigCheckItem check(String mchNo);
}
