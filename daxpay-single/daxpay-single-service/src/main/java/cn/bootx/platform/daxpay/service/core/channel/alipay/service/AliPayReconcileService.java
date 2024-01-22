package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.AliPayCode;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.alipay.dao.AliReconcileBillDetailManager;
import cn.bootx.platform.daxpay.service.core.channel.alipay.dao.AliReconcileBillTotalManager;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliReconcileBillDetail;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliReconcileBillTotal;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileDetail;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付宝对账服务
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayReconcileService {
    private final AliPayConfigService configService;

    private final AliReconcileBillDetailManager reconcileBillDetailManager;

    private final AliReconcileBillTotalManager reconcileBillTotalManager;

    /**
     * 下载对账单, 并进行解析进行保存
     *
     * @param date 对账日期 yyyy-MM-dd 格式
     * @param recordOrderId 对账订单ID
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void downAndSave(String date, Long recordOrderId){
        AliPayConfig config = configService.getConfig();
        configService.initConfig(config);

        try {
            AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
            model.setBillDate(date);
            model.setBillType("trade");
            val response = AliPayApi.billDownloadUrlQueryToResponse(model);
            // 判断返回结果
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                log.error("获取支付宝对账单失败: {}", response.getSubMsg());
                throw new PayFailureException(response.getSubMsg());
            }

            // 获取对账单下载地址并下载
            String url = response.getBillDownloadUrl();
            byte[] bytes = HttpUtil.downloadBytes(url);
            // 使用 Apache commons-compress 包装流, 读取返回的对账CSV文件
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(bytes),"GBK");
            ZipArchiveEntry entry;
            List<AliReconcileBillDetail> billDetails = new ArrayList<>();
            List<AliReconcileBillTotal> billTotals = new ArrayList<>();
            while ((entry= zipArchiveInputStream.getNextZipEntry()) != null){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipArchiveInputStream,"GBK"));
                List<String> strings = IoUtil.readLines(bufferedReader, new ArrayList<>());
                String name = entry.getName();
                if (StrUtil.endWith(name,"_业务明细(汇总).csv")){
                    // 汇总
                    billTotals = this.parseTotal(strings);
                } else {
                    // 明细
                    billDetails = this.parseDetail(strings);
                }
            }
            // 保存原始对账记录
            this.save(billDetails, billTotals, recordOrderId);

            // 将原始交易明细对账记录转换通用结构并保存到上下文中
            this.convertAndSave(billDetails);

        } catch (AlipayApiException e) {
            log.error("下载对账单失败",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存原始对账记录
     */
    private void save(List<AliReconcileBillDetail> billDetails, List<AliReconcileBillTotal> billTotals, Long recordOrderId){
        billDetails.forEach(o->o.setRecordOrderId(recordOrderId));
        reconcileBillDetailManager.saveAll(billDetails);
        billTotals.forEach(o->o.setRecordOrderId(recordOrderId));
        reconcileBillTotalManager.saveAll(billTotals);
    }

    /**
     * 转换为通用对账记录对象
     */
    private void convertAndSave(List<AliReconcileBillDetail> billDetails){
        List<PayReconcileDetail> collect = billDetails.stream()
                .map(this::convert)
                .collect(Collectors.toList());
        // 写入到上下文中
        PaymentContextLocal.get().getReconcile().setReconcileDetails(collect);
    }

    /**
     * 转换为通用对账记录对象
     */
    private PayReconcileDetail convert(AliReconcileBillDetail billDetail){
        // 金额
        String orderAmount = billDetail.getOrderAmount();
        double v = Double.parseDouble(orderAmount) * 100;
        int amount = Math.abs(((int) v));

        // 默认为支付对账记录
        PayReconcileDetail payReconcileDetail = new PayReconcileDetail()
                .setRecordOrderId(billDetail.getRecordOrderId())
                .setPaymentId(billDetail.getOutTradeNo())
                .setType("pay")
                .setAmount(amount)
                .setTitle(billDetail.getSubject())
                .setGatewayOrderNo(billDetail.getTradeNo());
        // 退款覆盖更新对应的字段
        if (Objects.equals(billDetail.getTradeType(), "退款")){
            payReconcileDetail.setRefundId(billDetail.getBatchNo())
                    .setType("refund");
        }

        return payReconcileDetail;
    }


    /**
     * 解析明细
     */
    public List<AliReconcileBillDetail> parseDetail(List<String> list){
        // 截取需要进行解析的文本内容
        String billDetail = list.stream()
                .collect(Collectors.joining(System.lineSeparator()));
        billDetail = StrUtil.subBetween(billDetail,
                "#-----------------------------------------业务明细列表----------------------------------------"+System.lineSeparator(),
                "#-----------------------------------------业务明细列表结束------------------------------------");
        billDetail = billDetail.replaceAll("\t", "");
        CsvReader reader = CsvUtil.getReader();
        return reader.read(billDetail, AliReconcileBillDetail.class);
    }

    /**
     * 解析汇总
     */
    public List<AliReconcileBillTotal> parseTotal(List<String> list){
        // 去除前 4 行和后 2 行 然后合并是个一个字符串
        String billTotal = list.stream()
                .collect(Collectors.joining(System.lineSeparator()));
        billTotal = StrUtil.subBetween(billTotal,
                "#-----------------------------------------业务汇总列表----------------------------------------"+System.lineSeparator(),
                "#----------------------------------------业务汇总列表结束-------------------------------------");

        billTotal = billTotal.replaceAll("\t", "");
        CsvReader reader = CsvUtil.getReader();
        return reader.read(billTotal, AliReconcileBillTotal.class);
    }
}
