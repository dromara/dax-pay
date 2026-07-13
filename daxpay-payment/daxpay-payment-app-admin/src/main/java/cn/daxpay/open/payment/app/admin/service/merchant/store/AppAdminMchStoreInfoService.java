package cn.daxpay.open.payment.app.admin.service.merchant.store;

import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoParam;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoQuery;
import cn.daxpay.open.payment.merchant.result.store.MchStoreInfoResult;
import cn.daxpay.open.payment.merchant.service.store.MchStoreInfoService;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-门店服务
///
/// 转发至 [MchStoreInfoService]
@Service
@RequiredArgsConstructor
public class AppAdminMchStoreInfoService {

    private final MchStoreInfoService mchStoreInfoService;

    /// 新增
    public void add(MchStoreInfoParam param) {
        mchStoreInfoService.add(param);
    }

    /// 修改
    public void update(MchStoreInfoParam param) {
        mchStoreInfoService.update(param);
    }

    /// 分页
    public PageResult<MchStoreInfoResult> page(PageParam pageParam, MchStoreInfoQuery query) {
        return mchStoreInfoService.page(pageParam, query);
    }

    /// 详情
    public MchStoreInfoResult findById(Long id) {
        return mchStoreInfoService.findById(id);
    }

    /// 删除
    public void delete(Long id) {
        mchStoreInfoService.delete(id);
    }
}
