package cn.daxpay.open.payment.common.check.model;

import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckSeverityEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 单个配置检查结果项
///
/// 一条"未完成配置"记录。已完成的配置不产生本对象(返回 null)。
/// i18n 由前端解析, 后端仅回传 key。
/// `category`/`severity` 以 code 字符串传输, 避免枚举序列化为大写 name()。
@Data
@Accessors(chain = true)
public class ConfigCheckItem {

    /// 所属分类 code(对应 [ConfigCheckCategoryEnum#getCode])
    private String category;

    /// 唯一键(用于前端去重/定位, 如 `pay_product:alipay`、`mch_app`)
    private String itemKey;

    /// 标题 i18n key(前端 `$t(titleKey)` 解析)
    private String titleKey;

    /// 描述 i18n key(前端 `$t(descriptionKey)` 解析)
    private String descriptionKey;

    /// 严重程度 code(对应 [ConfigCheckSeverityEnum#getCode])
    private String severity;

    /// 点击配置项时使用的 Vue Router 路由 name。
    /// 运营端动态菜单生成路由时, 菜单的 path 会同时作为路由的 name 和 path(见 menu.api.ts)。
    /// 因此这里应填写菜单 path, 如 `/system/config/platform`、`/payment/merchant/route`, 而不是组件 defineOptions 中的
    /// PascalCase name, 如 `PlatformConfig`, 否则前端 router.hasRoute(routeName) 无法找到路由。
    /// 静态路由使用其自身定义的 name, 例如 `NotifyCenter`。
    private String routeName;

    /// 列表型告警的未配置数量(单项型可留空)
    /// 前端用于显示"N 项待配置", 并在跳转列表后聚焦这些条目
    private Integer count;

    public static ConfigCheckItem of(ConfigCheckCategoryEnum category,
                                     String itemKey, String titleKey, String descriptionKey, String routeName) {
        return new ConfigCheckItem()
                .setCategory(category.getCode())
                .setItemKey(itemKey)
                .setTitleKey(titleKey)
                .setDescriptionKey(descriptionKey)
                .setRouteName(routeName)
                .setSeverity(ConfigCheckSeverityEnum.SUGGEST.getCode());
    }
}
