package org.dromara.daxpay.payment.channel.strategy;

import org.dromara.daxpay.platform.core.enums.channel.OnbApplyTypeEnum;
import org.dromara.daxpay.payment.channel.bo.OnbMchApplyResultBo;
import org.dromara.daxpay.payment.channel.bo.OnbMchApplyStatusBo;
import org.dromara.daxpay.payment.channel.entity.apply.OnbMchApply;
import org.dromara.daxpay.payment.channel.param.apply.OnbMchApplyParam;

import java.util.Collections;
import java.util.List;

/// # 商户进件策略
///
public abstract class AbsOnbMchApplyStrategy implements OnbStrategy {

    /// 支持的进件类型
    public List<OnbApplyTypeEnum> getApplyType(){
        return Collections.singletonList(OnbApplyTypeEnum.MERCHANT);
    }

    /// 初始化进件申请资料
    public void initApplyData(OnbMchApply apply, OnbMchApplyParam param){

    }

    /// 发起进件申请
    abstract public OnbMchApplyResultBo apply(OnbMchApply apply);

    /// 查询进件结果
    abstract public OnbMchApplyStatusBo queryResult(OnbMchApply apply);

}
