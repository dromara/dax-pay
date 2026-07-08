package cn.daxpay.open.channel.fuyou.entity.isv;

import cn.daxpay.open.channel.fuyou.convert.isv.FuyouIsvKeyConfigConvert;
import cn.daxpay.open.channel.fuyou.result.isv.FuyouIsvKeyConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 富友服务商密钥配置
///
/// 富友为收单机构服务商模式, 服务商密钥全局唯一(按 product 查询),
/// 子商户仅需富友商户号(merchantNo) + 终端号(termNo), 见 [FuyouIsvChannelMerchant]。
///
/// 签名算法: MD5withRSA, 私钥签名 / 公钥验签。
/// 敏感字段(私钥/公钥)通过 [DataEncryptTypeHandler] 加密入库。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "fuyou_isv_key_config", autoResultMap = true)
public class FuyouIsvKeyConfig extends MpBaseEntity implements ToResult<FuyouIsvKeyConfigResult> {

    /// 产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 富友应用编号(机构号 ins_cd)
    private String fyAppId;

    /// 富友订单前缀(关联订单号前缀, 富友回调凭 mchnt_order_no 反查平台订单)
    private String orderPrefix;

    /// 商户RSA私钥(PKCS8 Base64, MD5withRSA 签名, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /// 富友RSA公钥(X509 Base64, 响应/回调验签, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKey;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 转换
    @Override
    public FuyouIsvKeyConfigResult toResult() {
        return FuyouIsvKeyConfigConvert.CONVERT.toResult(this);
    }
}
