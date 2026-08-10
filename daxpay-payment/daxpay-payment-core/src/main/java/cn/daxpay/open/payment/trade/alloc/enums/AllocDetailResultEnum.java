package cn.daxpay.open.payment.trade.alloc.enums;

import cn.daxpay.open.platform.core.exception.system.StatusNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 分账明细结果
///
/// alloc_detail 表的明细结果, 标识单个接收方的分账结果。
/// 字典: alloc_detail_result
@Getter
@RequiredArgsConstructor
public enum AllocDetailResultEnum implements I18nSupport {

    /// 待分账(已创建, 通道尚未返回结果)
    PENDING("pending"),
    /// 成功
    SUCCESS("success"),
    /// 失败
    FAIL("fail"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.alloc_detail_result";
    }

    /// 根据编码获取枚举
    public static AllocDetailResultEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 该分账明细结果不存在: {0}
                .orElseThrow(() -> new StatusNotExistException("error.common.allocDetailResultNotExist", code));
    }
}
