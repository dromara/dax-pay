package cn.daxpay.open.channel.leshua.entity.isv;

import cn.daxpay.open.channel.leshua.convert.isv.LeshuaIsvKeyConfigConvert;
import cn.daxpay.open.channel.leshua.result.isv.LeshuaIsvKeyConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 乐刷服务商密钥配置
///
/// 乐刷为收单机构服务商模式, 服务商密钥全局唯一(按 product 查询),
/// 子商户的乐刷商户号(merchant_id)在通道商户绑定([LeshuaIsvChannelMerchant])维护,
/// 每个商户用自己的乐刷商户号收款, 服务商只统一持密钥与签名方式。
///
/// 签名算法: MD5 或 SM3, 用 tradeKey 做请求签名与回调验签。
/// 敏感字段(tradeKey/notifyKey)通过 [DataEncryptTypeHandler] 加密入库。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "leshua_isv_key_config", autoResultMap = true)
public class LeshuaIsvKeyConfig extends MpBaseEntity implements ToResult<LeshuaIsvKeyConfigResult> {

    /// 产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 交易密钥(tradeKey, 交易/进件等所有服务商接口请求签名, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String tradeKey;

    /// 异步通知密钥(notifyKey, 异步通知回调验签, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String notifyKey;

    /// 签名类型(MD5 / SM3)
    private String signType;

    /// 乐刷服务商号(lsIsvNo, 进件等接口场景必传)
    private String lsIsvNo;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 转换
    @Override
    public LeshuaIsvKeyConfigResult toResult() {
        return LeshuaIsvKeyConfigConvert.CONVERT.toResult(this);
    }
}
