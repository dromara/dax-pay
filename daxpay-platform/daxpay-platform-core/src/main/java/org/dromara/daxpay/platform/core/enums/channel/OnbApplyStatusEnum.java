package org.dromara.daxpay.platform.core.enums.channel;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 进件申请状态
///
/// 字典: onb_apply_status
@Getter
@RequiredArgsConstructor
public enum OnbApplyStatusEnum implements I18nSupport {

    /// 草稿
    DRAFT("draft"),
    /// 预审
    PRE_TRIAL("pre_trial"),
    /// 预审拒绝
    PRE_TRIAL_REJECT("pre_trial_reject"),
    /// 数据补填
    COMPLETION("completion"),
    /// 申请中
    APPLY("apply"),
    /// 驳回
    REJECT("reject"),
    /// 通过
    PASS("pass"),
    /// 已生成进件商户
    GENERATED("generated"),
    /// 关闭
    CLOSED("closed"),
    /// 错误
    ERROR("error");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.onb_apply_status";
    }

}
