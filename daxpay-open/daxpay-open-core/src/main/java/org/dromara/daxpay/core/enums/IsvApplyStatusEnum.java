package org.dromara.daxpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 进件申请状态
 * 字典: isv_apply_status
 * @author xxm
 * @since 2024/11/16
 */
@Getter
@AllArgsConstructor
public enum IsvApplyStatusEnum {
    /** 草稿 */
    DRAFT("draft", "草稿"),
    /** 预审(暂时不用) */
    PRE_TRIAL("pre_trial", "预审"),
    /** 预审拒绝(暂时不用) */
    PRE_TRIAL_REJECT("pre_trial_reject", "预审拒绝"),
    /** 申请中 */
    APPLY("apply", "申请中"),
    /** 驳回 */
    REJECT("reject", "驳回"),
    /** 待签署 */
    SIGN("sign", "待签署"),
    /** 开通中 */
    OPENING("opening", "开通中"),
    /** 通过 */
    PASS("pass", "通过"),
    /** 已生成进件商户 */
    GENERATED("generated", "已生成进件商户"),
    /** 关闭 */
    CLOSED("closed", "关闭");


    private final String code;
    private final String name;
}
