package org.dromara.daxpay.channel.union.service.reconcile;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.compress.Deflate;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.dromara.daxpay.channel.union.code.UnionPayCode;
import org.dromara.daxpay.channel.union.code.UnionReconcileFieldEnum;
import org.dromara.daxpay.channel.union.entity.reconcile.UnionReconcileBillDetail;
import org.dromara.daxpay.channel.union.sdk.api.UnionPayKit;
import org.dromara.daxpay.channel.union.sdk.bean.SDKConstants;
import org.dromara.daxpay.core.enums.TradeStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.reconcile.ChannelReconcileTradeBo;
import org.dromara.daxpay.service.bo.reconcile.ReconcileResolveResultBo;
import org.dromara.daxpay.service.entity.reconcile.ReconcileStatement;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 云闪付对账
 * TODO 目前默认账单除了 S30外都是支付成功状态, 后期需要根据账单类型进行完善
 * @author xxm
 * @since 2024/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionReconcileService {

    private final FileStorageService fileStorageService;

    /**
     * 下载对账单
     */
    public ReconcileResolveResultBo downAndResolve(ReconcileStatement statement, Date date, UnionPayKit unionPayKit){
        // 下载对账单
        Map<String, Object> map = unionPayKit.downloadBill(date, UnionPayCode.RECONCILE_BILL_TYPE);
        Object o = map.get(UnionPayCode.FILE_CONTENT);
        if (o == null) {
            log.warn("云闪付获取对账文件失败");
            throw new OperationFailException("云闪付获取对账文件失败");
        }
        String fileContent = o.toString();
        // 判断是否成功
        if (!SDKConstants.OK_RESP_CODE.equals(map.get(SDKConstants.param_respCode))) {
            log.warn("云闪付获取对账文件失败");
            throw new OperationFailException("云闪付获取对账文件失败");
        }

        try {
            // 先解base64，再DEFLATE解压为zip流
            byte[] decode = Base64.decode(fileContent);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Deflate deflate = Deflate.of(new ByteArrayInputStream(decode), out, false);
            deflate.inflater();
            deflate.close();

            // 读取zip文件, 解析出对账单内容
            byte[] zipBytes = out.toByteArray();
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(zipBytes),"GBK");
            ZipArchiveEntry entry;
            List<UnionReconcileBillDetail> billDetails = new ArrayList<>();
            byte[] bytes = null;
            while ((entry= zipArchiveInputStream.getNextZipEntry()) != null){
                if (StrUtil.startWith(entry.getName(), UnionPayCode.RECONCILE_FILE_PREFIX)){
                    bytes = IoUtil.readBytes(zipArchiveInputStream);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes),"GBK"));
                    // 明细解析
                    List<String> strings = IoUtil.readLines(bufferedReader, new ArrayList<>());
                    billDetails = this.parseDetail(strings);
                }
            }
            // 保存原始对账文件
            String originalFile = this.saveOriginalFile(statement, bytes);
            // 解析账单文件
            var tradeBos = this.convertAndSave(billDetails, statement);
            return new ReconcileResolveResultBo()
                    .setOriginalFileUrl(originalFile)
                    .setChannelTrades(tradeBos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析对账单明细
     */
    private List<UnionReconcileBillDetail> parseDetail(List<String> list){
        return list.stream()
                .map(this::convertDetail)
                .collect(Collectors.toList());
    }

    /**
     * 解析明细条目
     */
    private UnionReconcileBillDetail convertDetail(String line){
        //解析的结果MAP，key为对账文件列序号，value为解析的值
        Map<String,String> zmDataMap = new HashMap<>();
        //左侧游标
        int leftIndex = 0;
        for(int i = 0; i < UnionPayCode.RECONCILE_BILL_SPLIT.length; i++){
            //右侧游标
            int rightIndex = leftIndex + UnionPayCode.RECONCILE_BILL_SPLIT[i];
            String filed = StrUtil.sub(line, leftIndex, rightIndex);
            leftIndex = rightIndex+1;
            // 映射到数据对象
            UnionReconcileFieldEnum fieldEnum = UnionReconcileFieldEnum.findByNo(i);
            if (Objects.nonNull(fieldEnum)){
                zmDataMap.put(fieldEnum.getFiled(), filed.trim());
            }
        }
        return BeanUtil.toBean(zmDataMap, UnionReconcileBillDetail.class);
    }

    /**
     * 转换为通用对账记录对象
     */
    private List<ChannelReconcileTradeBo> convertAndSave(List<UnionReconcileBillDetail> billDetails, ReconcileStatement statement){
        // 只处理支付和退款的对账记录
        return billDetails.stream()
                .map(billDetail->this.convert(billDetail, statement))
                // 只处理支付和退款的对账记录
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 转换为通用对账记录对象
     */
    private ChannelReconcileTradeBo convert(UnionReconcileBillDetail billDetail, ReconcileStatement statement){
        // 金额
        String orderAmount = billDetail.getTxnAmt();
        int amount = Integer.parseInt(orderAmount);

        // 默认为支付对账记录
        ChannelReconcileTradeBo reconcileTradeDetail = new ChannelReconcileTradeBo()
                .setTradeType(TradeTypeEnum.PAY.getCode())
                .setAmount(PayUtil.conversionAmount(amount))
                .setTradeStatus(TradeStatusEnum.SUCCESS.getCode())
                .setTradeNo(billDetail.getOrderId())
                .setOutTradeNo(billDetail.getQueryId());

        // 时间, 从对账订单获取年份
        LocalDate date = statement.getDate();
        String year = LocalDateTimeUtil.format(date, DatePattern.NORM_YEAR_PATTERN);

        String txnTime = year + billDetail.getTxnTime();
        if (StrUtil.isNotBlank(txnTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(txnTime, DatePattern.PURE_DATETIME_PATTERN);
            reconcileTradeDetail.setTradeTime(time);
        }

        // 退款覆盖更新对应的字段
        if (Objects.equals(billDetail.getTradeType(), UnionPayCode.RECONCILE_TYPE_REFUND)){
            reconcileTradeDetail.setTradeType(TradeTypeEnum.REFUND.getCode());
        }
        return reconcileTradeDetail;
    }

    /**
     * 保存下载的原始对账文件
     */
    private String saveOriginalFile(ReconcileStatement statement, byte[] bytes) {

        String date = LocalDateTimeUtil.format(statement.getDate(), DatePattern.PURE_DATE_PATTERN);
        // 将原始文件进行保存 通道-日期
        String fileName = StrUtil.format("交易对账单-云闪付-{}.csv",date);
        var uploadPretreatment = fileStorageService.of(bytes);
        if (StrUtil.isNotBlank(fileName)) {
            uploadPretreatment.setOriginalFilename(fileName);
        }
        FileInfo upload = uploadPretreatment.upload();
        return upload.getUrl();
    }
}
