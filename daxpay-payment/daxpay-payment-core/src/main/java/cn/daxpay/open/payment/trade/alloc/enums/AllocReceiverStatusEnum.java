package cn.daxpay.open.payment.trade.alloc.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 分账接收方绑定状态
///
/// 接收方在通道侧的绑定生命周期(一步绑定模型, 无申请单中间态):
/// - 新增/重新绑定调通道 API, 成功 [BOUND], 失败 [FAIL](存失败原因, 可修正后重试)
/// - 已绑定可解绑, 解绑成功后保留记录([UNBOUND]), 支持重新绑定
///
/// 字典: alloc_receiver_status
@Getter
@RequiredArgsConstructor
public enum AllocReceiverStatusEnum implements I18nSupport {

    /// 已绑定(通道侧注册成功)
    BOUND("bound"),
    /// 已解绑(记录保留, 可重新绑定)
    UNBOUND("unbound"),
    /// 绑定失败(存 error_msg, 可修正后重新绑定)
    FAIL("fail"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.alloc_receiver_status";
    }

    /// 根据编码获取枚举
    public static AllocReceiverStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.dataNotExist", code));
    }
}
