package cn.daxpay.open.platform.system.service.region;

import cn.daxpay.open.platform.system.dao.region.CityAdjacentManager;
import cn.daxpay.open.platform.system.entity.region.CityAdjacent;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/// # 城市接壤关系服务
///
/// 启动后首次访问时一次性把全量接壤关系载入内存(约千级行, 静态数据), 构建城市编码 → 邻市编码集合的多重映射,
/// 供地理围栏 balanced 策略查询邻市。行政数据稳定, 无需运行期失效。
@Slf4j
@Service
@RequiredArgsConstructor
public class ChinaRegionAdjacencyService {

    private final CityAdjacentManager cityAdjacentManager;

    /// 接壤关系多重映射(惰性加载, 一经加载不再变更)
    private volatile Map<String, Set<String>> adjacencyMap;

    /// 查询指定城市的所有接壤邻市编码
    ///
    /// @param cityCode 城市编码(base_city.code, 4位)
    /// @return 邻市编码集合, 无记录返回空集
    public Set<String> findAdjacentCityCodes(String cityCode) {
        if (StrUtil.isBlank(cityCode)) {
            return Set.of();
        }
        return getAdjacencyMap().getOrDefault(cityCode, Set.of());
    }

    /// 惰性加载全量接壤关系并构建多重映射(双重检查锁定)
    private Map<String, Set<String>> getAdjacencyMap() {
        Map<String, Set<String>> local = this.adjacencyMap;
        if (local != null) {
            return local;
        }
        synchronized (this) {
            if (this.adjacencyMap != null) {
                return this.adjacencyMap;
            }
            long start = System.currentTimeMillis();
            Map<String, Set<String>> map = new HashMap<>();
            for (CityAdjacent row : cityAdjacentManager.findAll()) {
                // 双向插入: 数据源可能只存单方向(a,b), 内存映射同时建 a→b 与 b→a,
                // 使 findAdjacentCityCodes(cityCode) 对任意一端查询都返回完整邻市集合
                map.computeIfAbsent(row.getCityCode(), k -> new HashSet<>()).add(row.getAdjacentCityCode());
                map.computeIfAbsent(row.getAdjacentCityCode(), k -> new HashSet<>()).add(row.getCityCode());
            }
            this.adjacencyMap = map;
            log.info("城市接壤关系载入完成: {} 条邻接记录, {} 个城市, 耗时 {}ms",
                    map.values().stream().mapToInt(Set::size).sum(),
                    map.size(), System.currentTimeMillis() - start);
            return map;
        }
    }
}
