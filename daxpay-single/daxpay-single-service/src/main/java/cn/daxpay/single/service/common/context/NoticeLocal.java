package cn.daxpay.single.service.common.context;

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

    /**
     * 异步回调地址
     * 如果系统关闭回调, 则通知地址为空
     * 如果传输参数中不进行回调为true, 则通知地址为空
     * 如果传输参数地址为空, 读取接口配置回调地址
     * 如果接口配置为空, 读取系统平台配置的回调地址
     */
    private String notifyUrl;

    /**
     * 同步回调地址, 这个一定会触发, 只有支付会触发且无法关闭
     * 如果参数中不传输, 会自动读取系统平台配置的回调地址
     */
    private String returnUrl;

    /** 退出回调地址 */
    private String quitUrl;
}
