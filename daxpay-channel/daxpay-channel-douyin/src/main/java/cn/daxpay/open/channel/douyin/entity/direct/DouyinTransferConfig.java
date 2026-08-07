package cn.daxpay.open.channel.douyin.entity.direct;

import cn.daxpay.open.channel.douyin.convert.direct.DouyinTransferConfigConvert;
import cn.daxpay.open.channel.douyin.result.direct.DouyinTransferConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 抖音转账配置
///
/// 一个通道商户一条转账配置(一对一), 指定转账发起应用。
/// 发起转账时由 [cn.daxpay.open.channel.douyin.strategy.transfer.DouyinTransferStrategy]
/// 读取本配置按 [transferAppRefId] 解析发起应用(网站应用)的 douyinAppId, 决定转出主体与
/// 收款人 openId 的来源(H5 授权由网站应用承接)。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("douyin_transfer_config")
public class DouyinTransferConfig extends MchBaseEntity implements ToResult<DouyinTransferConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 转账发起应用引用(指向 dy_mch_app 主键, 须为网站应用 web_app, 决定转出 appid 与 openid 来源)
    private Long transferAppRefId;

    /// 转换
    @Override
    public DouyinTransferConfigResult toResult() {
        return DouyinTransferConfigConvert.CONVERT.toResult(this);
    }
}
