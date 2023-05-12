package cn.bootx.daxpay.core.paymodel.wechat.entity;

import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.annotation.EncryptionField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.daxpay.code.paymodel.WeChatPayCode;
import cn.bootx.daxpay.core.paymodel.wechat.convert.WeChatConvert;
import cn.bootx.daxpay.dto.paymodel.wechat.WeChatPayConfigDto;
import cn.bootx.daxpay.param.paymodel.wechat.WeChatPayConfigParam;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信支付
 *
 * @author xxm
 * @date 2021/3/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_wechat_pay_config")
public class WeChatPayConfig extends MpBaseEntity implements EntityBaseFunction<WeChatPayConfigDto> {

    /** 名称 */
    private String name;

    /** 微信商户号 */
    private String mchId;

    /** 微信应用appId */
    private String appId;

    /**
     * api版本
     * @see WeChatPayCode#API_V2
     */
    private String apiVersion;

    /** 商户平台「API安全」中的 APIv2 密钥 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @EncryptionField
    private String apiKeyV2;

    /** 商户平台「API安全」中的 APIv3 密钥 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @EncryptionField
    private String apiKeyV3;

    /** APPID对应的接口密码，用于获取微信公众号jsapi支付时使用 */
    @EncryptionField
    private String appSecret;

    /** API 证书中的 p12 文件存储的 id */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @EncryptionField
    private Long p12;

    /** API 证书中的 cert.pem 证书 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @BigField
    @EncryptionField
    private String certPem;

    /** API 证书中的 key.pem 私钥 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @BigField
    @EncryptionField
    private String keyPem;

    /** 应用域名，回调中会使用此参数 */
    private String domain;

    /** 服务器异步通知页面路径 通知url必须为直接可访问的url，不能携带参数。公网域名必须为https */
    private String notifyUrl;

    /** 页面跳转同步通知页面路径 */
    private String returnUrl;

    /** 是否沙箱环境 */
    private boolean sandbox;

    /** 超时时间(分钟) */
    private Integer expireTime;

    /** 可用支付方式 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String payWays;

    /** 是否启用 */
    private Boolean activity;

    /** 状态 */
    private Integer state;

    /** 备注 */
    private String remark;

    @Override
    public WeChatPayConfigDto toDto() {
        WeChatPayConfigDto convert = WeChatConvert.CONVERT.convert(this);
        if (StrUtil.isNotBlank(this.getPayWays())) {
            convert.setPayWayList(StrUtil.split(this.getPayWays(), ','));
        }
        return convert;
    }

    public static WeChatPayConfig init(WeChatPayConfigParam in) {
        WeChatPayConfig convert = WeChatConvert.CONVERT.convert(in);
        if (CollUtil.isNotEmpty(in.getPayWayList())) {
            convert.setPayWays(String.join(",", in.getPayWayList()));
        }
        return convert;
    }

}
