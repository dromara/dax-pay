package cn.daxpay.single.service.core.payment.reconcile.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.exception.OperationFailException;
import cn.daxpay.single.core.util.TradeNoGenerateUtil;
import cn.daxpay.single.service.code.ReconcileFileTypeEnum;
import cn.daxpay.single.service.code.ReconcileResultEnum;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.reconcile.dao.ReconcileDiffManager;
import cn.daxpay.single.service.core.order.reconcile.dao.ReconcileFileManager;
import cn.daxpay.single.service.core.order.reconcile.dao.ReconcileOrderManager;
import cn.daxpay.single.service.core.order.reconcile.dao.ReconcileOutTradeManager;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileDiff;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileFile;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileOrder;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileOutTrade;
import cn.daxpay.single.service.core.order.reconcile.service.ReconcileOrderService;
import cn.daxpay.single.service.core.payment.reconcile.domain.GeneralTradeInfo;
import cn.daxpay.single.service.dto.order.reconcile.ReconcileDiffExcel;
import cn.daxpay.single.service.dto.order.reconcile.ReconcileTradeDetailExcel;
import cn.daxpay.single.service.func.AbsReconcileStrategy;
import cn.daxpay.single.service.param.reconcile.ReconcileUploadParam;
import cn.daxpay.single.service.util.PayStrategyFactory;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
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

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    private final ReconcileDiffManager reconcileDiffManager;

    private final ReconcileOrderManager reconcileOrderManager;

    private final ReconcileOutTradeManager reconcileOutTradeManager;

    private final ReconcileAssistService reconcileAssistService;

    private final ReconcileFileManager reconcileFileManager;

    private final FileStorageService fileStorageService;

    /**
     * 创建对账订单
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ReconcileOrder create(LocalDate date, String channel) {
        ReconcileOrder order = new ReconcileOrder()
                .setReconcileNo(TradeNoGenerateUtil.reconciliation())
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
            throw new OperationFailException("对账单文件已经下载或上传");
        }
        // 将对账订单写入到上下文中
        PaymentContextLocal.get().getReconcileInfo().setReconcileOrder(reconcileOrder);
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = PayStrategyFactory.create(reconcileOrder.getChannel(), AbsReconcileStrategy.class);
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
        List<ReconcileOutTrade> reconcileTradeDetails = PaymentContextLocal.get()
                .getReconcileInfo()
                .getReconcileTradeDetails();
        reconcileOutTradeManager.saveAll(reconcileTradeDetails);
    }

    /**
     * 手动传输交易对账单
     */
    public void upload(ReconcileUploadParam param, MultipartFile file) {
        ReconcileOrder reconcileOrder = reconcileOrderService.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        // 将对账订单写入到上下文中
        PaymentContextLocal.get().getReconcileInfo().setReconcileOrder(reconcileOrder);
        AbsReconcileStrategy reconcileStrategy = PayStrategyFactory.create(reconcileOrder.getChannel(), AbsReconcileStrategy.class);
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
        List<ReconcileOutTrade> reconcileTradeDetails = PaymentContextLocal.get()
                .getReconcileInfo()
                .getReconcileTradeDetails();
        reconcileOutTradeManager.saveAll(reconcileTradeDetails);
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
            throw new OperationFailException("请先下载对账单");
        }
        // 是否对比完成
        if (reconcileOrder.isCompare()){
            throw new OperationFailException("对账单比对已经完成");
        }
        // 查询对账单
        List<ReconcileOutTrade> reconcileTradeDetails = reconcileOutTradeManager.findAllByReconcileId(reconcileOrder.getId());
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy =PayStrategyFactory.create(reconcileOrder.getChannel(), AbsReconcileStrategy.class);
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
            reconcileDiffManager.saveAll(diffRecords);
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
    @SneakyThrows
    public ResponseEntity<byte[]> downOriginal2Csv(Long id){
        ReconcileOrder reconcileOrder = reconcileOrderService.findById(id)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        // 查询对账-第三方交易明细
        List<ReconcileTradeDetailExcel> reconcileTradeDetails = reconcileOutTradeManager.findAllByReconcileId(reconcileOrder.getId())
                .stream()
                .map(o->{
                    ReconcileTradeDetailExcel excel = new ReconcileTradeDetailExcel();
                    BeanUtil.copyProperties(o,excel);
                    return excel;
                }).collect(Collectors.toList());

        // 转换为csv文件
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        EasyExcel.write(byteArrayOutputStream, ReconcileTradeDetailExcel.class)
                .excelType(ExcelTypeEnum.CSV)
                .sheet("对账单明细")
                .doWrite(() -> reconcileTradeDetails);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        // 设置header信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // TODO 后续使用数据库中数据
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(reconcileOrder.getChannel());
        String date = LocalDateTimeUtil.format(reconcileOrder.getDate(), DatePattern.PURE_DATE_PATTERN);
        // 将原始文件进行保存 通道-日期
        String fileName = StrUtil.format("外部对账单-{}-{}.csv", channelEnum.getName(),date);
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, CharsetUtil.UTF_8));
        return new ResponseEntity<>(bytes,headers, HttpStatus.OK);
    }

    /**
     * 下载对账单(本系统中的订单)
     */
    @SneakyThrows
    public ResponseEntity<byte[]> downLocalCsv(Long id){
        ReconcileOrder reconcileOrder = reconcileOrderService.findById(id)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        List<GeneralTradeInfo> generalTradeInfoList = reconcileAssistService.getGeneralTradeInfoList(reconcileOrder);

        // 转换为csv文件
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        EasyExcel.write(byteArrayOutputStream, GeneralTradeInfo.class)
                .excelType(ExcelTypeEnum.CSV)
                .sheet("对账单明细")
                .doWrite(() -> generalTradeInfoList);
        byte[] bytes = byteArrayOutputStream.toByteArray();// 设置header信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // TODO 后续使用数据库中数据
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(reconcileOrder.getChannel());
        String date = LocalDateTimeUtil.format(reconcileOrder.getDate(), DatePattern.PURE_DATE_PATTERN);
        // 将原始文件进行保存 通道-日期
        String fileName = StrUtil.format("系统对账单-{}-{}.csv", channelEnum.getName(),date);
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, CharsetUtil.UTF_8));
        return new ResponseEntity<>(bytes,headers, HttpStatus.OK);
    }

    /**
     * 下载对账差异单
     */
    @SneakyThrows
    public ResponseEntity<byte[]> downDiffCsv(Long id){
        ReconcileOrder reconcileOrder = reconcileOrderService.findById(id)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        List<ReconcileDiffExcel> reconcileDiffs = reconcileDiffManager.findAllByReconcileId(reconcileOrder.getId()).stream()
                .map(o->{
                    ReconcileDiffExcel excel = new ReconcileDiffExcel();
                    BeanUtil.copyProperties(o,excel);
                    return excel;
                }).collect(Collectors.toList());

        // 转换为csv文件
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        EasyExcel.write(byteArrayOutputStream, ReconcileDiffExcel.class)
                .sheet("对账单明细")
                .doWrite(() -> reconcileDiffs);
        byte[] bytes = byteArrayOutputStream.toByteArray();// 设置header信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // TODO 后续使用数据库中数据
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(reconcileOrder.getChannel());
        String date = LocalDateTimeUtil.format(reconcileOrder.getDate(), DatePattern.PURE_DATE_PATTERN);
        // 将原始文件进行保存 通道-日期
        String fileName = StrUtil.format("对账差异单-{}-{}.csv", channelEnum.getName(),date);
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, CharsetUtil.UTF_8));
        return new ResponseEntity<>(bytes,headers, HttpStatus.OK);
    }

}
