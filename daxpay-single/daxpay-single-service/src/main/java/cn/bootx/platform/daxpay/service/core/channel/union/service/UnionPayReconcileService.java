package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

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
    }

    /**
     * 转换和保存
     */
    public void convertAndSave(){

    }
}
