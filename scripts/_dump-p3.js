const fs = require('fs');
const path = require('path');
const f = fs.readFileSync(path.join(__dirname, '..', '.cursor', 'exec-b64-p3.js'), 'utf8');
const m = f.match(/atob\("([^"]+)"\)/);
const p3 = Buffer.from(m[1], 'base64').toString('utf8');
console.log(JSON.stringify({ len: p3.length, end: p3.slice(-50) }));
