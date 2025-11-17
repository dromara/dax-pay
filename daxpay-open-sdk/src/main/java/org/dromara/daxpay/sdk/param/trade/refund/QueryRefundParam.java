package org.dromara.daxpay.sdk.param.trade.refund;

import org.dromara.daxpay.sdk.net.DaxPayRequest;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.trade.refund.RefundOrderResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询退款订单参数类
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "查询退款订单参数类")
public class QueryRefundParam extends DaxPayRequest<RefundOrderResult> {

    /** 退款号 */
    @Schema(description = "退款号")
    @Size(max = 100, message = "退款号不可超过100位")
    private String refundNo;

    /** 商户退款号 */
    @Schema(description = "商户退款号")
    @Size(max = 100, message = "商户退款号不可超过100位")
    private String bizRefundNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/query/refundOrder";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxResult<RefundOrderResult> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxResult<RefundOrderResult>>() {});
    }
}
