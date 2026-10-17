package cn.daxpay.open.channel.helipay.entity;

import cn.daxpay.open.channel.helipay.convert.HelipayKeyConfigConvert;
import cn.daxpay.open.channel.helipay.result.HelipayKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 合利宝通道密钥配置
///
/// 通道商户维度的合利宝扫码产品线对接配置, 含商户编号、子商户号、扫码产品签名密钥与
/// 公众号/H5 场景 AppId, 敏感字段(签名密钥)通过 [DataEncryptTypeHandler] 加密入库。
/// channelMchNo 创建后不可修改; 密钥字段由密钥配置维护(空值不覆盖)。
///
/// 签名算法: MD5(参数值顺序拼接 + 签名密钥, 请求签名与回调验签共用)。
/// 合利宝无独立沙箱网关, 不设沙箱字段。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "helipay_key_config", autoResultMap = true)
public class HelipayKeyConfig extends MchBaseEntity implements ToResult<HelipayKeyConfigResult> {

    /// 通道商户号(关联 mch_channel_merchant, 创建后不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 合利宝商户编号(customerNumber, 上送 P3_customerNumber)
    private String customerNumber;

    /// 子商户号(可空; 设置后覆盖 P3_customerNumber 发起交易)
    private String subCustomerNumber;

    /// 扫码产品签名密钥(MD5 签名与回调验签共用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String signKey;

    /// 公众号/生活号支付 AppId(微信 JS 支付必填; 支付宝生活号固定传 1 由子应用处理)
    private String publicAppId;

    /// H5 支付 AppId(可空, 微信 H5 场景使用)
    private String h5AppId;

    /// 扫码支付报备ID(可空, 上送 P15_subMerchantId)
    private String reportId;

    @Override
    public HelipayKeyConfigResult toResult() {
        return HelipayKeyConfigConvert.CONVERT.toResult(this);
    }
}
