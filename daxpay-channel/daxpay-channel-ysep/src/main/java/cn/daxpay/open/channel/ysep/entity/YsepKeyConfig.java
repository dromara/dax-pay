package cn.daxpay.open.channel.ysep.entity;

import cn.daxpay.open.channel.ysep.convert.YsepKeyConfigConvert;
import cn.daxpay.open.channel.ysep.result.YsepKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 银盛通道密钥配置
///
/// 通道商户维度的银盛直连配置, 一通道两产品共用本表(通道商户号全局唯一, 行内 product 字段表达产品归属,
/// 对齐富友 fuyou_rsa_key_config 先例): 发起方商户号 + 收款商户号 + 商户私钥证书(pfx+密码) + 银盛平台公钥证书,
/// 敏感字段(证书/密码)通过 [DataEncryptTypeHandler] 加密入库。
/// channelMchNo 创建时录入不可修改; 密钥字段由密钥配置维护(空值不覆盖)。
///
/// 签名算法: SHA1withRSA(两产品族同源), 商户私钥证书签名 / 银盛平台公钥证书验签。
/// 银盛不提供集测沙箱网关, 不设沙箱字段。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "ysep_key_config", autoResultMap = true)
public class YsepKeyConfig extends MchBaseEntity implements ToResult<YsepKeyConfigResult> {

    /// 通道商户号(关联 mch_channel_merchant, 创建后不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 所属支付产品(ysep_pay 银盛支付 / yseqt_pay 银盛e企通, 表达该密钥行的产品归属)
    private String product;

    /// 发起方商户号(ysep_pay=partner_id / yseqt_pay=srcMerchantNo)
    private String sponsorMchNo;

    /// 收款商户号(ysep_pay=seller_id / yseqt_pay=payeeMerchantNo)
    private String payeeMchNo;

    /// 业务代码(ysep_pay 专用 business_code, 银盛后台分配; yseqt_pay 不使用)
    private String businessCode;

    /// 商户私钥证书 pfx 内容(Base64, PKCS12, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantCertPfx;

    /// 商户私钥证书密码(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantCertPassword;

    /// 银盛平台公钥证书 cer 内容(Base64/PEM, 响应/回调验签, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String ysepayPublicCert;

    @Override
    public YsepKeyConfigResult toResult() {
        return YsepKeyConfigConvert.CONVERT.toResult(this);
    }
}
