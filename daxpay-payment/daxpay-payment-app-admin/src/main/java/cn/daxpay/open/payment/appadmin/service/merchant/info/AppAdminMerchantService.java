package cn.daxpay.open.payment.appadmin.service.merchant.info;

import cn.daxpay.open.payment.admin.service.merchant.info.MerchantAdminService;
import cn.daxpay.open.payment.merchant.param.info.MerchantInfoParam;
import cn.daxpay.open.payment.merchant.param.info.MerchantInfoQuery;
import cn.daxpay.open.payment.merchant.param.info.MerchantRegisterParam;
import cn.daxpay.open.payment.merchant.result.info.MerchantInfoResult;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 运营移动端-商户服务
///
/// 转发至 [MerchantAdminService]
@Service
@RequiredArgsConstructor
public class AppAdminMerchantService {

    private final MerchantAdminService merchantAdminService;

    /// 新增商户
    public void add(MerchantRegisterParam param) {
        merchantAdminService.add(param);
    }

    /// 修改商户
    public void update(MerchantInfoParam param) {
        merchantAdminService.update(param);
    }

    /// 商户分页
    public PageResult<MerchantInfoResult> page(PageParam pageParam, MerchantInfoQuery param) {
        return merchantAdminService.page(pageParam, param);
    }

    /// 根据 id 查询
    public MerchantInfoResult findById(Long id) {
        return merchantAdminService.findById(id);
    }

    /// 根据商户号查询
    public MerchantInfoResult findByMchNo(String mchNo) {
        return merchantAdminService.findByMchNo(mchNo);
    }

    /// 删除商户
    public void delete(Long id) {
        merchantAdminService.delete(id);
    }

    /// 启用商户
    public void enable(Long id) {
        merchantAdminService.enable(id);
    }

    /// 禁用商户
    public void disable(Long id) {
        merchantAdminService.disable(id);
    }

    /// 商户下拉
    public List<LabelValue> dropdown() {
        return merchantAdminService.dropdown();
    }
}
