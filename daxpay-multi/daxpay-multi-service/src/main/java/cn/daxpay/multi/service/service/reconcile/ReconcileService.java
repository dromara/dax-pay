package cn.daxpay.multi.service.service.reconcile;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.daxpay.multi.core.util.TradeNoGenerateUtil;
import cn.daxpay.multi.service.dao.reconcile.ReconcileStatementManager;
import cn.daxpay.multi.service.entity.reconcile.ReconcileStatement;
import cn.daxpay.multi.service.param.reconcile.ReconcileCreatParam;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 创建对账订单
     *
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
//        this.downAndSave(reconcileOrder);
    }

//    /**
//     * 下载交易对账单并进行保存
//     */
//    public void downAndSave(ReconcileStatement reconcileStatement) {
//        // 如果对账单已经存在
//        if (reconcileStatement.isDownOrUpload()){
//            throw new OperationFailException("对账单文件已经下载或上传");
//        }
//        // 将对账订单写入到上下文中
//        PaymentContextLocal.get().getReconcileInfo().setReconcileOrder(reconcileStatement);
//        // 构建对账策略
//        AbsReconcileStrategy reconcileStrategy = PayStrategyFactory.create(reconcileStatement.getChannel(), AbsReconcileStrategy.class);
//        reconcileStrategy.setRecordOrder(reconcileStatement);
//        reconcileStrategy.doBeforeHandler();
//        try {
//            reconcileStrategy.downAndSave();
//            reconcileStatement.setDownOrUpload(true)
//                    .setErrorMsg(null);
//            reconcileOrderService.update(reconcileStatement);
//        } catch (Exception e) {
//            log.error("下载对账单异常", e);
//            reconcileStatement.setErrorMsg("原因: " + e.getMessage());
//            // 本方法无事务, 更新信息不会被回滚
//            reconcileOrderService.update(reconcileStatement);
//            throw new RuntimeException(e);
//        }
//        // 保存转换后的通用结构对账单
//        List<ReconcileOutTrade> reconcileTradeDetails = PaymentContextLocal.get()
//                .getReconcileInfo()
//                .getReconcileTradeDetails();
//        reconcileOutTradeManager.saveAll(reconcileTradeDetails);
//    }
//
//    /**
//     * 手动传输交易对账单
//     */
//    public void upload(ReconcileUploadParam param, MultipartFile file) {
//        ReconcileOrder reconcileOrder = reconcileOrderService.findById(param.getId())
//                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
//        // 将对账订单写入到上下文中
//        PaymentContextLocal.get().getReconcileInfo().setReconcileOrder(reconcileOrder);
//        AbsReconcileStrategy reconcileStrategy = PayStrategyFactory.create(reconcileOrder.getChannel(), AbsReconcileStrategy.class);
//        reconcileStrategy.setRecordOrder(reconcileOrder);
//        reconcileStrategy.doBeforeHandler();
//
//        // 上传类型
//        ReconcileFileTypeEnum fileType = ReconcileFileTypeEnum.findByCode(param.getFileType());
//        try {
//            reconcileStrategy.upload(file, fileType);
//            reconcileOrder.setErrorMsg(null);
//            reconcileOrderService.update(reconcileOrder);
//        } catch (Exception e) {
//            log.error("上传对账单异常", e);
//            reconcileOrder.setErrorMsg("原因: " + e.getMessage());
//            reconcileOrderService.update(reconcileOrder);
//            throw new RuntimeException(e);
//        }
//        // 保存转换后的通用结构对账单
//        List<ReconcileOutTrade> reconcileTradeDetails = PaymentContextLocal.get()
//                .getReconcileInfo()
//                .getReconcileTradeDetails();
//        reconcileOutTradeManager.saveAll(reconcileTradeDetails);
//    }
}
