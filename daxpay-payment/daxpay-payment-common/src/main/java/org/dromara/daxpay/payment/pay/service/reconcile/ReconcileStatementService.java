package org.dromara.daxpay.payment.pay.service.reconcile;

import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.common.mybatisplus.function.CollectorsFunction;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeTypeEnum;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.payment.common.util.PayUtil;
import org.dromara.daxpay.platform.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.payment.pay.bo.reconcile.*;
import org.dromara.daxpay.payment.pay.convert.reconcile.ReconcileConvert;
import org.dromara.daxpay.payment.pay.dao.reconcile.ChannelReconcileTradeManage;
import org.dromara.daxpay.payment.pay.dao.reconcile.ReconcileDiscrepancyManager;
import org.dromara.daxpay.payment.pay.dao.reconcile.ReconcileStatementManager;
import org.dromara.daxpay.payment.pay.entity.reconcile.ChannelReconcileTrade;
import org.dromara.daxpay.payment.pay.entity.reconcile.ReconcileDiscrepancy;
import org.dromara.daxpay.payment.pay.entity.reconcile.ReconcileStatement;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.platform.core.enums.pay.reconcile.ReconcileDiscrepancyTypeEnum;
import org.dromara.daxpay.platform.core.enums.pay.reconcile.ReconcileFileTypeEnum;
import org.dromara.daxpay.platform.core.enums.pay.reconcile.ReconcileResultEnum;
import org.dromara.daxpay.payment.pay.param.reconcile.ReconcileCreatParam;
import org.dromara.daxpay.payment.pay.param.reconcile.ReconcileUploadParam;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.pay.service.masterdata.channel.PayChannelService;
import org.dromara.daxpay.payment.pay.strategy.AbsReconcileStrategy;
import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.platform.capability.file.service.PlatformFileService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.dromara.daxpay.platform.core.code.CommonCode;

/// # 对账服务类
///
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
    private final PlatformFileService platformFileService;
    private final PayChannelService payChannelService;

    /// 创建对账订单
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ReconcileStatement create(ReconcileCreatParam param) {
        // 初始化上下文
        paymentAssistService.initMchAndApp(param.getMchNo(), param.getAppId());
        ReconcileStatement statement = new ReconcileStatement()
                .setName(param.getTitle())
                .setReconcileNo(TradeNoGenerateUtil.reconciliation())
                .setChannel(param.getChannel())
                .setProduct(param.getProduct())
                .setDate(param.getDate());
        // 从产品编码派生通道编码
        if (StrUtil.isNotBlank(statement.getProduct()) && StrUtil.isBlank(statement.getChannel())) {
            ProductEnum productEnum = ProductEnum.findByCode(statement.getProduct());
            if (productEnum != null) {
                statement.setChannel(productEnum.getChannel());
            }
        }
        reconcileStatementManager.save(statement);
        return statement;
    }

    /// 下载对账单并进行保存
    public void downAndSave(Long reconcileOrderId) {
        ReconcileStatement statement = reconcileStatementManager.findById(reconcileOrderId)
                .orElseThrow(() -> new DataNotExistException("error.payment.reconcile.orderNotFound"));
        this.downAndSave(statement);
    }

    /// 下载对账单并进行保存
    public void downAndSave(ReconcileStatement statement) {
        // 如果对账单已经存在
        if (statement.isDownOrUpload()){
            // 对账单文件已经下载或上传
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.order.reconcileFileAlreadyDownloaded");
        }
        // 初始化对商户和应用上下文
        paymentAssistService.initMchAndApp(statement.getMchNo(), statement.getAppId());

        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = PaymentStrategyFactory.createByProduct(statement.getProduct(), AbsReconcileStrategy.class);
        reconcileStrategy.setStatement(statement);
        reconcileStrategy.doBeforeHandler();
        try {
            // 下载
            var resolveResultBo = reconcileStrategy.downAndResolve();
            // 解析返回的交易记录, 并是对账单文件进行保存
            SpringUtil.getBean(this.getClass()).resolveAndSave(statement, resolveResultBo);
        } catch (Exception e) {
            log.error("解析对账单异常", e);
            statement.setErrorMsg("原因: " + StrUtil.sub(e.getMessage(),0,450));
            // 本方法无事务, 更新信息不会被回滚
            reconcileStatementManager.updateById(statement);
            // 解析对账单异常
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.order.reconcileParseError");
        }
    }

    /// 手动传输交易对账单
    public void uploadAndSave(ReconcileUploadParam param, MultipartFile file) {
        var statement = reconcileStatementManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("error.payment.reconcile.orderNotFound"));

        paymentAssistService.initMchAndApp(statement.getMchNo(), statement.getAppId());

        // 将对账订单写入到上下文中
        AbsReconcileStrategy reconcileStrategy = PaymentStrategyFactory.createByProduct(statement.getProduct(), AbsReconcileStrategy.class);
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

    /// 转换为对账交易并保存
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

    /// 数据比对
    @Transactional(rollbackFor = Exception.class)
    public void compare(Long id) {
        var statement = reconcileStatementManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.payment.reconcile.statementNotFound"));
        this.compare(statement);
    }
    /// 数据比对
    @Transactional(rollbackFor = Exception.class)
    public void compare(ReconcileStatement statement){
        // 判断是否已经下载了对账单明细
        if (!statement.isDownOrUpload()){
            // 请先下载对账单
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.order.reconcileDownloadFirst");
        }
        // 是否对比完成
        if (statement.isCompare()){
            // 对账单比对已经完成
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.order.reconcileAlreadyCompleted");
        }
        paymentAssistService.initMchAndApp(statement.getMchNo(), statement.getAppId());

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

    /// 交易对账比对
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

    /// 生成对账文件并保存
    /// TODO: 需要使用 EasyExcel 或其他 Excel 库重新实现，EasyPOI 已移除
    public void genReconcileFile(ReconcileStatement statement, List<ReconcileDiscrepancy> discrepancies, List<PlatformReconcileTradeBo> platformTrades, List<ChannelReconcileTrade> channelTrades){
        log.warn("对账单文件导出功能暂未实现，请使用 EasyExcel 或其他 Excel 库重新实现");
        statement.setPlatformFileUrl(null);
    }

    /// 计算汇总数据
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

    /// 转换对账单概览
    public ReconcileTotalExcel convertTotal(ReconcileStatement statement){
        // 汇总 通道
        String channelName = payChannelService.findNameByCode(statement.getChannel());
        return new ReconcileTotalExcel()
                .setReconcileDate(LocalDateTimeUtil.format(statement.getDate(), DatePattern.CHINESE_DATE_PATTERN))
                .setCreateTime(LocalDateTimeUtil.format(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
                .setChannel(channelName)
                .setResult(I18nUtil.getEnumName(ReconcileResultEnum.findByCode(statement.getResult())))
                .setTradeAmount(PayUtil.toDecimal(statement.getOrderAmount()).toString())
                .setTradeCount(statement.getOrderCount())
                .setRefundAmount(PayUtil.toDecimal(statement.getRefundAmount()).toString())
                .setRefundCount(statement.getRefundCount())
                .setChannelTradeAmount(PayUtil.toDecimal(statement.getChannelOrderAmount()).toString())
                .setChannelTradeCount(statement.getChannelOrderCount())
                .setChannelRefundAmount(PayUtil.toDecimal(statement.getChannelRefundAmount()).toString())
                .setChannelRefundCount(statement.getChannelRefundCount());
    }

    /// 转换对账单明细表格导出对象
    public List<ReconcileTradeExcel> convertTrades(List<ReconcileDiscrepancy> discrepancies, List<PlatformReconcileTradeBo> platformTrades, List<ChannelReconcileTrade> channelTrades) {
        // 平台异常交易
        var platformDiscrepancyMap = discrepancies.stream()
                .collect(Collectors.toMap(ReconcileDiscrepancy::getOutTradeNo, Function.identity(), CollectorsFunction::retainFirst));
        // 通道正常交易
        var channelTradeMap = channelTrades.stream()
                .collect(Collectors.toMap(ChannelReconcileTrade::getChannelTradeNo, Function.identity(), CollectorsFunction::retainFirst));

        List<ReconcileTradeExcel> tradeExcels = new ArrayList<>();

        // 先执行平台的正常订单
        for (PlatformReconcileTradeBo platformTrade : platformTrades) {
            if (Objects.nonNull(platformDiscrepancyMap.get(platformTrade.getOutTradeNo()))){
                continue;
            }
            var channelTrade = channelTradeMap.get(platformTrade.getOutTradeNo());
            tradeExcels.add(new ReconcileTradeExcel()
                    .setResult(I18nUtil.getEnumName(ReconcileDiscrepancyTypeEnum.CONSISTENT))
                    .setTradeNo(platformTrade.getTradeNo())
                    .setBizTradeNo(platformTrade.getBizTradeNo())
                    .setOutTradeNo(platformTrade.getOutTradeNo())
                    .setTradeType(I18nUtil.getEnumName(TradeTypeEnum.findByCode(platformTrade.getTradeType())))
                    .setTradeAmount(PayUtil.toDecimal(platformTrade.getAmount()).toPlainString())
                    .setTradeStatus(platformTrade.getTradeStatus())
                    .setTradeTime(LocalDateTimeUtil.format(platformTrade.getTradeTime().toLocalDateTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
                    .setChannelTradeNo(channelTrade.getChannelTradeNo())
                    .setChannelTradeType(I18nUtil.getEnumName(TradeTypeEnum.findByCode(channelTrade.getTradeType())))
                    .setChannelTradeAmount(PayUtil.toDecimal(channelTrade.getAmount()).toPlainString())
                    .setChannelTradeStatus(channelTrade.getTradeStatus())
                    .setChannelTradeTime(LocalDateTimeUtil.format(channelTrade.getTradeTime().toLocalDateTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
            );
        }
        // 处理异常订单,
        for (ReconcileDiscrepancy discrepancy : discrepancies) {
            var discrepancyTrade = platformDiscrepancyMap.get(discrepancy.getOutTradeNo());
            switch (ReconcileDiscrepancyTypeEnum.findByCode(discrepancy.getDiscrepancyType())) {
                // 处理本地短单
                case LOCAL_NOT_EXISTS -> tradeExcels.add(new ReconcileTradeExcel()
                        .setResult(I18nUtil.getEnumName(ReconcileDiscrepancyTypeEnum.LOCAL_NOT_EXISTS))
                        .setTradeNo(discrepancy.getTradeNo())
                        .setChannelTradeNo(discrepancy.getChannelTradeNo())
                        .setChannelTradeType(I18nUtil.getEnumName(TradeTypeEnum.findByCode(discrepancy.getChannelTradeType())))
                        .setChannelTradeStatus(discrepancy.getChannelTradeStatus())
                        .setChannelTradeAmount(PayUtil.toDecimal(discrepancy.getChannelTradeAmount()).toString())
                        .setChannelTradeTime(LocalDateTimeUtil.format(discrepancyTrade.getChannelTradeTime().toLocalDateTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
                );
                // 处理远程短单
                case REMOTE_NOT_EXISTS -> {
                    tradeExcels.add(new ReconcileTradeExcel()
                            .setResult(I18nUtil.getEnumName(ReconcileDiscrepancyTypeEnum.REMOTE_NOT_EXISTS))
                            .setTradeNo(discrepancyTrade.getTradeNo())
                            .setBizTradeNo(discrepancyTrade.getBizTradeNo())
                            .setOutTradeNo(discrepancyTrade.getOutTradeNo())
                            .setTradeType(I18nUtil.getEnumName(TradeTypeEnum.findByCode(discrepancyTrade.getTradeType())))
                            .setTradeAmount(PayUtil.toDecimal(discrepancyTrade.getTradeAmount()).toString())
                            .setTradeStatus(discrepancyTrade.getTradeStatus())
                            .setTradeTime(LocalDateTimeUtil.format(discrepancyTrade.getTradeTime().toLocalDateTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
                    );
                }
                // 处理信息不一致订单
                case NOT_MATCH -> {
                    tradeExcels.add(new ReconcileTradeExcel()
                            .setResult(I18nUtil.getEnumName(ReconcileDiscrepancyTypeEnum.NOT_MATCH))
                            .setTradeNo(discrepancyTrade.getTradeNo())
                            .setBizTradeNo(discrepancyTrade.getBizTradeNo())
                            .setOutTradeNo(discrepancyTrade.getOutTradeNo())
                            .setTradeType(I18nUtil.getEnumName(TradeTypeEnum.findByCode(discrepancyTrade.getTradeType())))
                            .setTradeAmount(PayUtil.toDecimal(discrepancyTrade.getTradeAmount()).toString())
                            .setTradeStatus(discrepancyTrade.getTradeStatus())
                            .setTradeTime(LocalDateTimeUtil.format(discrepancyTrade.getTradeTime().toLocalDateTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
                            .setChannelTradeNo(discrepancy.getChannelTradeNo())
                            .setChannelTradeAmount(PayUtil.toDecimal(discrepancy.getChannelTradeAmount()).toString())
                            .setChannelTradeStatus(discrepancy.getChannelTradeStatus())
                            .setChannelTradeTime(LocalDateTimeUtil.format(discrepancy.getChannelTradeTime().toLocalDateTime(), DatePattern.CHINESE_DATE_TIME_PATTERN))
                    );
                }
            }
        }
        return tradeExcels;
    }
}

