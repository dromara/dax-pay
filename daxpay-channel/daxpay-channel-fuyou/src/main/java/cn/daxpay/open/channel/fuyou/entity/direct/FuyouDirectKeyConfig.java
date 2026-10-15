package cn.daxpay.open.channel.fuyou.entity.direct;

import cn.daxpay.open.channel.fuyou.convert.direct.FuyouDirectKeyConfigConvert;
import cn.daxpay.open.channel.fuyou.result.direct.FuyouDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 富友直连商户密钥配置
///
/// 富友「商户移动支付前置接口」(aggregatePay) 为直连商户模式, 每个通道商户持有自己的
/// 商户号(mchnt_cd) + 商户密钥(mchnt_key) + 终端号(term_id), 与商户一一对应。
///
/// 签名算法: MD5(固定顺序拼接 + mchnt_key), 商户密钥通过 [DataEncryptTypeHandler] 加密入库。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "fuyou_direct_key_config", autoResultMap = true)
public class FuyouDirectKeyConfig extends MchBaseEntity implements ToResult<FuyouDirectKeyConfigResult> {

    /// 通道商户号(FUYOU+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 是否沙箱环境商户(创建通道商户时按产品 activeEnv 固化)
    private boolean sandbox;

    /// 富友商户号(mchnt_cd)
    private String mchntCd;

    /// 商户密钥(mchnt_key, MD5 签名用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String mchntKey;

    /// 终端号(term_id, 8 位随机数字字母组合)
    private String termNo;

    /// 转换
    @Override
    public FuyouDirectKeyConfigResult toResult() {
        return FuyouDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
