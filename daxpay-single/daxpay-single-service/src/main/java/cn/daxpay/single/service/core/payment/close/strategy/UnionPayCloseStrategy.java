package cn.daxpay.single.service.core.payment.close.strategy;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.union.entity.UnionPayConfig;
import cn.daxpay.single.service.core.channel.union.service.UnionPayCloseService;
import cn.daxpay.single.service.core.channel.union.service.UnionPayConfigService;
import cn.daxpay.single.service.func.AbsPayCloseStrategy;
import cn.daxpay.single.service.sdk.union.api.UnionPayKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class UnionPayCloseStrategy extends AbsPayCloseStrategy {

    private final UnionPayCloseService unionPayCloseService;

    private final UnionPayConfigService unionPayConfigService;

    private UnionPayConfig unionPayConfig;


    @Override
    public String getChannel() {
        return PayChannelEnum.UNION_PAY.getCode();
    }

    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCloseHandler() {
        this.unionPayConfig = unionPayConfigService.getConfig();
    }

    /**
     * 关闭操作
     */
    @Override
    public void doCloseHandler() {
        UnionPayKit unionPayKit = unionPayConfigService.initPayService(this.unionPayConfig);
        unionPayCloseService.close(this.getOrder(), unionPayKit);
    }
}
