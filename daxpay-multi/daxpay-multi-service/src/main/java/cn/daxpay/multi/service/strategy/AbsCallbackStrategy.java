package cn.daxpay.multi.service.strategy;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 回调处理策略
 * 注意不要直接抛出异常, 需要自行将异常处理完毕后, 返回对应支付渠道需要的响应值
 * @author xxm
 * @since 2024/7/19
 */
public abstract class AbsCallbackStrategy implements PaymentStrategy{

    /**
     * 支付回调处理 解析回调数据
     */
    public String doPayCallbackHandler(HttpServletRequest request){return "";}

    /**
     * 退款回调处理, 解析回调数据
     */
    public String doRefundCallbackHandler(HttpServletRequest request){return "";}

}
