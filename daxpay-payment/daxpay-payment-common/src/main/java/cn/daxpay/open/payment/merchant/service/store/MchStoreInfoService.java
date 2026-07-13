package cn.daxpay.open.payment.merchant.service.store;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.convert.store.MchStoreInfoConvert;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoParam;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoQuery;
import cn.daxpay.open.payment.merchant.result.store.MchStoreInfoResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/// # 门店信息管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MchStoreInfoService {

    private final MchStoreInfoManager mchStoreInfoManager;

    private final MerchantInfoManager merchantInfoManager;

    private final ClientCodeService clientCodeService;

    private final PaymentContext paymentContext;

    /// 新增门店
    public void add(MchStoreInfoParam param) {
        String mchNo = this.resolveMchNo(param.getMchNo());
        MerchantInfo merchant = merchantInfoManager.findByMchNo(mchNo)
                // 商户: 商户不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchNotExist"));
        param.setMchNo(mchNo);
        MchStoreInfo entity = MchStoreInfoConvert.CONVERT.toEntity(param);
        // 生成门店号
        entity.setStoreNo(this.generateStoreNo());
        entity.setMchNo(mchNo);
        mchStoreInfoManager.save(entity);
    }

    /// 修改门店
    @Transactional(rollbackFor = Exception.class)
    public void update(MchStoreInfoParam param) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(param.getId())
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        MchStoreInfoConvert.CONVERT.copy(param, mchStore);
        mchStoreInfoManager.updateById(mchStore);
    }

    /// 分页
    public PageResult<MchStoreInfoResult> page(PageParam pageParam, MchStoreInfoQuery query) {
        return MpUtil.toPageResult(mchStoreInfoManager.page(pageParam, query));
    }

    /// 根据id查询
    public MchStoreInfoResult findById(Long id) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(id)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        return mchStore.toResult();
    }

    /// 门店列表(商户端按当前商户过滤)
    public List<MchStoreInfoResult> list() {
        String mchNo = this.resolveMchNo(null);
        return mchStoreInfoManager.findAllByMchNo(mchNo).stream()
                .map(MchStoreInfo::toResult)
                .collect(Collectors.toList());
    }

    /// 删除
    public void delete(Long id) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(id)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        mchStoreInfoManager.deleteById(id);
    }

    /// 生成门店号
    public String generateStoreNo() {
        String storeNo = "S" + RandomUtil.randomNumbers(16);
        for (int i = 0; i < 10; i++) {
            if (!mchStoreInfoManager.existsByStoreNo(storeNo)) {
                return storeNo;
            }
            storeNo = "S" + RandomUtil.randomNumbers(16);
        }
        // 商户: 门店号生成失败
        throw new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.storeNoGenFailed");
    }

    /// 解析商户号, 商户端从上下文获取, 管理端从参数获取
    private String resolveMchNo(String paramMchNo) {
        if (clientCodeService.getClientCode().equals(ClientEnum.MERCHANT.getCode())) {
            return paymentContext.getMchNo();
        }
        if (paramMchNo == null) {
            // 商户: 数据错误，未发现商户号
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        return paramMchNo;
    }

    /// 如果和当前商户不匹配, 抛出错误(商户端校验)
    public void checkStore(MchStoreInfo mchStore) {
        if (clientCodeService.getClientCode().equals(ClientEnum.MERCHANT.getCode())) {
            if (!mchStore.getMchNo().equals(paymentContext.getMchNo())) {
                // 商户: 门店不属于当前商户
                throw new ConfigErrorException("error.payment.merchant.storeNoMatch");
            }
        }
    }
}
