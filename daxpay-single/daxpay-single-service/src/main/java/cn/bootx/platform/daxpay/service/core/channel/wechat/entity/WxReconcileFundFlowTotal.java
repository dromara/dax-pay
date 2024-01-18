package cn.bootx.platform.daxpay.service.core.channel.wechat.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信资金账单汇总
 * @author xxm
 * @since 2024/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pay_wechat_reconcile_fund_flow_total")
public class WxReconcileFundFlowTotal extends MpCreateEntity {

    @Alias("资金流水总笔数")
    private String totalNum;
    @Alias("收入笔数")
    private String incomeNum;
    @Alias("收入金额")
    private String incomeAmount;
    @Alias("支出笔数")
    private String expenditureNum;
    @Alias("支出金额")
    private String expenditureAmount;
}
