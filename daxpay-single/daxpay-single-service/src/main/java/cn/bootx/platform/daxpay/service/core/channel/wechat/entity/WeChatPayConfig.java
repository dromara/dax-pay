package cn.bootx.platform.daxpay.service.core.channel.wechat.entity;

import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.annotation.EncryptionField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.StringListTypeHandler;
import cn.bootx.platform.daxpay.service.code.WeChatPayCode;
import cn.bootx.platform.daxpay.service.core.channel.wechat.convert.WeChatConvert;
import cn.bootx.platform.daxpay.service.dto.channel.wechat.WeChatPayConfigDto;
import cn.bootx.platform.daxpay.service.param.channel.wechat.WeChatPayConfigParam;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
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

    /** 名称 */
    @DbColumn(comment = "名称")
    private String name;

    /** 商户编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @DbColumn(comment = "商户编码")
    private String mchCode;

    /** 商户应用编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @DbMySqlIndex(comment = "商户应用编码唯一索引")
    @DbColumn(comment = "商户应用编码")
    private String mchAppCode;

    /** 微信商户Id */
    @DbColumn(comment = "微信商户号")
    private String wxMchId;

    /** 微信应用appId */
    @DbColumn(comment = "微信应用appId")
    private String wxAppId;

     /**
     * api版本
     * @see WeChatPayCode#API_V2
     */
     @DbColumn(comment = "api版本")
     private String apiVersion;

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

    public static WeChatPayConfig init(WeChatPayConfigParam in) {
        return WeChatConvert.CONVERT.convert(in);
    }

}
