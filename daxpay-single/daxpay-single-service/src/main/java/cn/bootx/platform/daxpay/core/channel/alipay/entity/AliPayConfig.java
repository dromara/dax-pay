package cn.bootx.platform.daxpay.core.channel.alipay.entity;

import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.annotation.EncryptionField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.StringListTypeHandler;
import cn.bootx.platform.daxpay.code.AliPayCode;
import cn.bootx.platform.daxpay.core.channel.alipay.convert.AlipayConvert;
import cn.bootx.platform.daxpay.dto.channel.alipay.AliPayConfigDto;
import cn.bootx.platform.daxpay.param.channel.alipay.AliPayConfigParam;
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
@TableName("pay_alipay_config")
public class AliPayConfig extends MpBaseEntity implements EntityBaseFunction<AliPayConfigDto> {

    /** 支付宝商户appId */
    @DbColumn(comment = "支付宝商户appId")
    private String appId;

    /** 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 */
    @DbColumn(comment = "异步通知页面路径")
    private String notifyUrl;

    /** 请求网关地址 */
    @DbColumn(comment = "")
    private String serverUrl;

    /**
     * 认证类型 证书/公钥
     * @see AliPayCode#AUTH_TYPE_KEY
     */
    @DbColumn(comment = "认证类型")
    private String authType;

    /** 签名类型 RSA/RSA2 */
    @DbColumn(comment = "签名类型 RSA/RSA2")
    public String signType;

    /** 支付宝公钥 */
    @BigField
    @EncryptionField
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "支付宝公钥")
    public String alipayPublicKey;

    /** 私钥 */
    @BigField
    @EncryptionField
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "私钥")
    private String privateKey;

    /** 应用公钥证书 */
    @BigField
    @EncryptionField
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "应用公钥证书")
    private String appCert;

    /** 支付宝公钥证书 */
    @BigField
    @EncryptionField
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "支付宝公钥证书")
    private String alipayCert;

    /** 支付宝CA根证书 */
    @BigField
    @EncryptionField
    @DbColumn(comment = "支付宝CA根证书")
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    private String alipayRootCert;

    /** 是否沙箱环境 */
    @DbColumn(comment = "是否沙箱环境")
    private boolean sandbox;

    /** 可用支付方式 */
    @DbColumn(comment = "可用支付方式")
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> payWays;

    @DbColumn(comment = "状态")
    private String status;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    @Override
    public AliPayConfigDto toDto() {
        return AlipayConvert.CONVERT.convert(this);
    }

    public static AliPayConfig init(AliPayConfigParam in) {
        return AlipayConvert.CONVERT.convert(in);
    }

}
