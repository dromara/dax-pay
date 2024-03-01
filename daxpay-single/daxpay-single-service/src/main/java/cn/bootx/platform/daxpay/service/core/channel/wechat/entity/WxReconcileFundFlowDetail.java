package cn.bootx.platform.daxpay.service.core.channel.wechat.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
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

    @DbColumn(comment = "关联对账订单ID")
    private Long recordOrderId;
    @Alias("记账时间")
    @DbColumn(comment = "记账时间")
    private String time;
    @Alias("微信支付业务单号")
    @DbColumn(comment = "微信支付业务单号")
    private String outTradeNo;
    @Alias("资金流水单号")
    @DbColumn(comment = "资金流水单号")
    private String flowId;
    @Alias("业务名称")
    @DbColumn(comment = "业务名称")
    private String name;
    @Alias("业务类型")
    @DbColumn(comment = "业务类型")
    private String businessType;
    @Alias("收支类型")
    @DbColumn(comment = "收支类型")
    private String incomeType;
    @Alias("收支金额(元)")
    @DbColumn(comment = "收支金额(元)")
    private String amount;
    @Alias("账户结余(元)")
    @DbColumn(comment = "账户结余(元)")
    private String balance;
    @Alias("资金变更提交申请人")
    @DbColumn(comment = "资金变更提交申请人")
    private String applyUser;
    @Alias("备注")
    @DbColumn(comment = "备注")
    private String remark;
    @Alias("业务凭证号")
    @DbColumn(comment = "业务凭证号")
    private String voucherNo;
}
