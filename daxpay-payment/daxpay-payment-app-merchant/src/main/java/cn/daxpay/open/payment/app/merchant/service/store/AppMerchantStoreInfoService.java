package cn.daxpay.open.payment.app.merchant.service.store;

import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoParam;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoQuery;
import cn.daxpay.open.payment.merchant.result.store.MchStoreInfoResult;
import cn.daxpay.open.payment.merchant.service.store.MchStoreInfoService;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-门店信息服务
///
/// 转发至 core [MchStoreInfoService]
@Service
@RequiredArgsConstructor
public class AppMerchantStoreInfoService {

    private final MchStoreInfoService mchStoreInfoService;

    /// 门店分页
    public PageResult<MchStoreInfoResult> page(PageParam pageParam, MchStoreInfoQuery query) {
        return mchStoreInfoService.page(pageParam, query);
    }

    /// 根据id查询门店
    public MchStoreInfoResult findById(Long id) {
        return mchStoreInfoService.findById(id);
    }

    /// 新增门店
    public void add(MchStoreInfoParam param) {
        mchStoreInfoService.add(param);
    }

    /// 修改门店
    public void update(MchStoreInfoParam param) {
        mchStoreInfoService.update(param);
    }

    /// 删除门店
    public void delete(Long id) {
        mchStoreInfoService.delete(id);
    }
}
