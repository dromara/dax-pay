package cn.bootx.platform.daxpay.core.channel.wechat.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.annotation.EncryptionField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.core.channel.wechat.convert.WeChatConvert;
import cn.bootx.platform.daxpay.dto.channel.wechat.WeChatPayConfigDto;
import cn.bootx.platform.daxpay.param.channel.wechat.WeChatPayConfigParam;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信支付配置
 *
 * @author xxm
 * @date 2021/3/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "微信支付配置")
@Accessors(chain = true)
@TableName("pay_wechat_pay_config")
public class WeChatPayConfig extends MpBaseEntity implements EntityBaseFunction<WeChatPayConfigDto> {

    /** 名称 */
    @DbColumn(comment = "名称")
    private String name;

    /** 微信商户号 */
    @DbColumn(comment = "微信商户号")
    private String mchId;

    /** 商户应用Id */
    @DbColumn(comment = "商户应用Id")
    private Long mchAppId;

    /** 微信应用appId */
    @DbColumn(comment = "微信应用appId")
    private String appId;

//    /**
//     * api版本
//     * @see WeChatPayCode#API_V2
//     */
//    @DbColumn(comment = "api版本")
//    private String apiVersion;

    /** 商户平台「API安全」中的 APIv2 密钥 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @EncryptionField
    @DbColumn(comment = "APIv2 密钥")
    private String apiKeyV2;

    /** 商户平台「API安全」中的 APIv3 密钥 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @EncryptionField
    @DbColumn(comment = "APIv3 密钥")
    private String apiKeyV3;

    /** APPID对应的接口密码，用于获取微信公众号jsapi支付时使用 */
    @EncryptionField
    @DbColumn(comment = "APPID对应的接口密码")
    private String appSecret;

    /** API 证书中的 p12 文件存储的 id */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @EncryptionField
    @DbColumn(comment = "API 证书中的 p12 文件存储的 id")
    private Long p12;

    /** API 证书中的 cert.pem 证书 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @BigField
    @EncryptionField
    @DbColumn(comment = "API 证书中的 cert.pem 证书 ")
    private String certPem;

    /** API 证书中的 key.pem 私钥 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @BigField
    @EncryptionField
    @DbColumn(comment = "API 证书中的 key.pem 私钥")
    private String keyPem;

    /** 应用域名，回调中会使用此参数 */
    @DbColumn(comment = "应用域名")
    private String domain;

    /** 服务器异步通知页面路径 通知url必须为直接可访问的url，不能携带参数。公网域名必须为https */
    @DbColumn(comment = "异步通知页面")
    private String notifyUrl;

    /** 页面跳转同步通知页面路径 */
    @DbColumn(comment = "同步通知页面")
    private String returnUrl;

    /** 是否沙箱环境 */
    @DbColumn(comment = "是否沙箱环境")
    private boolean sandbox;

    /** 超时时间(分钟) */
    @DbColumn(comment = "超时时间(分钟)")
    private Integer expireTime;

    /** 可用支付方式 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @DbColumn(comment = "可用支付方式")
    private String payWays;

    /** 是否启用 */
    @DbColumn(comment = "是否启用")
    private Boolean activity;

    /** 状态 */
    @DbColumn(comment = "状态")
    private Integer state;

    /** 备注 */
    @DbColumn(comment = "备注")
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
