package cn.bootx.platform.daxpay.param.channel.alipay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author xxm
 * @date 2021/2/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付宝配置参数")
public class AlipayConfigParam implements Serializable {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "商户编码")
    private String mchCode;

    @Schema(description = "商户应用编码")
    private String mchAppCode;

    @Schema(description = "支付宝商户appId")
    private String appId;

    @Schema(description = "服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问")
    private String notifyUrl;

    @Schema(description = "页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址")
    private String returnUrl;

    @Schema(description = "请求网关地址")
    private String serverUrl;

    @Schema(description = "认证类型 证书/公钥")
    private Integer authType;

    @Schema(description = "签名类型")
    public String signType;

    @Schema(description = "支付宝公钥")
    public String alipayPublicKey;

    @Schema(description = "私钥")
    private String privateKey;

    @Schema(description = "应用公钥证书")
    private String appCert;

    @Schema(description = "支付宝公钥证书文件")
    private String alipayCert;

    @Schema(description = "支付宝CA根证书文件")
    private String alipayRootCert;

    @Schema(description = "超时配置")
    private Integer expireTime;

    @Schema(description = "可用支付方式")
    private List<String> payWayList;

    @Schema(description = "是否沙箱环境")
    private boolean sandbox;

    @Schema(description = "状态")
    private String state;

    @Schema(description = "备注")
    private String remark;

}
