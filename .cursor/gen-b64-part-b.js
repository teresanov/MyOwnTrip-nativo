const fs = require('fs');
const code = fs.readFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/part-b.ws.js',
  'utf8',
);
const splitPoints = [
  code.indexOf('function createQrSvgNode'),
  code.indexOf('async function rebuildDetailPhone'),
  code.indexOf('function ensureCap'),
  code.indexOf('await figma.loadAllPagesAsync'),
].filter((n) => n > 0);
const parts = [];
let start = 0;
for (const end of splitPoints) {
  parts.push(code.slice(start, end));
  start = end;
}
parts.push(code.slice(start));
const prefix = 'pb';
parts.forEach((part, i) => {
  const b64 = Buffer.from(part, 'utf8').toString('base64');
  const key = `${prefix}${i}`;
  fs.writeFileSync(
    `C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/exec-b64-${key}.js`,
    `await figma.clientStorage.setAsync("${key}", atob(${JSON.stringify(b64)})); return { k: "${key}", n: ${part.length} };`,
  );
  console.log(key, 'part', part.length, 'b64', b64.length);
});
const keys = parts.map((_, i) => prefix + i);
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/b64-run-part-b.js',
  `const code=${keys.map((k) => `(await figma.clientStorage.getAsync('${k}'))`).join('+')}; return await (0, eval)('(async () => {' + code + '})()');`,
);
console.log('parts', parts.length);
