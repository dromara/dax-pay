package cn.daxpay.multi.channel.wechat.service.reconcile;

import cn.daxpay.multi.channel.wechat.bo.reconcile.WechatReconcileBillDetail;
import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.core.exception.OperationFailException;
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
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            params.setHeadRows(1);
            List<WechatReconcileBillDetail> details = ExcelImportUtil.importExcel(new ByteArrayInputStream(bytes), WechatReconcileBillDetail.class, params);

            // 保存原始对账文件
            String originalFile = this.saveOriginalFile(statement, bytes);
            // 解析账单文件
//            ReconcileResolveResultBo result = this.convertReconcileTrade( details);


            return new ReconcileResolveResultBo()
                    .setOriginalFileUrl(originalFile);
        } catch (WxPayException | IOException e) {
            log.error("下载对账单失败", e);
            throw new OperationFailException("下载对账单失败");
        }
    }

    /**
     * 转换为通用对账记录对象
     */
    private List<ReconcileResolveResultBo> convertReconcileTrade(List<WechatReconcileBillDetail> billDetails){
        return billDetails.stream()
                .map(this::convert)
                .toList();
    }

    /**
     * 转换为通用对账记录对象
     */
    private ReconcileResolveResultBo convert(WechatReconcileBillDetail billDetail){
//        // 金额
//        var amount = new BigDecimal(billDetail.getTotalFee());
//
//        // 默认为支付对账记录
//        ReconcileResolveResultBo reconcileTradeBo = new ReconcileResolveResultBo()
//                .setTradeNo(billDetail.getOutRefundNo())
//                .setType(TradeTypeEnum.PAY.getCode())
//                .setAmount(amount)
//                .setOutTradeNo(billDetail.getTradeNo());
//        // 时间
//        String endTime = billDetail.getEndTime();
//        if (StrUtil.isNotBlank(endTime)) {
//            LocalDateTime time = LocalDateTimeUtil.parse(endTime, DatePattern.NORM_DATETIME_PATTERN);
//            reconcileTradeBo.setTradeTime(time);
//        }
//
//        // 退款覆盖更新对应的字段
//        if (Objects.equals(billDetail.getTradeType(), "退款")){
//            reconcileTradeBo.setTradeNo(billDetail.getBatchNo())
//                    .setType(TradeTypeEnum.REFUND.getCode());
//        }
//        return reconcileTradeBo;
        return null;
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
