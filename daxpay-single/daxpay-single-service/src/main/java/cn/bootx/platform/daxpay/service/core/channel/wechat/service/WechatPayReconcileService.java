package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.ReconcileTradeEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.WeChatPayCode;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.dao.WxReconcileBillDetailManager;
import cn.bootx.platform.daxpay.service.core.channel.wechat.dao.WxReconcileBillTotalManger;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WxReconcileBillDetail;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WxReconcileBillTotal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WxReconcileFundFlowDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.StrUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.DownloadBillModel;
import com.ijpay.wxpay.model.DownloadFundFlowModel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.daxpay.service.code.WeChatPayCode.ACCOUNT_TYPE_BASIC;

/**
 * 微信支付对账
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayReconcileService{
    private final WxReconcileBillTotalManger reconcileBillTotalManger;
    private final WxReconcileBillDetailManager reconcileBillDetailManager;

    /**
     * 下载对账单并保存
     * @param date 对账日期 yyyyMMdd 格式
     */
    @Transactional(rollbackFor = Exception.class)
    public void downAndSave(String date,  WeChatPayConfig config) {
        this.downBill(date, config);
        // 资金对账单先不启用
//        this.downFundFlow(date, recordOrderId, config);
    }

    /**
     * 下载交易账单
     *
     * @param date          对账日期 yyyyMMdd 格式
     */
    public void downBill(String date, WeChatPayConfig config){
        // 下载交易账单
        Map<String, String> params = DownloadBillModel.builder()
                .mch_id(config.getWxMchId())
                .appid(config.getWxAppId())
                .nonce_str(WxPayKit.generateStr())
                .bill_date(date)
                .bill_type("ALL")
                .build()
                .createSign(config.getApiKeyV2(), SignType.HMACSHA256);
        String result = WxPayApi.downloadBill(config.isSandbox(), params);

        // 验证返回数据是否成功
        this.verifyBillMsg(result);

        // 过滤特殊字符
        result = result.replaceAll("`", "").replaceAll("\uFEFF", "");
        CsvReader reader = CsvUtil.getReader();

        // 获取交易记录并保存 同时过滤出当前应用的交易记录
        String billDetail = StrUtil.subBefore(result, "总交易单数", false);
        List<WxReconcileBillDetail> billDetails = reader.read(billDetail, WxReconcileBillDetail.class).stream()
                .filter(o->Objects.equals(o.getAppId(), config.getWxAppId()))
                .collect(Collectors.toList());

        Long recordOrderId = PaymentContextLocal.get().getReconcileInfo().getReconcileOrder().getId();

        billDetails.forEach(o->o.setRecordOrderId(recordOrderId));
        reconcileBillDetailManager.saveAll(billDetails);

        // 获取汇总数据并保存
        String billTotal = result.substring(result.indexOf("总交易单数"));
        List<WxReconcileBillTotal> billTotals = reader.read(billTotal, WxReconcileBillTotal.class);
        billTotals.forEach(o->o.setRecordOrderId(recordOrderId));
        reconcileBillTotalManger.saveAll(billTotals);

        // 将原始交易明细对账记录转换通用结构并保存到上下文中
        this.convertAndSave(billDetails);
    }

    /**
     * 上传对账单
     */
    @SneakyThrows
    public void uploadBill(byte[] bytes, WeChatPayConfig config){
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes), "GBK"));
        String result = IoUtil.read(reader);
        // 过滤特殊字符
        result = result.replaceAll("`", "").replaceAll("\uFEFF", "");
        CsvReader csvRows = CsvUtil.getReader();

        // 对账订单
        ReconcileOrder reconcileOrder = PaymentContextLocal.get()
                .getReconcileInfo()
                .getReconcileOrder();

        // 获取交易记录并保存 同时过滤出当前应用的交易记录
        String billDetail = StrUtil.subBefore(result, "总交易单数", false);
        List<WxReconcileBillDetail> billDetails = csvRows.read(billDetail, WxReconcileBillDetail.class).stream()
                // 只读取当前商户的订单
                .filter(o->Objects.equals(o.getAppId(), config.getWxAppId()))
                // 只读取对账日的记录
                .filter(o->{
                    String transactionTime = o.getTransactionTime();
                    LocalDateTime time = LocalDateTimeUtil.parse(transactionTime, DatePattern.NORM_DATETIME_PATTERN);
                    return Objects.equals(reconcileOrder.getDate(), LocalDate.from(time));
                })
                .collect(Collectors.toList());

        billDetails.forEach(o->o.setRecordOrderId(reconcileOrder.getId()));
        reconcileBillDetailManager.saveAll(billDetails);

        // 将原始交易明细对账记录转换通用结构并保存到上下文中
        this.convertAndSave(billDetails);
    }

    /**
     * 转换为通用对账记录对象
     */
    public void convertAndSave(List<WxReconcileBillDetail> billDetails){
        List<ReconcileDetail> collect = billDetails.stream()
                .map(this::convert)
                .collect(Collectors.toList());
        // 写入到上下文中
        PaymentContextLocal.get().getReconcileInfo().setReconcileDetails(collect);
    }

    /**
     * 转换为通用对账记录对象
     */
    public ReconcileDetail convert(WxReconcileBillDetail billDetail){
        // 默认为支付对账记录
        ReconcileDetail reconcileDetail = new ReconcileDetail()
                .setReconcileId(billDetail.getRecordOrderId())
                .setTradeNo(billDetail.getMchOrderNo())
                .setTitle(billDetail.getSubject())
                .setOutTradeNo(billDetail.getWeiXinOrderNo());
        // 交易时间
        String transactionTime = billDetail.getTransactionTime();
        if (StrUtil.isNotBlank(transactionTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(transactionTime, DatePattern.NORM_DATETIME_PATTERN);
            reconcileDetail.setTradeTime(time);
        }

        // 支付
        if (Objects.equals(billDetail.getStatus(), "SUCCESS")){
            // 金额
            String orderAmount = billDetail.getOrderAmount();
            double v = Double.parseDouble(orderAmount) * 100;
            int amount = Math.abs(((int) v));
            reconcileDetail.setType(ReconcileTradeEnum.PAY.getCode())
                    .setAmount(amount);
        }
        // 退款
        if (Objects.equals(billDetail.getStatus(), "REFUND")){
            // 金额
            String refundAmount = billDetail.getApplyRefundAmount();
            double v = Double.parseDouble(refundAmount) * 100;
            int amount = Math.abs(((int) v));
            reconcileDetail.setType(ReconcileTradeEnum.REFUND.getCode())
                    .setAmount(amount)
                    .setTradeNo(billDetail.getMchRefundNo());
        }
        // TODO 已撤销, 暂时未处理
        if (Objects.equals(billDetail.getStatus(), "REVOKED")){
            log.warn("对账出现已撤销记录, 后续进行处理");
        }
        return reconcileDetail;
    }


    /**
     * 下载资金账单, 需要双向证书,
     * 如果出现 No appropriate protocol (protocol is disabled or cipher suites are inappropriate)
     * 更换一下最新的JDK即可, 例如 corretto Jdk
     *
     * @param date          对账日期 yyyyMMdd 格式
     * @param recordOrderId 对账订单ID
     */
    public void downFundFlow(String date, Long recordOrderId, WeChatPayConfig config){
        if (StrUtil.isBlank(config.getP12())){
            return;
        }
        Map<String, String> fundFlow = DownloadFundFlowModel.builder()
                .mch_id(config.getWxMchId())
                .appid(config.getWxAppId())
                .nonce_str(WxPayKit.generateStr())
                .bill_date(date)
                .account_type(ACCOUNT_TYPE_BASIC)
                .build()
                .createSign(config.getApiKeyV2(), SignType.HMACSHA256);
        byte[] fileBytes = Base64.decode(config.getP12());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
        // 证书密码为 微信商户号
        String result = WxPayApi.downloadFundFlow(fundFlow, inputStream, config.getWxMchId());

        // 验证返回数据是否成功
        this.verifyFundFlowMsg(result);

        // 过滤特殊字符
        result = result.replaceAll("`", "").replaceAll("\uFEFF", "");
        CsvReader reader = CsvUtil.getReader();

        // 获取交易记录
        String billDetail = StrUtil.subBefore(result, "资金流水总笔数", false);
        List<WxReconcileFundFlowDetail> billDetails = reader.read(billDetail, WxReconcileFundFlowDetail.class);

        // 获取汇总数据
        String billTotal = result.substring(result.indexOf("资金流水总笔数"));
        List<WxReconcileFundFlowDetail> billTotals = reader.read(billTotal, WxReconcileFundFlowDetail.class);

    }


    /**
     * 验证交易对账单错误信息
     */
    private void verifyBillMsg(String res) {
        // 判断是否能是对账单还是特殊数据
        if (StrUtil.startWith(res, "\uFEFF")){
            return;
        }

        // 出现xml返回说明一定是错误了
        Map<String, String> result = WxPayKit.xmlToMap(res);

        // 返回码
        String errCode = result.get(WeChatPayCode.ERROR_CODE);
        // 原因
        String resultMsg = result.get(WeChatPayCode.RETURN_MSG);

        // 处理20002 分别是 账单不存在 账单未生成
        if (Objects.equals(errCode, "20002")){
            if (Objects.equals("No Bill Exist",resultMsg)){
                log.warn("交易账单不存在, 请检查当前商户号在指定日期内是否有成功的交易");
                throw new PayFailureException("交易账单不存在");
            }
            if (Objects.equals("Bill Creating",resultMsg)){
                log.warn("交易账单未生成, 请在上午10点以后再试");
                throw new PayFailureException("交易账单未生成, 请在上午10点以后再试");
            }

        } else {
            log.error("微信交易对账失败, 原因: {}, 错误码: {}, 错误信息: {}", resultMsg, errCode, res);
            throw new PayFailureException("微信支付交易对账失败");
        }
    }

    /**
     * 验证资金对账单
     */
    private void verifyFundFlowMsg(String res) {
        // 判断是否能是对账单还是特殊数据
        if (StrUtil.startWith(res, "\uFEFF")){
            return;
        }
        // 出现xml返回说明一定是错误了
        Map<String, String> result = WxPayKit.xmlToMap(res);
        String returnCode = result.get(WeChatPayCode.RETURN_CODE);
        String errorMsg = result.get(WeChatPayCode.ERR_CODE_DES);
        if (StrUtil.isBlank(errorMsg)) {
            errorMsg = result.get(WeChatPayCode.RETURN_MSG);
        }
        log.error("微信资金对账失败, 原因: {}, 错误码: {}, 错误信息: {}", errorMsg, returnCode, res);
        throw new PayFailureException("微信资金对账失败: " + errorMsg);
    }

}
