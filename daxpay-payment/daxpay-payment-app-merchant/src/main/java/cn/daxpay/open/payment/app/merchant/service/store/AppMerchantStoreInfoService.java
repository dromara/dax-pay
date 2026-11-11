package cn.daxpay.open.payment.app.merchant.service.store;

import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoParam;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoQuery;
import cn.daxpay.open.payment.merchant.result.store.MchStoreInfoResult;
import cn.daxpay.open.payment.merchant.result.store.MchStoreOverviewResult;
import cn.daxpay.open.payment.merchant.service.store.MchStoreInfoService;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /// 门店列表(按当前商户过滤, 不分页; 移动端门店选择与卡片墙用)
    public List<MchStoreInfoResult> list() {
        return mchStoreInfoService.list();
    }

    /// 门店概况聚合(门店工作台: 默认应用/默认终端/绑定计数)
    public MchStoreOverviewResult overview(Long id) {
        return mchStoreInfoService.overview(id);
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

    /// 设为默认门店
    public void setDefault(Long id) {
        mchStoreInfoService.setDefault(id);
    }

    /// 取消默认门店
    public void clearDefault(Long id) {
        mchStoreInfoService.clearDefault(id);
    }

    /// 设置/清空门店默认应用(appId 空=跟随商户默认)
    public void setApp(Long id, String appId) {
        mchStoreInfoService.setApp(id, appId);
    }
}
