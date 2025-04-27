package org.dromara.daxpay.sdk.param.trade.pay;

import org.dromara.daxpay.sdk.net.DaxPayRequest;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付关闭参数
 * @author xxm
 * @since 2023/12/17
 */
@Getter
@Setter
public class PayCloseParam extends DaxPayRequest<Void> {


    /**
     * 支付订单号/商户订单号/通道订单号至少要传输一个，支付订单号 > 商户订单号 > 通道订单号
     */
    @Schema(description = "订单号")
    @Size(max = 100, message = "支付订单号不可超过100位")
    private String orderNo;

    /**
     * 支付订单号/商户订单号/通道订单号至少要传输一个，支付订单号 > 商户订单号 > 通道订单号
     */
    @Schema(description = "商户订单号")
    @Size(max = 100, message = "商户支付订单号不可超过100位")
    private String bizOrderNo;

    /**
     * 支付订单号/商户订单号/通道订单号至少要传输一个，支付订单号 > 商户订单号 > 通道订单号
     */
    @Schema(description = "通道订单号")
    @Size(max = 150, message = "通道支付订单号不可超过150位")
    private String outOrderNo;

    /**
     * 是否使用撤销方式进行订单关闭, 只有部分支付通道的支付方式才可以使用,
     * 如果支付订单不支持撤销, 这个参数将不会生效
     */
    @Schema(description = "是否使用撤销方式进行订单关闭")
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
    public DaxResult<Void> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxResult<Void>>() {});
    }
}
