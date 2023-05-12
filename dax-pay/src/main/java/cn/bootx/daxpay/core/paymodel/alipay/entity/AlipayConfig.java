package cn.bootx.daxpay.core.paymodel.alipay.entity;

import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.annotation.EncryptionField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.daxpay.core.paymodel.alipay.convert.AlipayConvert;
import cn.bootx.daxpay.dto.paymodel.alipay.AlipayConfigDto;
import cn.bootx.daxpay.param.paymodel.alipay.AlipayConfigParam;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付宝支付配置
 *
 * @author xxm
 * @date 2020/12/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alipay_config")
public class AlipayConfig extends MpBaseEntity implements EntityBaseFunction<AlipayConfigDto> {

    /** 名称 */
    private String name;

    /** 支付宝商户appId */
    private String appId;

    /** 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 */
    private String notifyUrl;

    /**
     * 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
     */
    private String returnUrl;

    /** 请求网关地址 */
    private String serverUrl;

    /** 认证类型 证书/公钥 */
    private Integer authType;

    /** 签名类型 */
    public String signType;

    /** 支付宝公钥 */
    @BigField
    public String alipayPublicKey;

    /** 私钥 */
    @BigField
    @EncryptionField
    private String privateKey;

    /** 应用公钥证书 */
    @BigField
    @EncryptionField
    private String appCert;

    /** 支付宝公钥证书 */
    @BigField
    @EncryptionField
    private String alipayCert;

    /** 支付宝CA根证书 */
    @BigField
    @EncryptionField
    private String alipayRootCert;

    /** 是否沙箱环境 */
    private boolean sandbox;

    /** 超时配置 */
    private Integer expireTime;

    /** 可用支付方式 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String payWays;

    /** 是否启用 */
    private Boolean activity;

    /** 状态 暂时没什么用 */
    private Integer state;

    /** 备注 */
    private String remark;

    @Override
    public AlipayConfigDto toDto() {
        AlipayConfigDto convert = AlipayConvert.CONVERT.convert(this);
        if (StrUtil.isNotBlank(this.getPayWays())) {
            convert.setPayWayList(StrUtil.split(this.getPayWays(), ','));
        }
        return convert;
    }

    public static AlipayConfig init(AlipayConfigParam in) {
        AlipayConfig convert = AlipayConvert.CONVERT.convert(in);
        if (CollUtil.isNotEmpty(in.getPayWayList())) {
            convert.setPayWays(String.join(",", in.getPayWayList()));
        }
        return convert;
    }

}
