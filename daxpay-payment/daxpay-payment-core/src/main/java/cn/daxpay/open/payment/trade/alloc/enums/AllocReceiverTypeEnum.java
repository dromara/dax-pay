package cn.daxpay.open.payment.trade.alloc.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 分账接收方类型
///
/// 分账接收方账号类型, 按通道差异化使用：
/// - 支付宝: userId(2088) / loginName(手机号/邮箱)
/// - 微信: MERCHANT_ID(商户号) / PERSONAL_OPENID(个人 openid)
/// - 抖音: MERCHANT_ID(商户号) / PERSONAL_OPENID(个人 openid)
///
/// 商业版统一用大写枚举码(与微信/抖音通道原生一致), 支付宝的 userId/loginName
/// 在通道适配层映射为大写类型, 对外接口统一使用本枚举。
/// 字典: alloc_receiver_type
@Getter
@RequiredArgsConstructor
public enum AllocReceiverTypeEnum implements I18nSupport {

    /// 商户号(微信/抖音)
    MERCHANT_ID("MERCHANT_ID"),
    /// 个人 openid(微信/抖音)
    PERSONAL_OPENID("PERSONAL_OPENID"),
    /// 子商户应用个人 openid(仅微信服务商接收方绑定使用, openid 为 sub_app_id 维度)
    PERSONAL_SUB_OPENID("PERSONAL_SUB_OPENID"),
    /// 支付宝用户ID(2088开头, 对应支付宝原生 userId)
    USER_ID("USER_ID"),
    /// 支付宝登录账号(手机号/邮箱, 对应支付宝原生 loginName)
    LOGIN_NAME("LOGIN_NAME"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.alloc_receiver_type";
    }

    /// 根据编码获取枚举
    public static AllocReceiverTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.dataNotExist", code));
    }
}
