package cn.daxpay.multi.service.service.reconcile;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.daxpay.multi.core.exception.OperationFailException;
import cn.daxpay.multi.core.util.TradeNoGenerateUtil;
import cn.daxpay.multi.service.bo.reconcile.ChannelReconcileTradeBo;
import cn.daxpay.multi.service.bo.reconcile.ReconcileResolveResultBo;
import cn.daxpay.multi.service.convert.reconcile.ReconcileConvert;
import cn.daxpay.multi.service.dao.reconcile.ChannelReconcileTradeManage;
import cn.daxpay.multi.service.dao.reconcile.ReconcileStatementManager;
import cn.daxpay.multi.service.entity.reconcile.ChannelReconcileTrade;
import cn.daxpay.multi.service.entity.reconcile.ReconcileStatement;
import cn.daxpay.multi.service.enums.ReconcileFileTypeEnum;
import cn.daxpay.multi.service.param.reconcile.ReconcileCreatParam;
import cn.daxpay.multi.service.param.reconcile.ReconcileUploadParam;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.strategy.AbsReconcileStrategy;
import cn.daxpay.multi.service.util.PaymentStrategyFactory;
import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 对账服务类
 * @author xxm
 * @since 2024/8/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileService {

    private final ReconcileStatementManager reconcileOrderManager;
    private final PaymentAssistService paymentAssistService;
    private final ReconcileStatementManager reconcileStatementManager;
    private final ChannelReconcileTradeManage reconcileTradeManage;

    /**
     * 创建对账订单,
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ReconcileStatement create(ReconcileCreatParam param) {
        ReconcileStatement order = new ReconcileStatement()
                .setReconcileNo(TradeNoGenerateUtil.reconciliation())
                .setChannel(param.getChannel())
                .setDate(param.getDate());
        reconcileOrderManager.save(order);
        return order;
    }

    /**
     * 下载对账单并进行保存
     */
    public void downAndSave(Long reconcileOrderId) {
        ReconcileStatement reconcileOrder = reconcileStatementManager.findById(reconcileOrderId)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        this.downAndSave(reconcileOrder);
    }

    /**
     * 下载交易对账单并进行保存
     */
    public void downAndSave(ReconcileStatement reconcileStatement) {
        // 如果对账单已经存在
        if (reconcileStatement.isDownOrUpload()){
            throw new OperationFailException("对账单文件已经下载或上传");
        }
        // 初始化对商户和应用上下文
        paymentAssistService.initMchAndApp(reconcileStatement.getMchNo(), reconcileStatement.getAppId());

        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = PaymentStrategyFactory.create(reconcileStatement.getChannel(), AbsReconcileStrategy.class);
        reconcileStrategy.setReconcileStatement(reconcileStatement);
        reconcileStrategy.doBeforeHandler();
        try {
            // 下载
            var resolveResultBo = reconcileStrategy.downAndResolve();
            // 解析返回的交易记录, 并是对账单文件进行保存
            SpringUtil.getBean(this.getClass()).resolveAndSave(reconcileStatement, resolveResultBo);
        } catch (Exception e) {
            log.error("解析对账单异常", e);
            reconcileStatement.setErrorMsg("原因: " + e.getMessage());
            // 本方法无事务, 更新信息不会被回滚
            reconcileStatementManager.updateById(reconcileStatement);
            throw new OperationFailException("解析对账单异常");
        }
    }

    /**
     * 手动传输交易对账单
     */
    public void uploadAndSave(ReconcileUploadParam param, MultipartFile file) {
        var statement = reconcileStatementManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        // 将对账订单写入到上下文中
        AbsReconcileStrategy reconcileStrategy = PaymentStrategyFactory.create(statement.getChannel(), AbsReconcileStrategy.class);
        reconcileStrategy.setReconcileStatement(statement);
        reconcileStrategy.doBeforeHandler();

        // 上传类型
        ReconcileFileTypeEnum fileType = ReconcileFileTypeEnum.findByCode(param.getFileType());
        try {
            var resolveResultBo = reconcileStrategy.uploadAndResolve(file, fileType);
            // 解析返回的交易记录, 并是对账单文件进行保存
            SpringUtil.getBean(this.getClass()).resolveAndSave(statement, resolveResultBo);

        } catch (Exception e) {
            log.error("上传对账单异常", e);
            statement.setErrorMsg("原因: " + e.getMessage());
            reconcileStatementManager.updateById(statement);
            throw new RuntimeException(e);
        }
    }


    /**
     * 生成对账文件, 如果有交易不一致, 同时创建并保存对账差异记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void resolveAndSave(ReconcileStatement statement, ReconcileResolveResultBo resolveResultBo) {

        List<ChannelReconcileTradeBo> channelTrades = resolveResultBo.getChannelTrades();
        List<ChannelReconcileTrade> list = ReconcileConvert.CONVERT.toList(channelTrades);
        for (var trade : list) {
            trade.setReconcileId(statement.getId());
        }
        statement.setChannelFileId(resolveResultBo.getChannelFileId())
                .setErrorCode(null)
                .setErrorMsg(null);
        reconcileStatementManager.updateById(statement);
        reconcileTradeManage.saveAll(list);
    }
}
