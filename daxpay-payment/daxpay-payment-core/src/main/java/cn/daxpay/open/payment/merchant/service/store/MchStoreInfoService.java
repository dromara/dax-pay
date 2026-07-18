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
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.enums.merchant.StoreStatusEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    @Transactional(rollbackFor = Exception.class)
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
        // 首店或显式 defaultStore: 设为默认(同商户仅一个)
        boolean firstStore = !mchStoreInfoManager.existsByMchNo(mchNo);
        boolean wantDefault = firstStore || param.isDefaultStore();
        entity.setDefaultStore(false);
        mchStoreInfoManager.save(entity);
        if (wantDefault) {
            this.setDefault(entity.getId());
        }
    }

    /// 为商户创建默认门店（启用 + defaultStore=true；名称按当前请求语言生成）
    ///
    /// @param mchNo         商户号
    /// @param mchName       商户名称（用于生成默认门店名）
    /// @param contactPhone  联系电话（可空）
    public void createDefaultStore(String mchNo, String mchName, String contactPhone) {
        MchStoreInfo store = new MchStoreInfo();
        // 默认门店名称（payment.merchant.defaultStoreName，{0}=商户名）
        store.setStoreName(I18nUtil.get("payment.merchant.defaultStoreName", mchName));
        store.setContactPhone(contactPhone);
        store.setStatus(StoreStatusEnum.ENABLE.getCode());
        store.setDefaultStore(true);
        store.setStoreNo(this.generateStoreNo());
        // 运营端创建无 PaymentContext，必须显式写 mchNo
        store.setMchNo(mchNo);
        mchStoreInfoManager.save(store);
    }

    /// 修改门店
    @Transactional(rollbackFor = Exception.class)
    public void update(MchStoreInfoParam param) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(param.getId())
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        boolean wasDefault = mchStore.isDefaultStore();
        MchStoreInfoConvert.CONVERT.copy(param, mchStore);
        // 默认标记不直接 copy 覆盖, 走 set/clear 保证同商户唯一
        mchStore.setDefaultStore(wasDefault);
        mchStoreInfoManager.updateById(mchStore);
        if (!Objects.equals(wasDefault, param.isDefaultStore())) {
            if (param.isDefaultStore()) {
                this.setDefault(mchStore.getId());
            } else {
                this.clearDefault(mchStore.getId());
            }
        }
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
    ///
    /// 默认门店禁止删除(与 [MchAppInfoService#delete] 对齐), 需先调用 [setDefault] 转交默认标记。
    public void delete(Long id) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(id)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        if (mchStore.isDefaultStore()) {
            // 商户: 默认门店不可删除, 请先转交默认标记
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.defaultStoreCannotDelete");
        }
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

    /// 设为默认门店(同商户先清后设)
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(id)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        mchStoreInfoManager.clearDefault(mchStore.getMchNo());
        // clearDefault 后重新加载, 避免 version 冲突
        mchStore = mchStoreInfoManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        mchStore.setDefaultStore(true);
        mchStoreInfoManager.updateById(mchStore);
    }

    /// 取消默认门店
    @Transactional(rollbackFor = Exception.class)
    public void clearDefault(Long id) {
        MchStoreInfo mchStore = mchStoreInfoManager.findById(id)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        this.checkStore(mchStore);
        mchStore.setDefaultStore(false);
        mchStoreInfoManager.updateById(mchStore);
    }

    /// 下单校验门店: 空则跳过; 非空须存在、归属商户、启用
    ///
    /// @param storeNo 门店号(可空)
    /// @param mchNo   当前下单商户号
    public void validateStoreForPay(String storeNo, String mchNo) {
        if (StrUtil.isBlank(storeNo)) {
            return;
        }
        MchStoreInfo store = mchStoreInfoManager.findByStoreNo(storeNo)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        if (StrUtil.isNotBlank(mchNo) && !Objects.equals(store.getMchNo(), mchNo)) {
            // 商户: 门店不属于当前商户
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.merchant.storeNoMatch");
        }
        if (!Objects.equals(StoreStatusEnum.ENABLE.getCode(), store.getStatus())) {
            // 商户: 门店已停用
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.merchant.storeDisabled");
        }
    }

    /// 解析门店号: 非空原样返回; 空则取商户默认门店。
    ///
    /// 与 [cn.daxpay.open.payment.common.context.MerchantContextLoader#resolveApp] 对齐,
    /// 默认门店不存在或已停用时**抛异常阻断下单**(替代早期返回 null 的宽松策略),
    /// 保证订单 storeNo 维度完整。
    ///
    /// 异常分类(便于排查):
    /// - 未传 storeNo 且无默认门店 → `error.payment.merchant.defaultStoreNotConfigured`
    /// - 未传 storeNo 且默认门店已停用 → `error.payment.merchant.defaultStoreDisabled`
    ///
    /// @param mchNo   商户号
    /// @param storeNo 显式门店号(可空)
    /// @return 解析后的门店号
    public String resolveStoreNo(String mchNo, String storeNo) {
        if (StrUtil.isNotBlank(storeNo)) {
            return storeNo;
        }
        if (StrUtil.isBlank(mchNo)) {
            // 商户: 商户未配置默认门店
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.defaultStoreNotConfigured");
        }
        MchStoreInfo defaultStore = mchStoreInfoManager.findDefaultByMchNo(mchNo)
                // 商户: 商户未配置默认门店
                .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.defaultStoreNotConfigured"));
        if (!Objects.equals(StoreStatusEnum.ENABLE.getCode(), defaultStore.getStatus())) {
            // 商户: 默认门店已停用, 请先启用或转交默认标记
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.defaultStoreDisabled");
        }
        return defaultStore.getStoreNo();
    }
}
