const fs = require('fs');
const path = require('path');
const root = path.join(__dirname);
const order = [
  'exec-b64-pb0-a.js',
  'exec-b64-pb0-b.js',
  'exec-b64-pb0-merge.js',
  'exec-b64-pb1-a.js',
  'exec-b64-pb1-b.js',
  'exec-b64-pb1-merge.js',
  'exec-b64-pb2-a.js',
  'exec-b64-pb2-b.js',
  'exec-b64-pb2-merge.js',
  'exec-b64-pb3.js',
  'exec-b64-pb4.js',
  'b64-run-part-b.js',
];
for (const f of order) {
  const p = path.join(root, f);
  if (!fs.existsSync(p)) throw new Error('missing ' + f);
  console.log('---', f, fs.statSync(p).size);
  console.log(fs.readFileSync(p, 'utf8').slice(0, 80) + '...');
}
