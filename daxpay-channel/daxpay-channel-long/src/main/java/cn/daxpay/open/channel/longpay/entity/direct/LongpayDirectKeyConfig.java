package cn.daxpay.open.channel.longpay.entity.direct;

import cn.daxpay.open.channel.longpay.convert.direct.LongpayDirectKeyConfigConvert;
import cn.daxpay.open.channel.longpay.result.direct.LongpayDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 建行龙支付直连配置
///
/// 直连商户维度的通道配置: 建行商户三要素(商户号 MERCHANTID / 柜台号 POSID / 分行号 BRANCHID)
/// 与两把公钥(柜台公钥 MD5 摘要用 + 建行公钥回调验签用, 加密存储)。
/// 三要素创建时录入, 公钥由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "long_pay_direct_key_config", autoResultMap = true)
public class LongpayDirectKeyConfig extends MchBaseEntity implements ToResult<LongpayDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 建行商户号(MERCHANTID, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String merchantId;

    /// 建行柜台号(POSID, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String posId;

    /// 建行分行号(BRANCHID, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String branchId;

    /// 柜台公钥(请求 MD5 摘要用, 取后 30 位参与 MAC, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPublicKey;

    /// 建行公钥(异步通知 RSA 验签用, DER hex 或 PEM, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String ccbPublicKey;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public LongpayDirectKeyConfigResult toResult() {
        return LongpayDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
