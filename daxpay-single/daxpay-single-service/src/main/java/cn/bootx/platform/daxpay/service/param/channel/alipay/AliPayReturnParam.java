package cn.bootx.platform.daxpay.service.param.channel.alipay;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付宝同步回调参数
 * @author xxm
 * @since 2024/2/11
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付宝同步回调参数")
public class AliPayReturnParam {

    /**
     * 编码格式，如 utf-8、gbk、gb2312 等。
     */
    @Schema(description = "编码格式")
    @JsonAlias("charset")
    private String charset;

    /**
     * 商家网站唯一订单号, 即为系统中的支付订单号
     */
    @Schema(description = "支付订单号")
    @JsonAlias("out_trade_no")
    private Long out_trade_no;

    /**
     * 该交易在支付宝系统中的交易流水号。最长 64 位。
     * 示例值：2016081121001004630200142207
     */
    @Schema(description = "网关订单号")
    @JsonAlias("trade_no")
    private String trade_no;

    @Schema(description = "接口名称")
    @JsonAlias("method")
    private String method;

    @Schema(description = "支付金额")
    @JsonAlias("total_amount")
    private String total_amount;

    @Schema(description = "签名")
    @JsonAlias("sign")
    private String sign;

    @Schema(description = "不确定是什么")
    @JsonAlias("auth_app_id")
    private String auth_app_id;

    /**
     * 支付宝分配给开发者的应用ID。
     * 示例值：2016040501024706
     */
    @Schema(description = "应用ID")
    @JsonAlias("app_id")
    private String app_id;

    /**
     * 签名算法类型，目前支持 RSA2 和 RSA，推荐使用RSA2
     */
    @Schema(description = "签名算法类型")
    @JsonAlias("sign_type")
    private String sign_type;

    /**
     * 收款支付宝账号对应的支付宝唯一用户号。以 2088 开头的纯 16 位数字。
     * 示例值：2088111111116894
     */
    @Schema(description = "收款支付宝账号对应的支付宝唯一用户号")
    @JsonAlias("seller_id")
    private String seller_id;

    /**
     * 前台回跳的时间，格式：yyyy-MM-dd HH:mm:ss。
     */
    @Schema(description = "前台回跳的时间")
    @JsonAlias("timestamp")
    private String timestamp;
}
