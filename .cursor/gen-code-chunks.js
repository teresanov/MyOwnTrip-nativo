const fs = require('fs');
const code = fs.readFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/part-a.ws.js',
  'utf8',
);
const size = 3200;
const keys = [];
let i = 0;
for (let o = 0; o < code.length; o += size) {
  const part = code.slice(o, o + size);
  const key = 'c' + ++i;
  keys.push(key);
  fs.writeFileSync(
    `C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/code-chunk-${key}.js`,
    `await figma.clientStorage.setAsync("${key}", ${JSON.stringify(part)}); return { key: "${key}", len: ${part.length} };`,
  );
}
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/code-chunk-run.js',
  `const code=${keys.map((k) => `(await figma.clientStorage.getAsync('${k}'))`).join('+')}; return await (0, eval)('(async () => {' + code + '})()');`,
);
console.log('chunks', i, 'total', code.length);
