const fs = require('fs');
const path = require('path');
const root = 'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor';
for (const key of ['pb0', 'pb1', 'pb2']) {
  const file = path.join(root, `exec-b64-${key}.js`);
  const code = fs.readFileSync(file, 'utf8');
  const m = code.match(/atob\("([^"]+)"\)/);
  if (!m) throw new Error('no b64 ' + key);
  const b64 = m[1];
  const half = Math.floor(b64.length / 2);
  const a = b64.slice(0, half);
  const b = b64.slice(half);
  fs.writeFileSync(
    path.join(root, `exec-b64-${key}-a.js`),
    `await figma.clientStorage.setAsync("${key}a", ${JSON.stringify(a)}); return { k: "${key}a", n: ${a.length} };`,
  );
  fs.writeFileSync(
    path.join(root, `exec-b64-${key}-b.js`),
    `await figma.clientStorage.setAsync("${key}b", ${JSON.stringify(b)}); return { k: "${key}b", n: ${b.length} };`,
  );
  fs.writeFileSync(
    path.join(root, `exec-b64-${key}-merge.js`),
    `await figma.clientStorage.setAsync("${key}", (await figma.clientStorage.getAsync("${key}a"))+(await figma.clientStorage.getAsync("${key}b"))); return { k: "${key}", merged: true };`,
  );
  console.log(key, b64.length, '->', a.length, b.length);
}
