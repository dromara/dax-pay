package cn.daxpay.multi.gateway.task;

import cn.daxpay.multi.service.dao.config.ChannelConfigManager;
import cn.daxpay.multi.service.dao.constant.ChannelConstManager;
import cn.daxpay.multi.service.dao.merchant.MchAppManager;
import cn.daxpay.multi.service.entity.config.ChannelConfig;
import cn.daxpay.multi.service.entity.constant.ChannelConst;
import cn.daxpay.multi.service.entity.merchant.MchApp;
import cn.daxpay.multi.service.entity.reconcile.ReconcileStatement;
import cn.daxpay.multi.service.enums.MchAppStautsEnum;
import cn.daxpay.multi.service.param.reconcile.ReconcileCreatParam;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.service.reconcile.ReconcileStatementService;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 每日对账定时任务, 一天一次, 上午10.30执行
 * @author xxm
 * @since 2024/1/20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReconcileTask  {
    private final ReconcileStatementService reconcileService;

    private final MchAppManager mchAppManager;

    private final ChannelConfigManager channelConfigManager;

    private final ChannelConstManager channelConstManager;

    private final PaymentAssistService paymentAssistService;



    /**
     * 任务实现, 上午10.30执行
     */
    @Scheduled(cron = "0 30 10 * * ?")
    public void reconcileTask() {
        Map<String, String> channelMap = channelConstManager.findAll()
                .stream()
                .collect(Collectors.toMap(ChannelConst::getCode, ChannelConst::getName));

        // 遍历所有启用的应用
        List<MchApp> mchApps = mchAppManager.findAllByStatus(MchAppStautsEnum.ENABLE);
        // 遍历应用下启用的通道
        for (MchApp mchApp : mchApps) {
            // 设置上下文
            paymentAssistService.initMchAndApp(mchApp.getMchNo(), mchApp.getAppId());
            List<ChannelConfig> configs = channelConfigManager.findEnableByAppId(mchApp.getAppId());
            for (ChannelConfig config : configs) {
                try {
                    log.info("商户: {}, 应用: {} 通道: {} 执行对账任务任务 }", mchApp.getMchNo(), mchApp.getAppId(), config.getChannel());
                    this.reconcileTaskRun(mchApp, config, channelMap);
                } catch (Exception e) {
                    log.error("商户: {}, 应用: {} 通道: {} 执行对账任务失败 }", mchApp.getMchNo(), mchApp.getAppId(), config.getChannel(), e);
                }
            }
        }
    }

    /**
     * 执行任务
     */
    public void reconcileTaskRun(MchApp mchApp, ChannelConfig config, Map<String, String> channelMap){
        // 1. 创建订单
        // 标题 【日期】 - 应用 - 通道
        String title = StrUtil.format("【{}】{}-{}-{}",
                DateUtil.format(DateUtil.yesterday(), DatePattern.NORM_DATE_PATTERN), mchApp.getMchNo(), mchApp.getAppId(), channelMap.get(config.getChannel()));
        ReconcileCreatParam param = new ReconcileCreatParam()
                .setMchNo(mchApp.getMchNo())
                .setAppId(mchApp.getAppId())
                .setChannel(config.getChannel())
                .setDate(LocalDate.now().plusDays(-1))
                .setTitle(title);
        ReconcileStatement statement = reconcileService.create(param);
        // 2. 执行对账任务, 下载对账单并解析和存储
        reconcileService.downAndSave(statement);
        // 3. 执行账单明细比对, 生成差异单
        reconcileService.compare(statement);
    }

}
