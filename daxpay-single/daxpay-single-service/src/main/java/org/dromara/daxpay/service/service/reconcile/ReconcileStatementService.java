package org.dromara.daxpay.service.service.reconcile;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.bo.reconcile.*;
import org.dromara.daxpay.service.convert.reconcile.ReconcileConvert;
import org.dromara.daxpay.service.dao.reconcile.ChannelReconcileTradeManage;
import org.dromara.daxpay.service.dao.reconcile.ReconcileDiscrepancyManager;
import org.dromara.daxpay.service.dao.reconcile.ReconcileStatementManager;
import org.dromara.daxpay.service.entity.reconcile.ChannelReconcileTrade;
import org.dromara.daxpay.service.entity.reconcile.ReconcileDiscrepancy;
import org.dromara.daxpay.service.entity.reconcile.ReconcileStatement;
import org.dromara.daxpay.service.enums.ReconcileDiscrepancyTypeEnum;
import org.dromara.daxpay.service.enums.ReconcileFileTypeEnum;
import org.dromara.daxpay.service.enums.ReconcileResultEnum;
import org.dromara.daxpay.service.param.reconcile.ReconcileCreatParam;
import org.dromara.daxpay.service.param.reconcile.ReconcileUploadParam;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.constant.ChannelConstService;
import org.dromara.daxpay.service.strategy.AbsReconcileStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.upload.UploadPretreatment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 对账服务类
 * @author xxm
 * @since 2024/8/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileStatementService {

    private final PaymentAssistService paymentAssistService;
    private final ReconcileStatementManager reconcileStatementManager;
    private final ChannelReconcileTradeManage reconcileTradeManage;
    private final ReconcileDiscrepancyManager discrepancyManager;
    private final ReconcileDiscrepancyService reconcileDiscrepancyService;
    private final ReconcileAssistService reconcileAssistService;
    private final FileStorageService fileStorageService;
    private final ChannelConstService channelConstService;

    /**
     * 创建对账订单
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ReconcileStatement create(ReconcileCreatParam param) {
        // 初始化上下文
        paymentAssistService.initMchApp(param.getAppId());
        ReconcileStatement statement = new ReconcileStatement()
                .setName(param.getTitle())
                .setReconcileNo(TradeNoGenerateUtil.reconciliation())
                .setChannel(param.getChannel())
                .setDate(param.getDate());
        reconcileStatementManager.save(statement);
        return statement;
    }

    /**
     * 下载对账单并进行保存
     */
    public void downAndSave(Long reconcileOrderId) {
        ReconcileStatement statement = reconcileStatementManager.findById(reconcileOrderId)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        this.downAndSave(statement);
    }

    /**
     * 下载对账单并进行保存
     */
    public void downAndSave(ReconcileStatement statement) {
        // 如果对账单已经存在
        if (statement.isDownOrUpload()){
            throw new OperationFailException("对账单文件已经下载或上传");
        }
        // 初始化对商户应用上下文
        paymentAssistService.initMchApp(statement.getAppId());

        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = PaymentStrategyFactory.create(statement.getChannel(), AbsReconcileStrategy.class);
        reconcileStrategy.setStatement(statement);
        reconcileStrategy.doBeforeHandler();
        try {
            // 下载
            var resolveResultBo = reconcileStrategy.downAndResolve();
            // 解析返回的交易记录, 并是对账单文件进行保存
            SpringUtil.getBean(this.getClass()).resolveAndSave(statement, resolveResultBo);
        } catch (Exception e) {
            log.error("解析对账单异常", e);
            statement.setErrorMsg("原因: " + e.getMessage());
            // 本方法无事务, 更新信息不会被回滚
            reconcileStatementManager.updateById(statement);
            throw new OperationFailException("解析对账单异常");
        }
    }


    /**
     * 手动传输交易对账单
     */
    public void uploadAndSave(ReconcileUploadParam param, MultipartFile file) {
        var statement = reconcileStatementManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));

        paymentAssistService.initMchApp(statement.getAppId());

        // 将对账订单写入到上下文中
        AbsReconcileStrategy reconcileStrategy = PaymentStrategyFactory.create(statement.getChannel(), AbsReconcileStrategy.class);
        reconcileStrategy.setStatement(statement);
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
     * 转换为对账交易并保存
     */
    @Transactional(rollbackFor = Exception.class)
    public void resolveAndSave(ReconcileStatement statement, ReconcileResolveResultBo resolveResultBo) {
        List<ChannelReconcileTradeBo> channelTrades = resolveResultBo.getChannelTrades();
        List<ChannelReconcileTrade> list = ReconcileConvert.CONVERT.toList(channelTrades);
        for (var trade : list) {
            trade.setReconcileId(statement.getId());
        }
        statement.setChannelFileUrl(resolveResultBo.getOriginalFileUrl())
                .setDownOrUpload(true)
                .setErrorCode(null)
                .setErrorMsg(null);
        reconcileStatementManager.updateById(statement);
        reconcileTradeManage.saveAll(list);
    }

    /**
     * 数据比对
     */
    @Transactional(rollbackFor = Exception.class)
    public void compare(Long id) {
        var statement = reconcileStatementManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("未找到对账单"));
        this.compare(statement);
    }
    /**
     * 数据比对
     */
    @Transactional(rollbackFor = Exception.class)
    public void compare(ReconcileStatement statement){
        // 判断是否已经下载了对账单明细
        if (!statement.isDownOrUpload()){
            throw new OperationFailException("请先下载对账单");
        }
        // 是否对比完成
        if (statement.isCompare()){
            throw new OperationFailException("对账单比对已经完成");
        }
        paymentAssistService.initMchApp(statement.getAppId());

        // 获取通道交易记录
        var channelTrades = reconcileTradeManage.findAllByReconcileId(statement.getId());
        // 获取平台交易记录
        var platformTrades = reconcileAssistService.getPlatformTrades(statement);
        // 汇总类数据计算
        this.calculationTotal(statement, channelTrades, platformTrades);
        // 进行比对并生成交易差异
        var discrepancies = this.compare(statement, channelTrades, platformTrades);
        // 生成对账单文件并保存
        this.genReconcileFile(statement, discrepancies, platformTrades, channelTrades);
        // 更新记录
        reconcileStatementManager.updateById(statement);
    }

    /**
     * 交易对账比对
     */
    @Transactional(rollbackFor = Exception.class)
    public List<ReconcileDiscrepancy> compare(ReconcileStatement statement, List<ChannelReconcileTrade> channelTrades, List<PlatformReconcileTradeBo> platformTrades){

        // 执行比对任务, 获取对账差异记录并保存
        var discrepancies = reconcileDiscrepancyService.generateDiscrepancy(statement, platformTrades, channelTrades);
        // 判断是否有差异
        if (CollUtil.isNotEmpty(discrepancies)){
            statement.setResult(ReconcileResultEnum.INCONSISTENT.getCode());
        }else {
            statement.setResult(ReconcileResultEnum.CONSISTENT.getCode());
        }
        statement.setCompare(true);
        discrepancyManager.saveAll(discrepancies);
        return discrepancies;
    }

    /**
     * 生成对账文件并保存
     */
    @SneakyThrows
    public void genReconcileFile(ReconcileStatement statement, List<ReconcileDiscrepancy> discrepancies, List<PlatformReconcileTradeBo> platformTrades, List<ChannelReconcileTrade> channelTrades){
        // 生成对账文件
        var params = new TemplateExportParams(ResourceUtil.getStream("template/对账单模板.xlsx"));
        params.setScanAllsheet(true);
        Map<String, Object> map = new HashMap<>();
        // 汇总
        map.put("total", this.convertTotal(statement));
        // 明细
        map.put("trades", this.convertTrades(discrepancies, platformTrades, channelTrades));
        // 生成对账单文件
        Workbook workbook = ExcelExportUtil.exportExcel(params,map);
        if (Objects.isNull(workbook)){
            throw new OperationFailException("生成对账单文件异常, 模板文件可能为空! ");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        UploadPretreatment uploadPretreatment = fileStorageService.of(baos.toByteArray(), "对账文件.xlsx");
        uploadPretreatment.setPath(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy/MM/dd/"));
        uploadPretreatment.setOriginalFilename(statement.getName()+".xlsx");
        FileInfo upload = uploadPretreatment.upload();
        statement.setPlatformFileUrl(upload.getUrl());
    }

    /**
     * 计算汇总数据
     */
    public void calculationTotal(ReconcileStatement statement, List<ChannelReconcileTrade> channelTrades, List<PlatformReconcileTradeBo> platformTrades){
        // 平台支付
        BigDecimal tradeAmount = platformTrades.stream()
                .filter(o-> Objects.equals(o.getTradeType(), TradeTypeEnum.PAY.getCode()))
                .map(PlatformReconcileTradeBo::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long tradeCount = platformTrades.stream()
                .filter(o -> Objects.equals(o.getTradeType(), TradeTypeEnum.PAY.getCode()))
                .count();
        statement.setOrderCount(Math.toIntExact(tradeCount))
                .setOrderAmount(tradeAmount);
        // 平台退款
        BigDecimal refundAmount = platformTrades.stream()
                .filter(o-> Objects.equals(o.getTradeType(), TradeTypeEnum.REFUND.getCode()))
                .map(PlatformReconcileTradeBo::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long refundCount = platformTrades.stream()
                .filter(o -> Objects.equals(o.getTradeType(), TradeTypeEnum.REFUND.getCode()))
                .count();
        statement.setRefundCount(Math.toIntExact(refundCount))
                .setRefundAmount(refundAmount);
        // 通道支付
        BigDecimal channelTradeAmount = channelTrades.stream()
                .filter(o-> Objects.equals(o.getTradeType(), TradeTypeEnum.PAY.getCode()))
                .map(ChannelReconcileTrade::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long channelTradeCount = channelTrades.stream()
                .filter(o -> Objects.equals(o.getTradeType(), TradeTypeEnum.PAY.getCode()))
                .count();
        statement.setChannelOrderCount(Math.toIntExact(channelTradeCount))
                .setChannelOrderAmount(channelTradeAmount);

        // 通道退款
        BigDecimal channelRefundAmount = channelTrades.stream()
                .filter(o-> Objects.equals(o.getTradeType(), TradeTypeEnum.REFUND.getCode()))
                .map(ChannelReconcileTrade::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long channelRefundCount = channelTrades.stream()
                .filter(o -> Objects.equals(o.getTradeType(), TradeTypeEnum.REFUND.getCode()))
                .count();
        statement.setChannelRefundCount(Math.toIntExact(channelRefundCount))
                .setChannelRefundAmount(channelRefundAmount);
    }


    /**
     * 转换对账单概览
     */
    public ReconcileTotalExcel convertTotal(ReconcileStatement statement){
        // 汇总 通道
        String channelName = channelConstService.findNameByCode(statement.getChannel());
        return new ReconcileTotalExcel()
                .setReconcileDate(LocalDateTimeUtil.format(statement.getDate(), DatePattern.CHINESE_DATE_PATTERN))
                .setCreateTime(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.CHINESE_DATE_TIME_PATTERN))
                .setChannel(channelName)
                .setResult(ReconcileResultEnum.findByCode(statement.getResult()).getName())
                .setTradeAmount(statement.getOrderAmount().toString())
                .setTradeCount(statement.getOrderCount())
                .setRefundAmount(statement.getRefundAmount().toString())
                .setRefundCount(statement.getRefundCount())
                .setChannelTradeAmount(statement.getChannelOrderAmount().toString())
                .setChannelTradeCount(statement.getChannelOrderCount())
                .setChannelRefundAmount(statement.getChannelRefundAmount().toString())
                .setChannelRefundCount(statement.getChannelRefundCount());
    }

    /**
     * 转换对账单明细表格导出对象
     */
    public List<ReconcileTradeExcel> convertTrades(List<ReconcileDiscrepancy> discrepancies, List<PlatformReconcileTradeBo> platformTrades, List<ChannelReconcileTrade> channelTrades) {
        // 平台异常交易
        var platformDiscrepancyMap = discrepancies.stream()
                .collect(Collectors.toMap(ReconcileDiscrepancy::getTradeNo, Function.identity(), (o1, o2) -> o1));
        // 通道正常交易
        var channelTradeMap = channelTrades.stream()
                .collect(Collectors.toMap(ChannelReconcileTrade::getOutTradeNo, Function.identity(), (o1, o2) -> o1));

        List<ReconcileTradeExcel> tradeExcels = new ArrayList<>();

        // 先执行平台的正常订单
        for (PlatformReconcileTradeBo platformTrade : platformTrades) {
            if (Objects.nonNull(platformDiscrepancyMap.get(platformTrade.getTradeNo()))){
                continue;
            }
            var channelTrade = channelTradeMap.get(platformTrade.getTradeNo());
            tradeExcels.add(new ReconcileTradeExcel()
                    .setResult("一致")
                    .setTradeNo(platformTrade.getTradeNo())
                    .setBizTradeNo(platformTrade.getBizTradeNo())
                    .setOutTradeNo(platformTrade.getOutTradeNo())
                    .setTradeType(TradeTypeEnum.findByCode(platformTrade.getTradeType()).getName())
                    .setTradeAmount(platformTrade.getAmount().toString())
                    .setTradeStatus(platformTrade.getTradeStatus())
                    .setTradeTime(LocalDateTimeUtil.format(platformTrade.getTradeTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
                    .setChannelTradeNo(channelTrade.getTradeNo())
                    .setChannelTradeType(TradeTypeEnum.findByCode(channelTrade.getTradeType()).getName())
                    .setChannelTradeAmount(channelTrade.getAmount().toString())
                    .setChannelTradeStatus(channelTrade.getTradeStatus())
                    .setChannelTradeTime(LocalDateTimeUtil.format(channelTrade.getTradeTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
            );
        }
        // 处理异常订单,
        for (ReconcileDiscrepancy discrepancy : discrepancies) {
            var discrepancyTrade = platformDiscrepancyMap.get(discrepancy.getTradeNo());
            switch (ReconcileDiscrepancyTypeEnum.findByCode(discrepancy.getDiscrepancyType())) {
                // 处理本地短单
                case LOCAL_NOT_EXISTS -> tradeExcels.add(new ReconcileTradeExcel()
                        .setResult(ReconcileDiscrepancyTypeEnum.LOCAL_NOT_EXISTS.getName())
                        .setTradeNo(discrepancy.getTradeNo())
                        .setChannelTradeNo(discrepancy.getChannelTradeNo())
                        .setChannelTradeType(TradeTypeEnum.findByCode(discrepancy.getChannelTradeType()).getName())
                        .setChannelTradeStatus(discrepancy.getChannelTradeStatus())
                        .setChannelTradeAmount(discrepancy.getChannelTradeAmount().toString())
                        .setChannelTradeTime(LocalDateTimeUtil.format(discrepancyTrade.getChannelTradeTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
                );
                // 处理远程短单
                case REMOTE_NOT_EXISTS -> tradeExcels.add(new ReconcileTradeExcel()
                        .setResult(ReconcileDiscrepancyTypeEnum.REMOTE_NOT_EXISTS.getName())
                        .setTradeNo(discrepancyTrade.getTradeNo())
                        .setBizTradeNo(discrepancyTrade.getBizTradeNo())
                        .setOutTradeNo(discrepancyTrade.getOutTradeNo())
                        .setTradeType(TradeTypeEnum.findByCode(discrepancyTrade.getTradeType()).getName())
                        .setTradeAmount(discrepancyTrade.getTradeAmount().toString())
                        .setTradeStatus(discrepancyTrade.getTradeStatus())
                        .setTradeTime(LocalDateTimeUtil.format(discrepancyTrade.getTradeTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
                );
                // 处理信息不一致订单
                case NOT_MATCH -> tradeExcels.add(new ReconcileTradeExcel()
                        .setResult(ReconcileDiscrepancyTypeEnum.NOT_MATCH.getName())
                        .setTradeNo(discrepancyTrade.getTradeNo())
                        .setBizTradeNo(discrepancyTrade.getBizTradeNo())
                        .setOutTradeNo(discrepancyTrade.getOutTradeNo())
                        .setTradeType(TradeTypeEnum.findByCode(discrepancyTrade.getTradeType()).getName())
                        .setTradeAmount(discrepancyTrade.getTradeAmount().toString())
                        .setTradeStatus(discrepancyTrade.getTradeStatus())
                        .setTradeTime(LocalDateTimeUtil.format(discrepancyTrade.getTradeTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
                        .setChannelTradeNo(discrepancy.getChannelTradeNo())
                        .setChannelTradeAmount(discrepancy.getChannelTradeAmount().toString())
                        .setChannelTradeStatus(discrepancy.getChannelTradeStatus())
                        .setChannelTradeTime(LocalDateTimeUtil.format(discrepancy.getChannelTradeTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
                );
            }
        }
        return tradeExcels;
    }
}
