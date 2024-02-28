package cn.bootx.platform.daxpay.service.configuration.sequence;

import cn.bootx.platform.common.sequence.func.Sequence;
import cn.bootx.platform.common.sequence.range.SeqRangeManager;
import cn.bootx.platform.common.sequence.util.SequenceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 序列生成器配置类
 * @author xxm
 * @since 2024/2/28
 */
@Configuration
@RequiredArgsConstructor
public class DaxPaySequenceConfiguration {

    /**
     * 支付宝对账单序列生成器
     */
    @Bean
    public Sequence alipayReconcileSequence(SeqRangeManager seqRangeManager) {
        return SequenceUtil.create(1,1,1,"AlipayReconcileSequence");
    }

    /**
     * 微信对账单序列生成器
     */
    @Bean
    public Sequence wechatReconcileSequence(SeqRangeManager seqRangeManager) {
        return SequenceUtil.create(1,1,1,"WechatReconcileSequence");
    }
}
