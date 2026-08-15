package cn.daxpay.open.payment.trade.alloc.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 分账关系类型
///
/// 接收方与分账商户的关系(微信/抖音绑定接收方时必填)。
/// 枚举码为平台统一小写形式, 通道适配层按通道原生风格映射:
/// - 微信 V3: 小写直传(relation_type)
/// - 抖音: 转大写(抖音原生为大写下划线形式)
/// - 支付宝: 无此概念(不使用)
///
/// 字典: alloc_relation_type
@Getter
@RequiredArgsConstructor
public enum AllocRelationTypeEnum implements I18nSupport {

    /// 服务商
    SERVICE_PROVIDER("service_provider"),
    /// 门店
    STORE("store"),
    /// 员工
    STAFF("staff"),
    /// 店主
    STORE_OWNER("store_owner"),
    /// 合作伙伴
    PARTNER("partner"),
    /// 总部
    HEADQUARTER("headquarter"),
    /// 品牌方
    BRAND("brand"),
    /// 分销商
    DISTRIBUTOR("distributor"),
    /// 用户
    USER("user"),
    /// 供应商
    SUPPLIER("supplier"),
    /// 自定义(需填写自定义关系名)
    CUSTOM("custom"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.alloc_relation_type";
    }

    /// 根据编码获取枚举
    public static AllocRelationTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.dataNotExist", code));
    }
}
