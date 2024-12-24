package org.dromara.daxpay.channel.alipay.service.reconcile;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.dromara.daxpay.channel.alipay.bo.AlipayReconcileBillDetail;
import org.dromara.daxpay.channel.alipay.bo.AlipayReconcileBillTotal;
import org.dromara.daxpay.channel.alipay.entity.AliPayConfig;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.core.enums.TradeStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.exception.ReconciliationFailException;
import org.dromara.daxpay.service.bo.reconcile.ChannelReconcileTradeBo;
import org.dromara.daxpay.service.bo.reconcile.ReconcileResolveResultBo;
import org.dromara.daxpay.service.entity.reconcile.ReconcileStatement;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.upload.UploadPretreatment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class AlipayReconcileService {

    private final AlipayConfigService aliPayConfigService;

    private final FileStorageService fileStorageService;


    /**
     * 下载对账单, 并进行解析
     *
     * @param date        对账日期 yyyy-MM-dd 格式
     * @param statement 对账单对象
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public ReconcileResolveResultBo downAndResolve(String date, ReconcileStatement statement, AliPayConfig aliPayConfig){
        try {
            AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
            model.setBillDate(date);
            // 下载交易类型
            model.setBillType("trade");
            AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
            // 特约商户调用
            if (aliPayConfig.isIsv()){
                request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, aliPayConfig.getAppAuthToken());
            }
            request.setBizModel(model);
            var response = aliPayConfigService.execute(request,aliPayConfig);
            // 判断返回结果
            if (!response.isSuccess()) {
                log.error("获取支付宝对账单失败: {}", response.getSubMsg());
                throw new ReconciliationFailException(response.getSubMsg());
            }

            // 获取对账单下载地址并下载
            String url = response.getBillDownloadUrl();
            byte[] zipBytes = HttpUtil.downloadBytes(url);
            // 使用 Apache commons-compress 包装流, 读取返回的对账CSV文件
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(zipBytes), "GBK");
            ZipArchiveEntry entry;
            List<AlipayReconcileBillDetail> billDetails = new ArrayList<>();
            byte[] bytes = null;
            while ((entry = zipArchiveInputStream.getNextZipEntry()) != null) {
                bytes = IoUtil.readBytes(zipArchiveInputStream);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes), "GBK"));
                List<String> strings = IoUtil.readLines(bufferedReader, new ArrayList<>());
                String name = entry.getName();
                // 只处理明细
                if (!StrUtil.endWith(name, "_业务明细(汇总).csv")) {
                    billDetails = this.parseDetail(strings);
                }
            }
            // 保存原始对账文件
            String originalFile = this.saveOriginalFile(statement, bytes);

            // 将原始交易明细对账记录转换通用结构
            var reconcileTradeBos = this.convertReconcileTrade(billDetails);
            return new ReconcileResolveResultBo()
                    .setChannelTrades(reconcileTradeBos)
                    .setOriginalFileUrl(originalFile);

        } catch (AlipayApiException e) {
            log.error("下载对账单失败",e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 上传对账单解析并保存
     */
    @SneakyThrows
    public ReconcileResolveResultBo upload(ReconcileStatement statement, byte[] bytes) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes),"GBK"));
        List<String> strings = IoUtil.readLines(bufferedReader, new ArrayList<>());
        List<AlipayReconcileBillDetail> billDetails = this.parseDetail(strings);
        // 保存原始对账文件
        String originalFile = this.saveOriginalFile(statement, bytes);
        // 将原始交易明细对账记录转换通用结构
        var reconcileTradeBos = this.convertReconcileTrade(billDetails);
        return new ReconcileResolveResultBo()
                .setChannelTrades(reconcileTradeBos)
                .setOriginalFileUrl(originalFile);
    }


    /**
     * 转换为通用对账记录对象
     */
    private List<ChannelReconcileTradeBo> convertReconcileTrade(List<AlipayReconcileBillDetail> billDetails){
        return billDetails.stream()
                .map(this::convert)
                .toList();
    }

    /**
     * 转换为通用对账记录对象
     */
    private ChannelReconcileTradeBo convert(AlipayReconcileBillDetail billDetail){
        // 金额
        var amount = new BigDecimal(billDetail.getOrderAmount());
        // 默认为支付对账记录
        ChannelReconcileTradeBo reconcileTradeBo = new ChannelReconcileTradeBo()
                .setOutTradeNo(billDetail.getOutTradeNo())
                .setTradeType(TradeTypeEnum.PAY.getCode())
                .setAmount(amount)
                .setTradeStatus(TradeStatusEnum.SUCCESS.getCode())
                .setTradeNo(billDetail.getTradeNo());
        // 时间
        String endTime = billDetail.getEndTime();
        if (StrUtil.isNotBlank(endTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(endTime, DatePattern.NORM_DATETIME_PATTERN);
            reconcileTradeBo.setTradeTime(time);
        }

        // 退款覆盖更新对应的字段
        if (Objects.equals(billDetail.getTradeType(), "退款")){
            reconcileTradeBo.setOutTradeNo(billDetail.getBatchNo())
                    .setTradeType(TradeTypeEnum.REFUND.getCode());
        }
        return reconcileTradeBo;
    }


    /**
     * 解析明细
     */
    private List<AlipayReconcileBillDetail> parseDetail(List<String> list){
        // 截取需要进行解析的文本内容
        String billDetail = list.stream()
                .collect(Collectors.joining(System.lineSeparator()));
        billDetail = StrUtil.subBetween(billDetail,
                "#-----------------------------------------业务明细列表----------------------------------------"+System.lineSeparator(),
                "#-----------------------------------------业务明细列表结束------------------------------------");
        billDetail = billDetail.replaceAll("\t", "");
        CsvReader reader = CsvUtil.getReader();
        return reader.read(billDetail, AlipayReconcileBillDetail.class);
    }

    /**
     * 解析汇总
     */
    private List<AlipayReconcileBillTotal> parseTotal(List<String> list){
        // 去除前 4 行和后 2 行 然后合并是个一个字符串
        String billTotal = list.stream()
                .collect(Collectors.joining(System.lineSeparator()));
        billTotal = StrUtil.subBetween(billTotal,
                "#-----------------------------------------业务汇总列表----------------------------------------"+System.lineSeparator(),
                "#----------------------------------------业务汇总列表结束-------------------------------------");

        billTotal = billTotal.replaceAll("\t", "");
        CsvReader reader = CsvUtil.getReader();
        return reader.read(billTotal, AlipayReconcileBillTotal.class);
    }

    /**
     * 保存下载的原始对账文件
     */
    private String saveOriginalFile(ReconcileStatement statement, byte[] bytes) {
        // 将原始文件进行保存
        String date = LocalDateTimeUtil.format(statement.getDate(), DatePattern.PURE_DATE_PATTERN);
        // 将原始文件进行保存 通道-日期
        String fileName = StrUtil.format("交易对账单-支付宝-{}.csv",date);
        UploadPretreatment uploadPretreatment = fileStorageService.of(bytes);
        if (StrUtil.isNotBlank(fileName)) {
            uploadPretreatment.setOriginalFilename(fileName);
        }
        FileInfo upload = uploadPretreatment.upload();
        return upload.getUrl();
    }
}
