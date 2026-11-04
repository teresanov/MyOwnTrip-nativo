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
parts.forEach((part, i) => {
  const b64 = Buffer.from(part, 'utf8').toString('base64');
  fs.writeFileSync(
    `C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/b64-p${i}.txt`,
    b64,
  );
  fs.writeFileSync(
    `C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/exec-b64-p${i}.js`,
    `await figma.clientStorage.setAsync("p${i}", atob(${JSON.stringify(b64)})); return { p: ${i}, n: ${part.length} };`,
  );
  console.log('p' + i, part.length, 'b64', b64.length);
});
const keys = parts.map((_, i) => 'p' + i);
const join = keys.map((k) => `(await figma.clientStorage.getAsync('${k}'))`).join('+');
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/b64-run.js',
  `const code=${join}; return await (0, eval)('(async () => {' + code + '})()');`,
);
