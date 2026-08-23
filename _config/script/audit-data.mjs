#!/usr/bin/env node
/**
 * data.sql 脱敏产物审计脚本(敏感遗漏 + 数据完整性)
 *
 * 检查维度:
 *   1. 行数对比: 脱敏后保留表 vs 原始导出, 种子表应全量一致, 行级保留表应为 1 行
 *   2. 敏感模式深扫: bcrypt/v1:加密串/JWT/base64/PEM/邮箱/IPv4/身份证/secret类/URL
 *   3. bootx 超管行完整性: id=1 两行必须存在
 *
 * 用法: node audit-data.mjs <脱敏后data.sql> <原始导出.sql>
 */

import { readFileSync } from 'node:fs'
import { argv, exit } from 'node:process'

const [redactedPath, rawPath] = argv.slice(2)
if (!redactedPath || !rawPath) {
  console.error('用法：node audit-data.mjs <脱敏后data.sql> <原始导出.sql>')
  exit(1)
}
const redacted = readFileSync(redactedPath, 'utf8')
const raw = readFileSync(rawPath, 'utf8')

// ---------- 1. 行数对比 ----------
const countBy = (s) => {
  const m = new Map()
  for (const line of s.split('\n')) {
    const mm = line.match(/^INSERT INTO (?:public\.)?(\w+)\s+VALUES/)
    if (mm) m.set(mm[1], (m.get(mm[1]) ?? 0) + 1)
  }
  return m
}
const rMap = countBy(redacted), rawMap = countBy(raw)
const ROW_LEVEL = new Set(['iam_user_info', 'iam_user_expand_info'])
console.log('===== 1. 保留表行数对比 =====')
let mismatch = 0
for (const [t, n] of [...rMap].sort()) {
  const o = rawMap.get(t)
  const ok = ROW_LEVEL.has(t) ? n === 1 : n === o
  if (!ok) { mismatch++; console.log(`  MISMATCH ${t}: ${n} vs 原始 ${o}`) }
  else console.log(`  OK ${t.padEnd(30)} ${n}${ROW_LEVEL.has(t) ? ` (行级保留, 原始 ${o})` : ''}`)
}
console.log(mismatch === 0 ? '  ==> 全部一致' : `  ==> ${mismatch} 张不一致!`)

// ---------- 2. 敏感模式扫描 ----------
console.log('\n===== 2. 敏感模式扫描 =====')
const patterns = [
  ['bcrypt密钥hash(超管默认密码属预期)', /\$2[aby]\$\d{2}\$[./A-Za-z0-9]{53}/, ''],
  ['v1:加密串', /'v1:[A-Za-z0-9+/=]{20,}'/, ''],
  ['JWT(eyJ)', /eyJ[A-Za-z0-9_-]{15,}\./, ''],
  ['纯base64串>=40(排除路径)', /'([A-Za-z0-9+]{40,}={0,2})'/, ''],
  ['RSA PEM头', /-----BEGIN/, ''],
  ['真实邮箱', /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+\.[a-z]{2,4}/, ''],
  ['IPv4', /'\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}'/, ''],
  ['身份证18位', /'\d{17}[\dXx]'/, ''],
  ['secret/token类值', /(secret|token|password|private_?key)['"]?,?\s*'[A-Za-z0-9+/=_-]{8,}'/i, 'i'],
  ['URL(http)', /'https?:\/\/[^']{8,}'/, ''],
]
let hitsTotal = 0
for (const [name, re, fl] of patterns) {
  const hits = redacted.match(new RegExp(re.source, fl + 'g')) ?? []
  hitsTotal += hits.length
  console.log(`  ${name.padEnd(30)} ${hits.length} 处${hits.length ? '  样例: ' + hits.slice(0, 2).join(' | ').slice(0, 90) : ''}`)
}

// ---------- 3. bootx 超管行 ----------
console.log('\n===== 3. bootx 超管行 =====')
const bootxOk = ['iam_user_info', 'iam_user_expand_info'].every(t =>
  redacted.split('\n').some(l => new RegExp(`^INSERT INTO (?:public\\.)?${t} VALUES \\(1,`).test(l)))
console.log(`  ${bootxOk ? 'OK 两表 id=1 均在' : 'MISSING bootx 行缺失!'}`)
console.log('\n总结: ' + (mismatch === 0 && bootxOk ? '结构完整' : '存在异常') + ', 敏感命中 ' + hitsTotal + ' 处(bcrypt 超管默认密码 1 处属交付预期)')
