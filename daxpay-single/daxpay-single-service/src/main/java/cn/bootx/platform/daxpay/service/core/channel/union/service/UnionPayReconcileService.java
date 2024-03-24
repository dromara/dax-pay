package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.ReconcileTradeEnum;
import cn.bootx.platform.daxpay.service.code.UnionPayCode;
import cn.bootx.platform.daxpay.service.code.UnionReconcileFieldEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionReconcileBillDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDetail;
import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.compress.Deflate;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.bootx.platform.daxpay.service.code.UnionPayCode.RECONCILE_BILL_SPLIT;
import static cn.bootx.platform.daxpay.service.code.UnionPayCode.RECONCILE_BILL_TYPE;

/**
 * 云闪付对账
 * @author xxm
 * @since 2024/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayReconcileService {

    /**
     * 下载对账单
     */
    public void downAndSave(Date date, Long recordOrderId, UnionPayKit unionPayKit){
        // 下载对账单
        Map<String, Object> stringObjectMap = unionPayKit.downloadBill(date, RECONCILE_BILL_TYPE);

        String fileContent = stringObjectMap.get("fileContent").toString();
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
            while ((entry= zipArchiveInputStream.getNextZipEntry()) != null){
                System.out.println(StrUtil.startWith(entry.getName(), "INN"));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipArchiveInputStream,"GBK"));
                if (StrUtil.startWith(entry.getName(), "INN")){
                    // 明细解析
                    List<String> strings = IoUtil.readLines(bufferedReader, new ArrayList<>());
                    billDetails = this.parseDetail(strings);
                } else {
                    // 汇总目前不进行处理
                }
            }
            // 保存原始对账记录
            this.save(billDetails, recordOrderId);

            // 将原始交易明细对账记录转换通用结构并保存到上下文中
            this.convertAndSave(billDetails);
//            Reader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("D:/data/INN24031100ZM_777290058206553.txt"))));
//            List<String> strings = IoUtil.readLines(bufferedReader, new ArrayList<>());
//            List<UnionReconcileBillDetail> unionReconcileBillDetails = this.parseDetail(strings);
//            System.out.println(unionReconcileBillDetails);
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
        //右侧游标
        int rightIndex = 0;
        for(int i=0;i<RECONCILE_BILL_SPLIT.length;i++){
            rightIndex = leftIndex + RECONCILE_BILL_SPLIT[i];
            String filed = StrUtil.sub(line, leftIndex, rightIndex);
            leftIndex = rightIndex+1;

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
    private void convertAndSave(List<UnionReconcileBillDetail> billDetails){
        List<ReconcileDetail> collect = billDetails.stream()
                .map(this::convert)
                .collect(Collectors.toList());
        // 写入到上下文中
        PaymentContextLocal.get().getReconcileInfo().setReconcileDetails(collect);
    }



    /**
     * 转换为通用对账记录对象
     */
    private ReconcileDetail convert(UnionReconcileBillDetail billDetail){
        // 金额
        String orderAmount = billDetail.getTxnAmt();
        int amount = Integer.parseInt(orderAmount);

        // 默认为支付对账记录
        ReconcileDetail reconcileDetail = new ReconcileDetail()
                .setRecordOrderId(billDetail.getRecordOrderId())
                .setOrderId(billDetail.getOrderId())
                .setType(ReconcileTradeEnum.PAY.getCode())
                .setAmount(amount)
                .setGatewayOrderNo(billDetail.getQueryId());

        // 时间
        String txnTime = billDetail.getTxnTime();
        if (StrUtil.isNotBlank(txnTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(txnTime, DatePattern.NORM_DATETIME_PATTERN);
            reconcileDetail.setOrderTime(time);
        }

        // 退款覆盖更新对应的字段
        if (Objects.equals(billDetail.getTradeType(), UnionPayCode.RECONCILE_TYPE_REFUND)){
            reconcileDetail.setType(ReconcileTradeEnum.REFUND.getCode());
        }
        return reconcileDetail;
    }

    /**
     * 保存原始对账记录
     */
    private void save(List<UnionReconcileBillDetail> billDetails, Long recordOrderId){
        billDetails.forEach(o->o.setRecordOrderId(recordOrderId));
    }

}
