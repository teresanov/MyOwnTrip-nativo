const fs = require('fs');
const s = fs.readFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/exec-s1.js',
  'utf8',
);
const start = s.indexOf('"wa", "') + 7;
const end = s.lastIndexOf('"); return');
const b64 = s.slice(start, end);
console.log('b64 len', b64.length, 'file len', s.length);
