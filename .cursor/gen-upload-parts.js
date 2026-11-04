const fs = require('fs');
const code = fs.readFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/part-a.ws.js',
  'utf8',
);
const size = 1600;
const parts = [];
for (let o = 0; o < code.length; o += size) {
  parts.push(code.slice(o, o + size));
}
const keys = parts.map((_, i) => 'p' + i);
parts.forEach((part, i) => {
  fs.writeFileSync(
    `C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/upload-${keys[i]}.txt`,
    `await figma.clientStorage.setAsync("${keys[i]}", ${JSON.stringify(part)}); return { k: "${keys[i]}", n: ${part.length} };`,
  );
});
const join = keys.map((k) => `(await figma.clientStorage.getAsync('${k}'))`).join('+');
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/upload-final.txt',
  `const code=${join}; return await (0, eval)('(async () => {' + code + '})()');`,
);
console.log('parts', parts.length, 'sizes', parts.map((p) => p.length).join(','));
