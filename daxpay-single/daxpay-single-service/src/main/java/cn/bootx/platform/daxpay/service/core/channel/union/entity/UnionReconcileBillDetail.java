package cn.bootx.platform.daxpay.service.core.channel.union.entity;

import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 云闪付业务明细对账单
 * @author xxm
 * @since 2024/3/24
 */
@Data
@Accessors(chain = true)
@DbTable(comment = "云闪付业务明细对账单")
@TableName("pay_union_reconcile_bill_detail")
public class UnionReconcileBillDetail {

    /** 关联对账订单ID */
    @DbColumn(comment = "关联对账订单ID")
    private Long recordOrderId;
    /** 交易代码 */
    @DbColumn(comment = "交易代码")
    private String tradeType;
    /** 代理机构标识码 */
    /** 发送机构标识码  */
    /** 系统跟踪号 */
    /** 交易传输时间 */
    @DbColumn(comment = "交易传输时间")
    private String txnTime;
    /** 帐号 */
    /** 交易金额 */
    @DbColumn(comment = "交易金额")
    private String txnAmt;
    /** 商户类别 */
    /** 终端类型 */
    /** 查询流水号 */
    @DbColumn(comment = "查询流水号")
    private String queryId;
    /** 支付方式（旧） */
    /** 商户订单号  */
    @DbColumn(comment = "商户订单号")
    private String orderId;
    /** 支付卡类型 */
    /** 原始交易的系统跟踪号  */
    /** 原始交易日期时间  */
    /** 商户手续费 */
    /** 结算金额 */
    /** 支付方式 */
    /** 集团商户代码  */
    /** 交易类型 */
    /** 交易子类 */
    /** 业务类型  */
    /** 帐号类型 */
    /** 账单类型 */
    /** 账单号码 */
    /** 交互方式 */
    /** 商户代码 */
    /** 分账入账方式  */
    /** 二级商户代码 */
    /** 二级商户简称 */
    /** 二级商户分账入账金额 */
    /** 清算净额 */
    /** 终端号 */
    /** 商户自定义域  */
    /** 优惠金额 */
    /** 发票金额 */
    /** 分期付款附加手续费 */
    /** 分期付款期数 */
    /** 交易介质 */
    /** 原始交易订单号  */
    /** 清算金额 */
    /** 服务点输入方式码 */
    /** 移动支付产品标志 */
    /** 交易代码 */
    /** 检索参考号 */
    /** 保留使用 */
}
