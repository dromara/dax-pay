package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.code.PayChannelEnum;

import java.time.LocalDate;

/**
 * 支付对账策略
 * @author xxm
 * @since 2024/1/18
 */
public abstract class AbsReconcileStrategy {



    /**
     * 策略标识
     * @see PayChannelEnum
     */
    public abstract PayChannelEnum getType();

    /**
     * 对账前处理, 主要是初始化支付SDK配置
     */
    public void doBeforeHandler() {
    }


    /**
     * 下载对账单到本地进行保存
     */
    public abstract void downAndSave(LocalDate date);

    /**
     * 轧差
     * 1. 远程有, 本地无  补单(追加回订单/记录差异表)
     * 2. 远程无, 本地有  记录差错表
     * 3. 远程有, 本地有, 但状态不一致
     */
    public void offsetting(LocalDate date){
    }

}
