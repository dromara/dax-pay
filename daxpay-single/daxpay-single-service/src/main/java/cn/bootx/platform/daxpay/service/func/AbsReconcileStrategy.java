package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileOrder;
import lombok.Getter;

/**
 * 支付对账策略
 * @author xxm
 * @since 2024/1/18
 */
@Getter
public abstract class AbsReconcileStrategy implements PayStrategy {

    /** 对账订单 */
    private PayReconcileOrder recordOrder;

    /**
     * 对账前处理, 主要是初始化支付SDK配置
     */
    public void doBeforeHandler() {
    }

    /**
     * 初始化参数
     */
    public void initParam(PayReconcileOrder recordOrder){
        this.recordOrder = recordOrder;
    }

    /**
     * 下载对账单到本地进行保存
     */
    public abstract void downAndSave();

    /**
     * 比对生成对账差异单
     * 1. 远程有, 本地无  补单(追加回订单/记录差异表)
     * 2. 远程无, 本地有  记录差错表
     * 3. 远程有, 本地有, 但状态不一致
     */
    public void offsetting(){

    }

}
