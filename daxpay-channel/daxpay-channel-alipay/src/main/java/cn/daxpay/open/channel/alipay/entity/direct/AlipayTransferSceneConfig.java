package cn.daxpay.open.channel.alipay.entity.direct;

import cn.daxpay.open.channel.alipay.convert.direct.AlipayTransferSceneConfigConvert;
import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferSceneConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝转账场景配置
///
/// 按通道商户维度管理转账场景(一对多),承载 2026 年起支付宝对新接入商户强制要求的
/// `transfer_scene_name` 与 `transfer_scene_report_infos`。发起转账时按本表配置注入请求。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("alipay_transfer_scene_config")
public class AlipayTransferSceneConfig extends MchBaseEntity implements ToResult<AlipayTransferSceneConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 转账场景名称(8 枚举之一:现金营销/企业退款/佣金报酬/业务结算/二手回收/公益补助/行政补贴和退款/保险理赔)
    private String sceneName;

    /// 是否启用(一个通道商户最多启用3个, 发起转账时仅可选择启用场景)
    private Boolean enabled;

    /// 是否默认场景(一个通道商户最多一个默认, 默认场景必须启用, 由部分唯一索引约束)
    private Boolean isDefault;

    /// 转换
    @Override
    public AlipayTransferSceneConfigResult toResult() {
        return AlipayTransferSceneConfigConvert.CONVERT.toResult(this);
    }
}
