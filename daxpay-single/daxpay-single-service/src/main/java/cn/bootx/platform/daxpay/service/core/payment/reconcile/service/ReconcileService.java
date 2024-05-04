package cn.bootx.platform.daxpay.service.core.payment.reconcile.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.ReconcileFileTypeEnum;
import cn.bootx.platform.daxpay.service.code.ReconcileResultEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.ReconcileFileManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.ReconcileTradeDetailManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.ReconcileOrderManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileFile;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileTradeDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDiff;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.ReconcileDiffService;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.ReconcileOrderService;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.GeneralTradeInfo;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.factory.ReconcileStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import cn.bootx.platform.daxpay.service.param.reconcile.ReconcileUploadParam;
import cn.bootx.platform.daxpay.util.OrderNoGenerateUtil;
import cn.hutool.core.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

/**
 * 对账服务,
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileService {
    private final ReconcileOrderService reconcileOrderService;

    private final ReconcileDiffService reconcileDiffService;

    private final ReconcileOrderManager reconcileOrderManager;

    private final ReconcileTradeDetailManager reconcileTradeDetailManager;

    private final ReconcileAssistService reconcileAssistService;

    private final ReconcileFileManager reconcileFileManager;

    private final FileStorageService fileStorageService;

    /**
     * 创建对账订单
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ReconcileOrder create(LocalDate date, String channel) {
        ReconcileOrder order = new ReconcileOrder()
                .setReconcileNo(OrderNoGenerateUtil.reconciliation())
                .setChannel(channel)
                .setDate(date);
        reconcileOrderManager.save(order);
        return order;
    }

    /**
     * 下载对账单并进行保存
     */
    public void downAndSave(Long reconcileOrderId) {
        ReconcileOrder reconcileOrder = reconcileOrderService.findById(reconcileOrderId)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        this.downAndSave(reconcileOrder);
    }

    /**
     * 下载交易对账单并进行保存
     */
    public void downAndSave(ReconcileOrder reconcileOrder) {
        // 如果对账单已经存在
        if (reconcileOrder.isDownOrUpload()){
            throw new PayFailureException("对账单文件已经下载或上传");
        }
        // 将对账订单写入到上下文中
        PaymentContextLocal.get().getReconcileInfo().setReconcileOrder(reconcileOrder);
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = ReconcileStrategyFactory.create(reconcileOrder.getChannel());
        reconcileStrategy.setRecordOrder(reconcileOrder);
        reconcileStrategy.doBeforeHandler();
        try {
            reconcileStrategy.downAndSave();
            reconcileOrder.setDownOrUpload(true)
                    .setErrorMsg(null);
            reconcileOrderService.update(reconcileOrder);
        } catch (Exception e) {
            log.error("下载对账单异常", e);
            reconcileOrder.setErrorMsg("原因: " + e.getMessage());
            // 本方法无事务, 更新信息不会被回滚
            reconcileOrderService.update(reconcileOrder);
            throw new RuntimeException(e);
        }
        // 保存转换后的通用结构对账单
        List<ReconcileTradeDetail> reconcileTradeDetails = PaymentContextLocal.get()
                .getReconcileInfo()
                .getReconcileTradeDetails();
        reconcileTradeDetailManager.saveAll(reconcileTradeDetails);
    }

    /**
     * 手动传输交易对账单
     */
    public void upload(ReconcileUploadParam param, MultipartFile file) {
        ReconcileOrder reconcileOrder = reconcileOrderService.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        // 将对账订单写入到上下文中
        PaymentContextLocal.get().getReconcileInfo().setReconcileOrder(reconcileOrder);
        AbsReconcileStrategy reconcileStrategy = ReconcileStrategyFactory.create(reconcileOrder.getChannel());
        reconcileStrategy.setRecordOrder(reconcileOrder);
        reconcileStrategy.doBeforeHandler();

        // 上传类型
        ReconcileFileTypeEnum fileType = ReconcileFileTypeEnum.findByCode(param.getFileType());
        try {
            reconcileStrategy.upload(file, fileType);
            reconcileOrder.setErrorMsg(null);
            reconcileOrderService.update(reconcileOrder);
        } catch (Exception e) {
            log.error("上传对账单异常", e);
            reconcileOrder.setErrorMsg("原因: " + e.getMessage());
            reconcileOrderService.update(reconcileOrder);
            throw new RuntimeException(e);
        }
        // 保存转换后的通用结构对账单
        List<ReconcileTradeDetail> reconcileTradeDetails = PaymentContextLocal.get()
                .getReconcileInfo()
                .getReconcileTradeDetails();
        reconcileTradeDetailManager.saveAll(reconcileTradeDetails);
    }
    /**
     * 对账单明细比对
     */
    @Transactional(rollbackFor = Exception.class)
    public void compare(Long reconcileOrderId){
        ReconcileOrder reconcileOrder = reconcileOrderService.findById(reconcileOrderId)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        this.compare(reconcileOrder);
    }

    /**
     * 交易对账比对
     */
    public void compare(ReconcileOrder reconcileOrder){
        // 判断是否已经下载了对账单明细
        if (!reconcileOrder.isDownOrUpload()){
            throw new PayFailureException("请先下载对账单");
        }
        // 是否对比完成
        if (reconcileOrder.isCompare()){
            throw new PayFailureException("对账单比对已经完成");
        }
        // 查询对账单
        List<ReconcileTradeDetail> reconcileTradeDetails = reconcileTradeDetailManager.findAllByReconcileId(reconcileOrder.getId());
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = ReconcileStrategyFactory.create(reconcileOrder.getChannel());
        // 初始化参数
        reconcileStrategy.setRecordOrder(reconcileOrder);

        try {
            // 执行比对任务, 获取对账差异记录并保存
            List<GeneralTradeInfo> generalTradeInfo = reconcileAssistService.getGeneralTradeInfoList(reconcileOrder);
            List<ReconcileDiff> diffRecords = reconcileAssistService.generateDiffRecord(reconcileOrder, generalTradeInfo, reconcileTradeDetails);
            // 判断是否有差异
            if (CollUtil.isNotEmpty(diffRecords)){
                reconcileOrder.setResult(ReconcileResultEnum.INCONSISTENT.getCode());
            }else {
                reconcileOrder.setResult(ReconcileResultEnum.CONSISTENT.getCode());
            }
            reconcileOrder.setCompare(true);
            reconcileOrderService.update(reconcileOrder);
            reconcileDiffService.saveAll(diffRecords);
        } catch (Exception e) {
            log.error("比对对账单异常", e);
            throw e;
        }
    }

    /**
     * 下载原始对交易对账单文件
     */
    @SneakyThrows
    public ResponseEntity<byte[]> downOriginal(Long id){
        if (!reconcileOrderManager.existedById(id)) {
            throw new DataNotExistException("未找到对账订单");
        }
        // 获取对账单文件ID
        List<ReconcileFile> reconcileFiles = reconcileFileManager.findAllByReconcileId(id);
        if (CollUtil.isEmpty(reconcileFiles)){
            throw new DataNotExistException("未找到对账文件");
        }

        // 查询文件
        FileInfo fileInfo = fileStorageService.getFileInfoByUrl(String.valueOf(reconcileFiles.get(0).getFileId()));
        byte[] bytes = fileStorageService.download(fileInfo).bytes();
        // 设置header信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String fileName = fileInfo.getOriginalFilename();
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, CharsetUtil.UTF_8));
        return new ResponseEntity<>(bytes,headers, HttpStatus.OK);
    }

    /**
     * 下载基于原始交易对账单数据转换的对账单文件, 例如csv, json方式
     */
    public void downOriginal2Csv(Long id){
        ReconcileOrder reconcileOrder = reconcileOrderService.findById(id)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        // 查询对账-第三方交易明细
        List<ReconcileTradeDetail> reconcileTradeDetails = reconcileTradeDetailManager.findAllByReconcileId(reconcileOrder.getId());

        // 转换为csv文件
    }

    /**
     * 下载对账单(本系统中的订单)
     */
    public void downLocal(Long id){
    }

}
