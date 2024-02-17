package cn.bootx.platform.daxpay.service.core.channel.voucher.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpDelEntity;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.service.param.channel.voucher.VoucherQuery;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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
    public Page<Voucher> page(PageParam pageParam, VoucherQuery query) {
        QueryWrapper<Voucher> generator = QueryGenerator.generator(query);
        Page<Voucher> mpPage = MpUtil.getMpPage(pageParam, Voucher.class);
        return this.page(mpPage, generator);
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
        this.lambdaUpdate()
                .eq(MpIdEntity::getId, id)
                .set(MpDelEntity::getLastModifiedTime, LocalDateTime.now())
                .set(MpDelEntity::getLastModifier, userIdOrDefaultId)
                .set(Voucher::getStatus, status)
                .update();

    }

    /**
     * 批量更改状态
     */
    public void changeStatusBatch(List<Long> ids, String status) {
        Long userIdOrDefaultId = SecurityUtil.getUserIdOrDefaultId();
        this.lambdaUpdate().in(MpIdEntity::getId, ids)
                .set(MpDelEntity::getLastModifiedTime, LocalDateTime.now())
                .set(MpDelEntity::getLastModifier,userIdOrDefaultId)
                .set(Voucher::getStatus, status)
                .update();
    }

}
