package cn.bootx.platform.daxpay.core.channel.voucher.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpDelEntity;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherParam;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xxm
 * @since 2022/3/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class VoucherManager extends BaseManager<VoucherMapper, Voucher> {

    /**
     * 分页
     */
    public Page<Voucher> page(PageParam pageParam, VoucherParam param) {
        Page<Voucher> mpPage = MpUtil.getMpPage(pageParam, Voucher.class);
        return this.lambdaQuery()
            .ge(Objects.nonNull(param.getStartTime()), Voucher::getStartTime, param.getStartTime())
            .le(Objects.nonNull(param.getEndTime()), Voucher::getEndTime, param.getEndTime())
            .eq(Objects.nonNull(param.getEnduring()), Voucher::isEnduring, param.getEnduring())
            .like(StrUtil.isNotBlank(param.getCardNo()), Voucher::getCardNo, param.getCardNo())
            .like(Objects.nonNull(param.getBatchNo()), Voucher::getBatchNo, param.getBatchNo())
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }

    /**
     * 根据卡号查询
     */
    public Optional<Voucher> findByCardNo(String cardNo) {
        return this.findByField(Voucher::getCardNo, cardNo);
    }

    /**
     * 根据卡号查询
     */
    public List<Voucher> findByCardNoList(List<String> cardNos) {
        return this.findAllByFields(Voucher::getCardNo, cardNos);
    }

    /**
     * 更改状态
     */
    public void changeStatus(Long id, String status) {
        Long userIdOrDefaultId = SecurityUtil.getUserIdOrDefaultId();
        this.lambdaUpdate().eq(MpIdEntity::getId, id)
                .set(MpDelEntity::getLastModifiedTime, LocalDateTime.now())
                .set(MpDelEntity::getLastModifier,userIdOrDefaultId)
                .set(Voucher::getStatus, status).update();

    }

    /**
     * 批量更改状态
     */
    public void changeStatusBatch(List<Long> ids, String status) {
        Long userIdOrDefaultId = SecurityUtil.getUserIdOrDefaultId();
        this.lambdaUpdate().in(MpIdEntity::getId, ids)
                .set(MpDelEntity::getLastModifiedTime, LocalDateTime.now())
                .set(MpDelEntity::getLastModifier,userIdOrDefaultId)
                .set(Voucher::getStatus, status).update();
    }

}
