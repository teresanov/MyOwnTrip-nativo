const fs = require('fs');
const path = require('path');

const root = path.join(__dirname, '..', '.cursor');
const out = path.join(__dirname, '_chunk-uploads');
fs.mkdirSync(out, { recursive: true });

const CHUNK = 480;

function innerB64(file) {
  const s = fs.readFileSync(path.join(root, file), 'utf8');
  const m = s.match(/atob\("([^"]+)"\)/);
  if (!m) throw new Error('no atob: ' + file);
  return m[1];
}

function writeParts(key, b64) {
  const parts = [];
  for (let i = 0; i < b64.length; i += CHUNK) {
    parts.push(b64.slice(i, i + CHUNK));
  }
  parts.forEach((part, i) => {
    const code = `await figma.clientStorage.setAsync(${JSON.stringify(`${key}_${i}`)}, ${JSON.stringify(part)}); return { ${JSON.stringify(key)}: ${i}, len: ${part.length} };`;
    fs.writeFileSync(path.join(out, `${key}-${i}.js`), code);
  });
  const assemble = `const parts=[]; let i=0; while(true){ const p=await figma.clientStorage.getAsync(${JSON.stringify(key + '_')} + i); if(!p) break; parts.push(p); i++; } const b64=parts.join(''); await figma.clientStorage.setAsync(${JSON.stringify(key)}, atob(b64)); return { ${JSON.stringify(key)}: 'assembled', parts: i, b64Len: b64.length };`;
  fs.writeFileSync(path.join(out, `${key}-assemble.js`), assemble);
  console.log(key, parts.length, 'parts');
}

for (const f of ['exec-b64-p3.js', 'exec-b64-p4.js', 'exec-b64-p5.js']) {
  const key = 'p' + f.match(/p(\d)/)[1];
  writeParts(key, innerB64(f));
}
