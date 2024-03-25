package cn.bootx.platform.daxpay.service.handler.sequence;

import cn.bootx.platform.common.sequence.func.Sequence;
import cn.bootx.platform.common.sequence.util.SequenceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * 序列生成器配置类
 * @author xxm
 * @since 2024/2/28
 */
@Configuration
@RequiredArgsConstructor
public class DaxPaySequenceHandler {

    /**
     * 支付宝对账单序列生成器
     */
    public Sequence alipayReconcileSequence(String date) {
        return SequenceUtil.create(1,1,1,"AlipayReconcileSequence"+date);
    }

    /**
     * 微信对账单序列生成器
     */
    public Sequence wechatReconcileSequence(String date) {
        return SequenceUtil.create(1,1,1,"WechatReconcileSequence"+date);
    }

    /**
     * 云闪付对账单序列生成器
     */
    public Sequence unionPayReconcileSequence(String date) {
        return SequenceUtil.create(1,1,1,"UnionPayReconcileSequence"+date);
    }
}
