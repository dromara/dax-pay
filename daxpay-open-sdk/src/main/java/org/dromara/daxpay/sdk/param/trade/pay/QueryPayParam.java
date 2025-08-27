package org.dromara.daxpay.sdk.param.trade.pay;

import org.dromara.daxpay.sdk.net.DaxPayRequest;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.trade.pay.PayOrderResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付查询参数
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付查询参数")
public class QueryPayParam extends DaxPayRequest<PayOrderResult> {


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
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/query/payOrder";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxResult<PayOrderResult> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxResult<PayOrderResult>>() {});
    }
}
