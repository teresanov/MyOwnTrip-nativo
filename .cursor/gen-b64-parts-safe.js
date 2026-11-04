const fs = require('fs');
const code = fs.readFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/part-a.ws.js',
  'utf8',
);
const splitPoints = [
  code.indexOf('function createQrSvgNode'),
  code.indexOf('async function rebuildPhoneShell'),
  code.indexOf('await figma.loadAllPagesAsync'),
].filter((n) => n > 0);
const parts = [];
let start = 0;
for (const end of splitPoints) {
  parts.push(code.slice(start, end));
  start = end;
}
parts.push(code.slice(start));
parts.forEach((part, i) => {
  const b64 = Buffer.from(part, 'utf8').toString('base64');
  fs.writeFileSync(
    `C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/exec-b64-p${i}.js`,
    `await figma.clientStorage.setAsync("p${i}", atob(${JSON.stringify(b64)})); return { p: ${i}, n: ${part.length} };`,
  );
  console.log('p' + i, part.length, 'b64', b64.length);
});
const keys = parts.map((_, i) => 'p' + i);
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/b64-run.js',
  `const code=${keys.map((k) => `(await figma.clientStorage.getAsync('${k}'))`).join('+')}; return await (0, eval)('(async () => {' + code + '})()');`,
);
