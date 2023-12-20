package cn.bootx.platform.daxpay.core.channel.voucher.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.VoucherCode;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherLogManager;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherManager;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherImportParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    private final VoucherLogManager voucherLogManager;


    /**
     * 批量导入
     * @param skip 是否跳过已经导入的储值卡,false时将会异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void importBatch(Boolean skip, String mchCode, String mchAppCode,List<VoucherImportParam> voucherImports) {
        List<String> cardNoList = voucherImports.stream()
                .map(VoucherImportParam::getCardNo)
                .distinct()
                .collect(Collectors.toList());
        // 卡号不能重复
        if (voucherImports.size()!=cardNoList.size()){
            throw new BizException("卡号不能重复");
        }
        // 查询库中是否已经有对应的储值卡号
        List<Voucher> vouchersByDB = voucherManager.findByCardNoList(cardNoList);
        // 不跳过已经导入的储值卡且存在数据, 抛出异常
        if (Objects.equals(skip,true)&& CollUtil.isNotEmpty(vouchersByDB)){
            log.warn("数据库中已经存在的卡号:{}",vouchersByDB.stream().map(Voucher::getCardNo).collect(Collectors.toList()));
            throw new BizException("要导入的卡号在数据中已经存在");
        }
        // 导入对应储值卡
        List<String> cardNoListByDb = vouchersByDB.stream()
                .map(Voucher::getCardNo)
                .distinct()
                .collect(Collectors.toList());
        long batchNo = IdUtil.getSnowflakeNextId();
        List<Voucher> vouchers = voucherImports.stream()
                .filter(o -> !cardNoListByDb.contains(o.getCardNo()))
                .map(o -> {
                    Voucher voucher = new Voucher();
                    BeanUtil.copyProperties(o, voucher);
                    return voucher.setMchCode(mchCode)
                            .setMchAppCode(mchAppCode)
                            .setBatchNo(batchNo);
                })
                .collect(Collectors.toList());
        voucherManager.saveAll(vouchers);
        // TODO 记录日志

    }

    /**
     * 启用
     */
    public void unlock(Long id) {
        voucherManager.changeStatus(id, VoucherCode.STATUS_NORMAL);
    }

    /**
     * 冻结
     */
    public void lock(Long id) {
        voucherManager.changeStatus(id, VoucherCode.STATUS_FORBIDDEN);
    }

    /**
     * 批量启用
     */
    public void unlockBatch(List<Long> ids) {
        voucherManager.changeStatusBatch(ids, VoucherCode.STATUS_NORMAL);
    }

    /**
     * 批量冻结
     */
    public void lockBatch(List<Long> ids) {
        voucherManager.changeStatusBatch(ids, VoucherCode.STATUS_FORBIDDEN);
    }


}
