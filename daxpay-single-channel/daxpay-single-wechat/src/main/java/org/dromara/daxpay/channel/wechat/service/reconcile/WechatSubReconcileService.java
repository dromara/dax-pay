package org.dromara.daxpay.channel.wechat.service.reconcile;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.bean.request.WxPayApplyTradeBillV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayApplyBillV3Result;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.entity.reconcile.WechatReconcileBillDetail;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.core.enums.TradeStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.service.bo.reconcile.ChannelReconcileTradeBo;
import org.dromara.daxpay.service.bo.reconcile.ReconcileResolveResultBo;
import org.dromara.daxpay.service.entity.reconcile.ReconcileStatement;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class WechatSubReconcileService {
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
        WechatPayConfig config = wechatPayConfigService.getAndCheckConfig(true);
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

            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
            String result = IoUtil.read(reader);
            // 过滤特殊字符
            result = result.replaceAll("`", "").replaceAll("\uFEFF", "");
            CsvReader csvRows = CsvUtil.getReader();
            // 获取交易记录并保存 同时过滤出当前应用的交易记录
            String billDetail = StrUtil.subBefore(result, "总交易单数", false);
            var billDetails = csvRows.read(billDetail, WechatReconcileBillDetail.class).stream()
                    // 只读取当前商户的订单
                    .filter(o->Objects.equals(o.getAppid(), config.getSubAppId()))
                    // 只读取对账日的记录
                    .filter(o->{
                        String transactionTime = o.getTransactionTime();
                        LocalDateTime time = LocalDateTimeUtil.parse(transactionTime, DatePattern.NORM_DATETIME_PATTERN);
                        return Objects.equals(statement.getDate(), LocalDate.from(time));
                    })
                    .toList();

            // 保存原始对账文件
            String originalFile = this.saveOriginalFile(statement, bytes);
            // 解析账单文件
            var tradeBos = this.convertReconcileTrade(billDetails);
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
        // 交易时间时间 指该笔交易的支付成功时间或发起退款成功时间（注：不是退款成功时间）
        LocalDateTime time = LocalDateTimeUtil.parse(billDetail.getTransactionTime(), DatePattern.NORM_DATETIME_PATTERN);
        // 默认为支付对账记录
        ChannelReconcileTradeBo tradeBo = new ChannelReconcileTradeBo()
                .setOutTradeNo(billDetail.getOutTradeNo())
                .setTradeType(TradeTypeEnum.PAY.getCode())
                .setTradeTime(time)
                .setTradeNo(billDetail.getTransactionId());
        if (Objects.equals(billDetail.getTradeState(), WxPayConstants.WxpayTradeStatus.SUCCESS)) {
            tradeBo.setTradeType(TradeTypeEnum.PAY.getCode())
                    .setAmount(new BigDecimal(billDetail.getTotalFee()))
           .setTradeStatus(TradeStatusEnum.SUCCESS.getCode());
        }

        // 退款覆盖更新对应的字段
        if (Objects.equals(billDetail.getTradeState(), WxPayConstants.WxpayTradeStatus.REFUND)) {
            tradeBo.setOutTradeNo(billDetail.getOutRefundNo())
                    .setTradeNo(billDetail.getRefundId())
                    .setAmount(new BigDecimal(billDetail.getRefundFee()))
                    .setTradeType(TradeTypeEnum.REFUND.getCode());
            tradeBo.setTradeType(TradeTypeEnum.REFUND.getCode());
            // 状态
            switch (billDetail.getRefundStatus()) {
                case WxPayConstants.RefundStatus.SUCCESS -> tradeBo.setTradeStatus(TradeStatusEnum.SUCCESS.getCode());
                case WxPayConstants.RefundStatus.PROCESSING -> tradeBo.setTradeStatus(TradeStatusEnum.CLOSED.getCode());
                case WxPayConstants.ResultCode.FAIL -> tradeBo.setTradeStatus(TradeStatusEnum.FAIL.getCode());
                case WxPayConstants.RefundStatus.CHANGE -> tradeBo.setTradeStatus(TradeStatusEnum.EXCEPTION.getCode());
            }
            tradeBo.setTradeStatus(TradeStatusEnum.SUCCESS.getCode());
        }
        // 撤销状态
        if (Objects.equals(billDetail.getTradeState(), WxPayConstants.WxpayTradeStatus.REVOKED)) {
            tradeBo.setTradeType(TradeTypeEnum.PAY.getCode());
            tradeBo.setTradeStatus(TradeStatusEnum.REVOKED.getCode());
        }

        return tradeBo;
    }

    /**
     * 保存下载的原始对账文件
     */
    private String saveOriginalFile(ReconcileStatement reconcileOrder, byte[] bytes) {
        String date = LocalDateTimeUtil.format(reconcileOrder.getDate(), DatePattern.PURE_DATE_PATTERN);
        // 将原始文件进行保存 通道-日期
        String fileName = StrUtil.format("交易对账单-微信-{}.csv",date);
        var uploadPretreatment = fileStorageService.of(bytes);
        if (StrUtil.isNotBlank(fileName)) {
            uploadPretreatment.setOriginalFilename(fileName);
        }
        FileInfo upload = uploadPretreatment.upload();
        return upload.getUrl();
    }
}
