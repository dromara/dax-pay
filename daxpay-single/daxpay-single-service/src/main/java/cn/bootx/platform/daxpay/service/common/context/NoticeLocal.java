package cn.bootx.platform.daxpay.service.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付通知(主动发起, 用于通知客户系统)
 * @author xxm
 * @since 2023/12/24
 */
@Data
@Accessors(chain = true)
public class NoticeLocal {

    /** 异步回调地址 */
    private String notifyUrl;

    /** 同步回调地址 */
    private String returnUrl;

    /** 退出回调地址 */
    private String quitUrl;
}
