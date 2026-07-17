/**
 * 安全批量重命名：仅替换类型名与退款实体 getter，不触碰 setComplete 等支付 BO 字段
 * 运行: node dax-pay-open/_config/scripts/rename-refund-order.mjs
 */
import fs from 'fs'
import path from 'path'

function walk(dir, acc = []) {
  for (const ent of fs.readdirSync(dir, { withFileTypes: true })) {
    const p = path.join(dir, ent.name)
    if (ent.isDirectory()) {
      if (ent.name === 'target' || ent.name === 'node_modules' || ent.name === '.git') continue
      walk(p, acc)
    } else if (/\.(java|ts|vue)$/.test(ent.name)) {
      acc.push(p)
    }
  }
  return acc
}

const roots = [
  'dax-pay-open/daxpay-payment',
  'dax-pay-open/daxpay-channel',
  'dax-pay-open/daxpay-plugin',
  'dax-pay-ui/apps/daxpay-admin/src',
]

// 长名优先；不碰 PayRefundStatusEnum
const classRepls = [
  ['PayRefundOrderAdminController', 'RefundOrderAdminController'],
  ['PayRefundOrderAdminService', 'RefundOrderAdminService'],
  ['PayRefundOrderConvert', 'RefundOrderConvert'],
  ['PayRefundOrderManager', 'RefundOrderManager'],
  ['PayRefundOrderMapper', 'RefundOrderMapper'],
  ['PayRefundOrderQuery', 'RefundOrderQuery'],
  ['PayRefundOrderResult', 'RefundOrderResult'],
  ['PayRefundSettleService', 'RefundSettleService'],
  ['PayRefundSyncService', 'RefundSyncService'],
  ['PayRefundService', 'RefundService'],
  ['PayRefundParam', 'RefundParam'],
  ['PayRefundOrder', 'RefundOrder'],
]

let changedFiles = 0
let totalRepls = 0

for (const root of roots) {
  if (!fs.existsSync(root)) {
    console.warn('skip missing root', root)
    continue
  }
  for (const file of walk(root)) {
    let text = fs.readFileSync(file, 'utf8')
    const orig = text

    for (const [from, to] of classRepls) {
      if (!text.includes(from)) continue
      const parts = text.split(from)
      totalRepls += parts.length - 1
      text = parts.join(to)
    }

    // 退款实体：orderNo → tradeNo（仅 refundOrder / RefundOrder 方法引用）
    text = text.replaceAll('RefundOrder::getOrderNo', 'RefundOrder::getTradeNo')
    text = text.replaceAll('refundOrder.getOrderNo()', 'refundOrder.getTradeNo()')
    text = text.replaceAll('refundOrder.setOrderNo(', 'refundOrder.setTradeNo(')
    text = text.replaceAll('refund.getOrderNo()', 'refund.getTradeNo()')

    // 上送通道的退款请求号用 relationOrderNo（建单默认 = refundNo）
    text = text.replaceAll(
      'req.setOutRefundNo(refundOrder.getRefundNo())',
      'req.setOutRefundNo(refundOrder.getRelationOrderNo())',
    )
    text = text.replaceAll(
      'req.setOutRequestNo(refundOrder.getRefundNo())',
      'req.setOutRequestNo(refundOrder.getRelationOrderNo())',
    )

    // 前端/入参：RefundParam.orderNo → tradeNo
    if (file.endsWith('.ts') || file.endsWith('.vue')) {
      // 接口字段
      text = text.replace(
        /(\/\*\*[^*]*原支付订单号[^*]*\*\/\s*)orderNo\?:/g,
        '$1tradeNo?:',
      )
      text = text.replace(
        /orderNo: refundForm\.value\.orderNo/g,
        'tradeNo: refundForm.value.tradeNo',
      )
      text = text.replace(
        /orderNo: data\?\.tradeNo/g,
        'tradeNo: data?.tradeNo',
      )
      // 查询条件/列表字段名若仍用 orderNo 表示原支付号，改为 tradeNo
      text = text.replace(
        /field: 'orderNo'/g,
        "field: 'tradeNo'",
      )
    }

    // Java RefundParam：setOrderNo → setTradeNo（易支付等）
    text = text.replaceAll('refundParam.setOrderNo(', 'refundParam.setTradeNo(')
    text = text.replaceAll('param.getOrderNo()', 'param.getTradeNo()')

    if (text !== orig) {
      fs.writeFileSync(file, text, 'utf8')
      changedFiles++
      console.log('updated', file)
    }
  }
}

console.log('done changedFiles=', changedFiles, 'totalRepls=', totalRepls)
