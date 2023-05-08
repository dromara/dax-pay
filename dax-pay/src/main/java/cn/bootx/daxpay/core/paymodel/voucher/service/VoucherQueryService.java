package cn.bootx.daxpay.core.paymodel.voucher.service;

import cn.bootx.common.core.exception.DataNotExistException;
import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.daxpay.core.paymodel.voucher.dao.VoucherManager;
import cn.bootx.daxpay.core.paymodel.voucher.entity.Voucher;
import cn.bootx.daxpay.dto.paymodel.voucher.VoucherDto;
import cn.bootx.daxpay.param.paymodel.voucher.VoucherParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 储值卡查询
 *
 * @author xxm
 * @date 2022/3/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherQueryService {

    private final VoucherManager voucherManager;

    /**
     * 分页
     */
    public PageResult<VoucherDto> page(PageParam pageParam, VoucherParam param) {
        return MpUtil.convert2DtoPageResult(voucherManager.page(pageParam, param));
    }

    /**
     * 根据id查询
     */
    public VoucherDto findById(Long id) {
        return voucherManager.findById(id).map(Voucher::toDto).orElseThrow(() -> new DataNotExistException("储值卡不存在"));
    }

    /**
     * 根据卡号查询
     */
    public VoucherDto findByCardNo(String cardNo) {
        return voucherManager.findByCardNo(cardNo)
            .map(Voucher::toDto)
            .orElseThrow(() -> new DataNotExistException("储值卡不存在"));
    }

}
