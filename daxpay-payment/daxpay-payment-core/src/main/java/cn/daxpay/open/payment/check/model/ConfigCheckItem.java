package cn.daxpay.open.payment.check.model;

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

    /// 所属分类 code(对应 [cn.daxpay.open.payment.check.enums.ConfigCheckCategoryEnum#getCode])
    private String category;

    /// 唯一键(用于前端去重/定位, 如 `pay_product:alipay`、`mch_app`)
    private String itemKey;

    /// 标题 i18n key(前端 `$t(titleKey)` 解析)
    private String titleKey;

    /// 描述 i18n key(前端 `$t(descriptionKey)` 解析)
    private String descriptionKey;

    /// 严重程度 code(对应 [cn.daxpay.open.payment.check.enums.ConfigCheckSeverityEnum#getCode])
    private String severity;

    /// 前端路由 name, 点击该项时跳转
    /// 必须等于前端菜单的 path 字段(运营端 menu.api.ts 中 `route.name = menu.path`),
    /// 而非组件 defineOptions 的 PascalCase name, 否则 [org.vue.Router#hasRoute] 无法命中
    /// 跳转目标(如 `/system/config/platform`、`/payment/merchant/route`)
    private String routeName;

    /// 列表型告警的未配置数量(单项型可留空)
    /// 前端用于显示"N 项待配置", 并在跳转列表后聚焦这些条目
    private Integer count;

    public static ConfigCheckItem of(cn.daxpay.open.payment.check.enums.ConfigCheckCategoryEnum category,
                                     String itemKey, String titleKey, String descriptionKey, String routeName) {
        return new ConfigCheckItem()
                .setCategory(category.getCode())
                .setItemKey(itemKey)
                .setTitleKey(titleKey)
                .setDescriptionKey(descriptionKey)
                .setRouteName(routeName)
                .setSeverity(cn.daxpay.open.payment.check.enums.ConfigCheckSeverityEnum.SUGGEST.getCode());
    }
}
