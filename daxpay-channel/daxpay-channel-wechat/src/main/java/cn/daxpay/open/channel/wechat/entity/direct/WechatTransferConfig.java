package cn.daxpay.open.channel.wechat.entity.direct;

import cn.daxpay.open.channel.wechat.convert.direct.WechatTransferConfigConvert;
import cn.daxpay.open.channel.wechat.result.direct.WechatTransferConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信转账配置
///
/// 一个通道商户一条转账配置(一对一), 合并「转账场景」与「转账发起应用」。
/// 发起转账时由 [cn.daxpay.open.channel.wechat.strategy.direct.transfer.WechatTransferStrategy]
/// 读取本配置注入 transfer_scene 并按 [transferAppRefId] 解析发起应用(公众号)的 wxAppId。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("wechat_transfer_config")
public class WechatTransferConfig extends MchBaseEntity implements ToResult<WechatTransferConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 转账场景ID(微信 transfer_scene, 8 枚举之一, 允许为空待后续补配)
    /// @see cn.daxpay.open.channel.wechat.enums.WechatTransferSceneEnum
    private String transferScene;

    /// 转账发起应用引用(指向 wx_mch_app 主键, 须为公众号类型, 决定 appid 与 openid 来源)
    private Long transferAppRefId;

    /// 转换
    @Override
    public WechatTransferConfigResult toResult() {
        return WechatTransferConfigConvert.CONVERT.toResult(this);
    }
}
