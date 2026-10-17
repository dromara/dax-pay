package cn.daxpay.open.channel.shouqianba.entity.direct;

import cn.daxpay.open.channel.shouqianba.convert.direct.ShouqianbaDirectKeyConfigConvert;
import cn.daxpay.open.channel.shouqianba.result.direct.ShouqianbaDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 收钱吧直连配置
///
/// 直连商户维度的通道配置: 终端号(terminal_sn, 收钱吧终端激活后获得, 创建时录入不可修改)与
/// 终端密钥(terminal_key, 签到重置后可经密钥配置更新, 加密存储)。
/// 收钱吧为终端凭证直连形态, 一个通道商户对应一个收钱吧终端。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "shouqianba_direct_key_config", autoResultMap = true)
public class ShouqianbaDirectKeyConfig extends MchBaseEntity implements ToResult<ShouqianbaDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 收钱吧终端号(terminal_sn, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String terminalSn;

    /// 收钱吧终端密钥(terminal_key, 签名用, 加密存储; 签到重置后更新)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String terminalKey;

    /// 操作员(收钱吧要求上送, 缺省 daxpay)
    private String operator;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public ShouqianbaDirectKeyConfigResult toResult() {
        return ShouqianbaDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
