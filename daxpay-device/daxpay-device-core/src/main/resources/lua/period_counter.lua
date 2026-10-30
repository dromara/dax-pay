-- 周期计数原子脚本: INCR/INCRBY + 仅首次创建时设置 TTL
-- KEYS[1] 计数key; ARGV[1] 增量delta; ARGV[2] TTL秒
-- 返回自增后的计数值
local current = redis.call('INCRBY', KEYS[1], ARGV[1])
-- INCRBY 从 0 起步, 结果等于增量说明本次调用创建了 key, 此时才设置 TTL;
-- 后续自增不重置 TTL, 保证窗口兜底清理时间从首次写入起算
if current == tonumber(ARGV[1]) then
    redis.call('EXPIRE', KEYS[1], ARGV[2])
end
return current
