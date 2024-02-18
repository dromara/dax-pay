package cn.bootx.platform.daxpay.service.core.channel.voucher.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.service.code.VoucherCode;
import cn.bootx.platform.daxpay.service.core.channel.voucher.convert.VoucherConvert;
import cn.bootx.platform.daxpay.service.core.channel.voucher.dao.VoucherManager;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.service.param.channel.voucher.VoucherBatchImportParam;
import cn.bootx.platform.daxpay.service.param.channel.voucher.VoucherImportParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 储值卡
 *
 * @author xxm
 * @since 2022/3/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherManager voucherManager;

    /**
     * 导入
     */
    @Transactional(rollbackFor = Exception.class)
    public void voucherImport(VoucherImportParam param){
        Voucher voucher = VoucherConvert.CONVERT.convert(param);
        // 判断重复
        if (voucherManager.existsByCardNo(param.getCardNo())) {
            throw new BizException("储值卡已存在");
        }
        voucherManager.save(voucher);
    }

    /**
     * 批量导入
     */
    @Transactional(rollbackFor = Exception.class)
    public void voucherBatchImport(VoucherBatchImportParam param){
        List<Voucher> voucherList = param.cardNoList.stream()
                .map(cardNo -> VoucherConvert.CONVERT.convert(param)
                        .setCardNo(cardNo))
                .collect(Collectors.toList());
        voucherManager.saveAll(voucherList);
    }

    /**
     * 启用
     */
    public void unlock(Long id) {
        voucherManager.findById(id).orElseThrow(() -> new BizException("储值卡不存在"));
        voucherManager.changeStatus(id, VoucherCode.STATUS_NORMAL);
    }

    /**
     * 冻结
     */
    public void lock(Long id) {
        voucherManager.changeStatus(id, VoucherCode.STATUS_FORBIDDEN);
    }

}
