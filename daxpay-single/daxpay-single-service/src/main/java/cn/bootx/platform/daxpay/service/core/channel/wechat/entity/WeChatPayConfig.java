package cn.bootx.platform.daxpay.service.core.channel.wechat.entity;

import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.annotation.EncryptionField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.StringListTypeHandler;
import cn.bootx.platform.daxpay.service.core.channel.wechat.convert.WeChatConvert;
import cn.bootx.platform.daxpay.service.dto.channel.wechat.WeChatPayConfigDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 微信支付配置
 *
 * @author xxm
 * @since 2021/3/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "微信支付配置")
@Accessors(chain = true)
@TableName(value = "pay_wechat_pay_config",autoResultMap = true)
public class WeChatPayConfig extends MpBaseEntity implements EntityBaseFunction<WeChatPayConfigDto> {

    /** 微信商户Id */
    @DbColumn(comment = "微信商户号")
    private String wxMchId;

    /** 微信应用appId */
    @DbColumn(comment = "微信应用appId")
    private String wxAppId;

    /**
     * 服务器异步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 消息顺序 微信网关 -> 本网关进行处理 -> 发送消息通知业务系统
     */
    @DbColumn(comment = "异步通知路径")
    private String notifyUrl;

    /**
     * 服务器同步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 消息顺序 微信网关 -> 本网关进行处理 -> 重定向到业务系统中
     */
    @DbColumn(comment = "同步通知路径")
    private String returnUrl;

    /** 商户平台「API安全」中的 APIv2 密钥 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    @BigField
    @EncryptionField
    @DbColumn(comment = "APIv2 密钥")
    private String apiKeyV2;

    /** 商户平台「API安全」中的 APIv3 密钥 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    @BigField
    @EncryptionField
    @DbColumn(comment = "APIv3 密钥")
    private String apiKeyV3;

    /** APPID对应的接口密码，用于获取微信公众号jsapi支付时使用 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    @EncryptionField
    @DbColumn(comment = "APPID对应的接口密码")
    private String appSecret;

    /** API证书中p12证书Base64 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    @BigField
    @EncryptionField
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "API证书中p12证书Base64")
    private String p12;

    /** 是否沙箱环境 */
    @DbColumn(comment = "是否沙箱环境")
    private boolean sandbox;

    /** 可用支付方式 */
    @DbColumn(comment = "可用支付方式")
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> payWays;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    @Override
    public WeChatPayConfigDto toDto() {
        return WeChatConvert.CONVERT.convert(this);
    }
}
