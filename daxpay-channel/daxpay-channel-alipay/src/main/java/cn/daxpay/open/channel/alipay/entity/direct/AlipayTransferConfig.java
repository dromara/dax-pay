package cn.daxpay.open.channel.alipay.entity.direct;

import cn.daxpay.open.channel.alipay.convert.direct.AlipayTransferConfigConvert;
import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝转账配置
///
/// 一个通道商户一条转账配置(一对一), 绑定「转账转出应用」。
/// 发起转账时由 [cn.daxpay.open.channel.alipay.strategy.direct.transfer.AlipayTransferStrategy]
/// 读取本配置按 [transferAppRefId] 解析转出应用(支付宝直连应用)的 aliAppId 与密钥。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("alipay_transfer_config")
public class AlipayTransferConfig extends MchBaseEntity implements ToResult<AlipayTransferConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 转账转出应用引用(指向 alipay_direct_app 主键, 决定转账使用的 aliAppId 与密钥)
    private Long transferAppRefId;

    /// 转换
    @Override
    public AlipayTransferConfigResult toResult() {
        return AlipayTransferConfigConvert.CONVERT.toResult(this);
    }
}
