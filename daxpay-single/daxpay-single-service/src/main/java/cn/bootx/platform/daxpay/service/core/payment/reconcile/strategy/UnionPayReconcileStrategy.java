package cn.bootx.platform.daxpay.service.core.payment.reconcile.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.union.service.UnionPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.union.service.UnionPayReconcileService;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 *
 * @author xxm
 * @since 2024/3/8
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UnionPayReconcileStrategy extends AbsReconcileStrategy {

    private final UnionPayConfigService configService;

    private final UnionPayReconcileService reconcileService;

    private UnionPayKit unionPayKit;

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.UNION_PAY;
    }

    /**
     * 对账前处理, 主要是初始化支付SDK配置
     */
    @Override
    public void doBeforeHandler() {
        UnionPayConfig config = configService.getConfig();
        // 测试环境使用测试号
        if (config.isSandbox()) {
            config.setMachId("700000000000001");
        }
        this.unionPayKit = configService.initPayService(config);
    }

    /**
     * 上传对账单解析并保存
     */
    @SneakyThrows
    @Override
    public void upload(MultipartFile file) {
        reconcileService.upload(file.getBytes());
    }

    /**
     * 下载对账单到本地进行保存
     */
    @Override
    public void downAndSave() {
        Date date = DateUtil.date(this.getRecordOrder().getDate());
        reconcileService.downAndSave(this.getRecordOrder(),date, this.unionPayKit);
    }
}
