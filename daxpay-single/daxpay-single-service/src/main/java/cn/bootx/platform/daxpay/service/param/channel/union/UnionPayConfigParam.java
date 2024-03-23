package cn.bootx.platform.daxpay.service.param.channel.union;

import cn.bootx.platform.daxpay.service.code.UnionPaySignTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 云闪付支付配置参数
 * @author xxm
 * @since 2024/3/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "云闪付支付配置参数")
public class UnionPayConfigParam {

    /** 商户号 */
    @Schema(description = "商户号")
    private String machId;

    /** 是否启用, 只影响支付和退款操作 */
    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "支付限额")
    private Integer singleLimit;

    /**
     * 商户收款账号
     */
    @Schema(description = "商户收款账号")
    private String seller;

    /**
     * 签名类型
     * @see UnionPaySignTypeEnum
     */
    @Schema(description = "签名类型")
    public String signType;

    /**
     * 是否为证书签名
     */
    @Schema(description = "是否为证书签名")
    private boolean certSign;

    /**
     * 应用私钥证书
     */
    @Schema(description = "应用私钥证书")
    private String keyPrivateCert;
    /**
     * 私钥证书对应的密码
     */
    @Schema(description = "私钥证书对应的密码")
    private String keyPrivateCertPwd;

    /**
     * 中级证书
     */
    @Schema(description = "中级证书")
    private String acpMiddleCert;
    /**
     * 根证书
     */
    @Schema(description = "根证书")
    private String acpRootCert;

    /** 是否沙箱环境 */
    @Schema(description = "是否沙箱环境")
    private boolean sandbox;

    /**
     * 服务器异步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 消息顺序 银联网关 -> 本网关进行处理 -> 发送消息通知业务系统
     */
    @Schema(description = "异步通知路径")
    private String notifyUrl;

    /**
     * 服务器同步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 消息顺序 银联网关 -> 本网关进行处理 -> 重定向到业务系统中
     */
    @Schema(description = "同步通知页面路径")
    private String returnUrl;

    /** 可用支付方式 */
    @Schema(description = "可用支付方式")
    private List<String> payWays;

    @Schema(description = "备注")
    private String remark;
}
