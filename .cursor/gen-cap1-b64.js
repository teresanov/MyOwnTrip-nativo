const fs = require('fs');
const path = require('path');

const root = path.join(__dirname, '..');
let code = fs.readFileSync(
  path.join(root, 'scripts/figma-design-file-wallet-cap1.js'),
  'utf8',
);
code = code.replace(/^\/\*\*[\s\S]*?\*\/\s*/, '');

const chunkSize = 3500;
const chunks = [];
for (let i = 0; i < code.length; i += chunkSize) {
  chunks.push(code.slice(i, i + chunkSize));
}

const dir = __dirname;
chunks.forEach((chunk, i) => {
  const escaped = JSON.stringify(chunk);
  const js = `await figma.clientStorage.setAsync('cap1_${i}', ${escaped});\nreturn { chunk: ${i}, len: ${chunk.length} };`;
  fs.writeFileSync(path.join(dir, `exec-cap1-store-${i}.js`), js);
});

const keys = chunks.map((_, i) => `'cap1_${i}'`).join(', ');
const run = `
const parts = [${keys}];
let code = '';
for (const k of parts) code += await figma.clientStorage.getAsync(k);
return await (0, eval)('(async () => {' + code + '})()');
`;
fs.writeFileSync(path.join(dir, 'exec-cap1-run.js'), run.trim());
console.log('chunks', chunks.length, 'total', code.length);
