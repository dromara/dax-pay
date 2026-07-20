-- pay_trade 新增 title 列: 冗余自容器(业务订单), 资金列表/工作台免 JOIN 容器即可展示订单标题
ALTER TABLE pay_trade ADD COLUMN title VARCHAR(255);
COMMENT ON COLUMN pay_trade.title IS '订单标题(冗余自容器, 资金列表免JOIN; 权威在容器)';
