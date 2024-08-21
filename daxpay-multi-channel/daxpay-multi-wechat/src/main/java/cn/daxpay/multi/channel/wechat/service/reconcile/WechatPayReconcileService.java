package cn.daxpay.multi.channel.wechat.service.reconcile;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.daxpay.multi.channel.wechat.bo.reconcile.WechatReconcileBillDetail;
import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.core.exception.OperationFailException;
import cn.daxpay.multi.service.bo.reconcile.ChannelReconcileTradeBo;
import cn.daxpay.multi.service.bo.reconcile.ReconcileResolveResultBo;
import cn.daxpay.multi.service.entity.reconcile.ReconcileStatement;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.bean.request.WxPayApplyTradeBillV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayApplyBillV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 微信支付对账
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayReconcileService {
    private final FileStorageService fileStorageService;
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 下载对账单并保存
     *
     * @param statement 对账订单
     * @param date        对账日期 yyyyMMdd 格式
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public ReconcileResolveResultBo downAndResolve(ReconcileStatement statement, String date) {
        WechatPayConfig config = wechatPayConfigService.getWechatPayConfig();
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        var request = new WxPayApplyTradeBillV3Request();
        request.setBillDate(date);
        request.setBillType("ALL");
        try {
            WxPayApplyBillV3Result wxPayApplyBillV3Result = wxPayService.applyTradeBill(request);
            String downloadUrl = wxPayApplyBillV3Result.getDownloadUrl();
            byte[] bytes;
            try (InputStream inputStream = wxPayService.downloadBill(downloadUrl)) {
                bytes = IoUtil.readBytes(inputStream);
            }
            List<WechatReconcileBillDetail> details = ExcelImportUtil
                    .importExcel(new ByteArrayInputStream(bytes), WechatReconcileBillDetail.class, new ImportParams());
            // 去除前缀的 ` 符号
            details.forEach(WechatReconcileBillDetail::removeStartSymbol);
            // 保存原始对账文件
            String originalFile = this.saveOriginalFile(statement, bytes);
            // 解析账单文件
            var tradeBos = this.convertReconcileTrade(details);
            return new ReconcileResolveResultBo()
                    .setOriginalFileUrl(originalFile)
                    .setChannelTrades(tradeBos);
        } catch (WxPayException | IOException e) {
            log.error("下载对账单失败", e);
            throw new OperationFailException("下载对账单失败");
        }
    }

    /**
     * 转换为通用对账记录对象
     */
    private List<ChannelReconcileTradeBo> convertReconcileTrade(List<WechatReconcileBillDetail> billDetails){
        return billDetails.stream()
                .map(this::convert)
                .toList();
    }

    /**
     * 转换为通用对账记录对象
     */
    private ChannelReconcileTradeBo convert(WechatReconcileBillDetail billDetail){
        // 金额
        var amount = new BigDecimal(billDetail.getTotalFee());
        // 默认为支付对账记录
        ChannelReconcileTradeBo reconcileTradeBo = new ChannelReconcileTradeBo()
                .setOutTradeNo(billDetail.getOutTradeNo())
                .setTradeType(TradeTypeEnum.PAY.getCode())
                .setAmount(amount)
                .setTradeNo(billDetail.getTransactionId());
        // 时间
        String endTime = billDetail.getTransactionTime();
        if (StrUtil.isNotBlank(endTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(endTime, DatePattern.NORM_DATETIME_PATTERN);
            reconcileTradeBo.setTradeTime(time);
        }

        // 退款覆盖更新对应的字段
        if (Objects.equals(billDetail.getTradeType(), "REFUND")){
            reconcileTradeBo.setOutTradeNo(billDetail.getOutRefundNo())
                    .setTradeNo(billDetail.getRefundId())
                    .setTradeType(TradeTypeEnum.REFUND.getCode());
        }
        return reconcileTradeBo;
    }

    /**
     * 保存下载的原始对账文件
     */
    private String saveOriginalFile(ReconcileStatement reconcileOrder, byte[] bytes) {
        String date = LocalDateTimeUtil.format(reconcileOrder.getDate(), DatePattern.PURE_DATE_PATTERN);
        // 将原始文件进行保存 通道-日期
        String fileName = StrUtil.format("交易对账单-微信-{}.xlsx",date);
        var uploadPretreatment = fileStorageService.of(bytes);
        if (StrUtil.isNotBlank(fileName)) {
            uploadPretreatment.setOriginalFilename(fileName);
        }
        FileInfo upload = uploadPretreatment.upload();
        return upload.getUrl();
    }
}
