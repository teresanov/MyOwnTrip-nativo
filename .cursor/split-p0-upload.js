const fs = require('fs');
const code = fs.readFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/part-a.ws.js',
  'utf8',
);
const split = 3629;
const end = code.indexOf('function createQrSvgNode');
const parts = [code.slice(0, split), code.slice(split, end)];
parts.forEach((part, i) => {
  const key = 'p0' + (i ? 'b' : 'a');
  const b64 = Buffer.from(part, 'utf8').toString('base64');
  fs.writeFileSync(
    `C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/exec-b64-${key}.js`,
    `await figma.clientStorage.setAsync("${key}", atob(${JSON.stringify(b64)})); return { k: "${key}", n: ${part.length} };`,
  );
  console.log(key, part.length, fs.statSync(`C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/exec-b64-${key}.js`).size);
});
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/b64-run.js',
  "const code=(await figma.clientStorage.getAsync('p0a'))+(await figma.clientStorage.getAsync('p0b'))+(await figma.clientStorage.getAsync('p1'))+(await figma.clientStorage.getAsync('p2'))+(await figma.clientStorage.getAsync('p3')); return await (0, eval)('(async () => {' + code + '})()');",
);
