const fs = require('fs');
const path = require('path');
const root = path.join(__dirname, '..', '.cursor');
const outDir = __dirname;

const files = [
  ['exec-b64-p3.js', 'p3', 1600],
  ['exec-b64-p4.js', 'p4', 1600],
  ['exec-b64-p5.js', 'p5', 1494],
];

for (const [name, key, n] of files) {
  const s = fs.readFileSync(path.join(root, name), 'utf8');
  const m = s.match(/atob\("([^"]+)"\)/);
  if (!m) throw new Error('no atob in ' + name);
  const code = `await figma.clientStorage.setAsync("${key}", atob(${JSON.stringify(m[1])})); return { p: ${key.slice(1)}, n: ${n} };`;
  fs.writeFileSync(path.join(outDir, `_tmp-${key}-code.txt`), code);
  console.log(name, code.length);
}

const b64run = fs.readFileSync(path.join(root, 'b64-run.js'), 'utf8').trim();
fs.writeFileSync(path.join(outDir, '_tmp-b64run-code.txt'), b64run);
console.log('b64-run.js', b64run.length);
