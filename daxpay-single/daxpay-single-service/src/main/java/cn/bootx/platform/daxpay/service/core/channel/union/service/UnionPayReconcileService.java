package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.ReconcileTradeEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.UnionPayCode;
import cn.bootx.platform.daxpay.service.code.UnionReconcileFieldEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.union.dao.UnionReconcileBillDetailManager;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionReconcileBillDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.compress.Deflate;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.egzosn.pay.union.bean.SDKConstants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.bootx.platform.daxpay.service.code.UnionPayCode.*;

/**
 * 云闪付对账
 * @author xxm
 * @since 2024/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayReconcileService {

    private final UnionReconcileBillDetailManager unionReconcileBillDetailManager;

    /**
     * 下载对账单
     */
    public void downAndSave(Date date, UnionPayKit unionPayKit){
        // 下载对账单
        Map<String, Object> map = unionPayKit.downloadBill(date, RECONCILE_BILL_TYPE);
        String fileContent = map.get(FILE_CONTENT).toString();
        // 判断是否成功
        if (!SDKConstants.OK_RESP_CODE.equals(map.get(SDKConstants.param_respCode))) {
            log.warn("云闪付获取对账文件失败");
            throw new PayFailureException("云闪付获取对账文件失败");
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
            while ((entry= zipArchiveInputStream.getNextZipEntry()) != null){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipArchiveInputStream,"GBK"));
                if (StrUtil.startWith(entry.getName(), RECONCILE_FILE_PREFIX)){
                    // 明细解析
                    List<String> strings = IoUtil.readLines(bufferedReader, new ArrayList<>());
                    billDetails = this.parseDetail(strings);
                } else {
                    // 汇总目前不进行处理
                }
            }
            // 保存原始对账记录
            this.save(billDetails);
            // 转换为通用对账记录对象
            this.convertAndSave(billDetails);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析上传的对账单
     */
    @SneakyThrows
    public void upload(byte[] bytes){
            // 明细解析
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes),"GBK"));
            List<String> strings = IoUtil.readLines(bufferedReader, new ArrayList<>());
            List<UnionReconcileBillDetail> billDetails = this.parseDetail(strings);
            // 保存原始对账记录
            this.save(billDetails);
            // 转换为通用对账记录对象
            this.convertAndSave(billDetails);
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
        for(int i=0;i<RECONCILE_BILL_SPLIT.length;i++){
            //右侧游标
            int rightIndex = leftIndex + RECONCILE_BILL_SPLIT[i];
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
    private void convertAndSave(List<UnionReconcileBillDetail> billDetails){
        List<ReconcileDetail> collect = billDetails.stream()
                .map(this::convert)
                // 只处理支付和退款的对账记录
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // 写入到上下文中
        PaymentContextLocal.get().getReconcileInfo().setReconcileDetails(collect);
    }

    /**
     * 转换为通用对账记录对象
     */
    private ReconcileDetail convert(UnionReconcileBillDetail billDetail){
        ReconcileOrder reconcileOrder = PaymentContextLocal.get()
                .getReconcileInfo()
                .getReconcileOrder();
        // 金额
        String orderAmount = billDetail.getTxnAmt();
        int amount = Integer.parseInt(orderAmount);

        // 默认为支付对账记录
        ReconcileDetail reconcileDetail = new ReconcileDetail()
                .setTitle("未知")
                .setRecordOrderId(billDetail.getRecordOrderId())
                .setOrderId(billDetail.getOrderId())
                .setType(ReconcileTradeEnum.PAY.getCode())
                .setAmount(amount)
                .setGatewayOrderNo(billDetail.getQueryId());

        // 时间, 从对账订单获取年份
        LocalDate date = reconcileOrder.getDate();
        String year = LocalDateTimeUtil.format(date, DatePattern.NORM_YEAR_PATTERN);

        String txnTime = year + billDetail.getTxnTime();
        if (StrUtil.isNotBlank(txnTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(txnTime, DatePattern.PURE_DATETIME_PATTERN);
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
    private void save(List<UnionReconcileBillDetail> billDetails){
        Long recordOrderId = PaymentContextLocal.get().getReconcileInfo().getReconcileOrder().getId();
        billDetails.forEach(o->o.setRecordOrderId(recordOrderId));
        unionReconcileBillDetailManager.saveAll(billDetails);
    }
}
