package cn.daxpay.single.service.core.channel.alipay.entity;

import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.StringListTypeHandler;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.common.typehandler.DecryptTypeHandler;
import cn.daxpay.single.service.core.channel.alipay.convert.AlipayConvert;
import cn.daxpay.single.service.dto.channel.alipay.AliPayConfigDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 支付宝支付配置
 *
 * @author xxm
 * @since 2020/12/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付宝支付配置")
@TableName(value = "pay_alipay_config",autoResultMap = true)
public class AliPayConfig extends MpBaseEntity implements EntityBaseFunction<AliPayConfigDto> {

    /** 支付宝商户appId */
    @DbColumn(comment = "支付宝商户appId")
    private String appId;

    /** 是否启用, 只影响支付和退款操作 */
    @DbColumn(comment = "是否启用")
    private Boolean enable;

    /** 是否支付分账 */
    @DbColumn(comment = "是否支付分账")
    private Boolean allocation;

    /** 支付限额 */
    @DbColumn(comment = "支付限额")
    private Integer singleLimit;

    /**
     * 服务器异步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 调用顺序 支付宝网关 -> 本网关进行处理 -> 发送消息通知业务系统
     */
    @DbColumn(comment = "异步通知页面路径")
    private String notifyUrl;

    /**
     * 服务器同步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 消息顺序 支付宝网关 -> 本网关进行处理 -> 重定向到业务系统中
     */
    @DbColumn(comment = "同步通知页面路径")
    private String returnUrl;

    /** 支付网关地址 */
    @DbColumn(comment = "支付网关地址")
    private String serverUrl;

    /**
     * 认证类型 证书/公钥
     * @see AliPayCode#AUTH_TYPE_KEY
     */
    @DbColumn(comment = "认证类型")
    private String authType;

    /** 签名类型 RSA2 */
    @DbColumn(comment = "签名类型 RSA2")
    public String signType;

    /**
     * 是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取
     */
    @TableField(typeHandler = DecryptTypeHandler.class)
    @DbColumn(comment = "合作者身份ID")
    private String alipayUserId;

    /** 支付宝公钥 */
    @BigField
    @TableField(typeHandler = DecryptTypeHandler.class)
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "支付宝公钥")
    public String alipayPublicKey;

    /** 私钥 */
    @BigField
    @TableField(typeHandler = DecryptTypeHandler.class)
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "私钥")
    private String privateKey;

    /** 应用公钥证书 */
    @BigField
    @TableField(typeHandler = DecryptTypeHandler.class)
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "应用公钥证书")
    private String appCert;

    /** 支付宝公钥证书 */
    @BigField
    @TableField(typeHandler = DecryptTypeHandler.class)
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "支付宝公钥证书")
    private String alipayCert;

    /** 支付宝CA根证书 */
    @BigField
    @TableField(typeHandler = DecryptTypeHandler.class)
    @DbColumn(comment = "支付宝CA根证书")
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    private String alipayRootCert;

    /** 是否沙箱环境 */
    @DbColumn(comment = "是否沙箱环境")
    private boolean sandbox;

    /** 可用支付方式 */
    @DbColumn(comment = "可用支付方式")
    @DbMySqlFieldType(MySqlFieldTypeEnum.VARCHAR)
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> payWays;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    @Override
    public AliPayConfigDto toDto() {
        return AlipayConvert.CONVERT.convert(this);
    }

}
