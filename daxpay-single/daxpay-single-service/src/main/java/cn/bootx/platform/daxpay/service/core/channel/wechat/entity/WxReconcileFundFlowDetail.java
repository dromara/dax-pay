package cn.bootx.platform.daxpay.service.core.channel.wechat.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信资金账单明细
 * @author xxm
 * @since 2024/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pay_wechat_reconcile_fund_flow_detail")
public class WxReconcileFundFlowDetail extends MpCreateEntity {
    @Alias("记账时间")
    private String time;
    @Alias("微信支付业务单号")
    private String outTradeNo;
    @Alias("资金流水单号")
    private String id;
    @Alias("业务名称")
    private String packet1;
    @Alias("业务类型")
    private String packet2;
    @Alias("收支类型")
    private String packet3;
    @Alias("收支金额(元)")
    private String amount;
    @Alias("账户结余(元)")
    private String balance;
    @Alias("资金变更提交申请人")
    private String packet4;
    @Alias("备注")
    private String packet5;
    @Alias("业务凭证号")
    private String packet6;
}
