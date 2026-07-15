package cn.daxpay.open.plugin.easypay.result.order;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "易支付订单结果")
public class EasyPayOrderResult extends MchBaseResult {

    private Long orderId;
    private Integer pid;
    private String appId;
    private String tradeNo;
    private String outTradeNo;
    private String apiTradeNo;
    private String type;
    private Integer status;
    private OffsetDateTime addTime;
    private OffsetDateTime endTime;
    private String name;
    private BigDecimal money;
    private BigDecimal refundMoney;
    private String notifyUrl;
    private String returnUrl;
    private String param;
    private String buyer;
    private String clientIp;
    private String apiVersion;
    private String pcCallType;
    private String payUrl;
    private String payBody;
}
