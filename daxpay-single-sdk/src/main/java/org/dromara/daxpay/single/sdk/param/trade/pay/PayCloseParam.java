package org.dromara.daxpay.single.sdk.param.trade.pay;

import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

/**
 * 支付关闭参数
 * @author xxm
 * @since 2023/12/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PayCloseParam extends DaxPayRequest<Void> {

    /**
     * 订单号
     * 订单号和商户订单号必传一个
     * 长度不可超过32位
     */
    private String orderNo;

    /**
     * 商户订单号
     * 订单号和商户订单号必传一个
     * 长度不可超过100位
     */
    private String bizOrderNo;

    /**
     * 是否使用撤销方式进行订单关闭, 只
     * 有部分支付通道的支付方式才可以使用,
     * 如果支付订单不支持撤销, 这个参数将不会生效
     */
    private boolean useCancel;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/close";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<Void> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<Void>>() {});
    }
}
