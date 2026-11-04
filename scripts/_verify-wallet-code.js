const fs = require('fs');
const path = require('path');
const root = path.join(__dirname, '..', '.cursor');
let code = '';
const parts = [];
for (let i = 0; i <= 5; i++) {
  const f = fs.readFileSync(path.join(root, `exec-b64-p${i}.js`), 'utf8');
  const m = f.match(/atob\("([^"]+)"\)/);
  const chunk = Buffer.from(m[1], 'base64').toString('utf8');
  parts.push(chunk);
  code += chunk;
}
console.log('part lens', parts.map((p) => p.length).join(','));
console.log('len', code.length);
console.log('createQrSvgNode', code.includes('function createQrSvgNode'));
console.log('createWalletQRCard', code.includes('function createWalletQRCard'));
try {
  // eslint-disable-next-line no-new-func
  new Function(`return (async ()=>{${code}})()`);
  console.log('parse: ok');
} catch (e) {
  console.log('parse: fail', e.message);
}
