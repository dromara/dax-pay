package cn.daxpay.open.payment.common.check.model;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 产品绑定检查单项结果
///
/// 描述一项产品级配置的检查结果(如"服务商商户号已配置"、"API证书未配置")。
/// i18n 由前端解析, 后端仅回传 key。
@Data
@Accessors(chain = true)
public class ProductBindingCheckItem {

    /// 唯一键(用于前端定位, 如 `wechatIsv.mchId`)
    private String itemKey;

    /// 标题 i18n key(前端 `$t(titleKey)` 解析)
    private String titleKey;

    /// 描述 i18n key(前端 `$t(descriptionKey)` 解析)
    private String descriptionKey;

    /// 是否已配置
    private boolean configured;

    /// 前端操作标识(用于前端区分跳转目标, 如 `openKeyConfig`、`openPlatformCapability`)
    private String action;

    public static ProductBindingCheckItem of(String itemKey, String titleKey, String descriptionKey,
                                             boolean configured, String action) {
        return new ProductBindingCheckItem()
                .setItemKey(itemKey)
                .setTitleKey(titleKey)
                .setDescriptionKey(descriptionKey)
                .setConfigured(configured)
                .setAction(action);
    }
}
