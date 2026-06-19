package cn.daxpay.open.platform.system.dao.region;

import cn.daxpay.open.platform.system.entity.region.Street;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 街道表
///
@Repository
@RequiredArgsConstructor
public class StreetManager extends BaseManager<StreetMapper, Street> {

    /// 根据区县编码查询所有街道
    ///
    /// @param areaCode 区县编码
    /// @return 街道列表
    public List<Street> findAllByAreaCode(String areaCode) {
        return findAllByField(Street::getAreaCode, areaCode);
    }

}


