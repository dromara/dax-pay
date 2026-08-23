#!/usr/bin/env node
/**
 * table.sql 冗长注释排查脚本
 *
 * 识别两类"枚举展开型"冗长注释(按 2026-08-23 拍板: 注释只留主标题, 枚举取值不塞注释):
 *   A. 冒号枚举型: '标题: code1-说明1 code2-说明2 ...'
 *   B. 括号取值域型: '标题(code1说明1/code2说明2)' 或 '标题(bound已绑定/unbound已解绑)'
 * 不动"括号内是用途/约束说明"的注释(如 '加密存储'/'创建时录入不可修改'/'部分唯一索引')
 *
 * 用法: node find-verbose-comments.mjs <table.sql>
 */

import { createReadStream } from 'node:fs'
import { createInterface } from 'node:readline'
import { argv, exit } from 'node:process'

const inputPath = argv[2]
if (!inputPath) {
  console.error('用法：node find-verbose-comments.mjs <table.sql>')
  exit(1)
}

// 冒号枚举型: 冒号后出现 "标识符-中文" 或 "标识符 中文" 连续对
const COLON_ENUM_RE = /^([^:'(]+):\s*\w+-\S+/       // 标题: code-说明 ...
// 括号取值域型: 括号内含 标识符紧跟中文 的枚举对(斜杠/空格分隔), 如 (USER_ID用户号/LOGIN_NAME登录账号)
const PAREN_ENUM_RE = /\(([a-zA-Z_][a-zA-Z0-9_]*[\u4e00-\u9fff][^()]*(\/[a-zA-Z_][a-zA-Z0-9_]*[\u4e00-\u9fff][^()]*)+)\)/
// 括号取值域型变体: code中文 说明 连续多对无斜杠, 如 (bound已绑定/unbound已解绑/fail绑定失败) 已覆盖; (mini_program小程序 移动应用) 少见, 保守只认斜杠型

const rl = createInterface({ input: createReadStream(inputPath, 'utf8'), crlfDelay: Infinity })

const colonHits = []
const parenHits = []
rl.on('line', (line) => {
  const m = line.match(/^COMMENT ON (TABLE|COLUMN|INDEX) "public"\."([^"]+)"(?:\."([^"]+)")? IS '(.*)';/)
  if (!m) return
  const [, kind, obj, col, text] = m
  const target = col ? `${obj}.${col}` : `${obj}`
  if (COLON_ENUM_RE.test(text)) colonHits.push([target, kind, text])
  else if (PAREN_ENUM_RE.test(text)) parenHits.push([target, kind, text])
})

rl.on('close', () => {
  console.log(`===== A. 冒号枚举型 ${colonHits.length} 条 =====`)
  for (const [t, k, s] of colonHits) console.log(`  ${t}  [${k}]  '${s}'`)
  console.log(`\n===== B. 括号取值域型 ${parenHits.length} 条 =====`)
  for (const [t, k, s] of parenHits) console.log(`  ${t}  [${k}]  '${s}'`)
})
