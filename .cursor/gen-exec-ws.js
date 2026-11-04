const fs = require('fs');
const ws = fs.readFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/part-a.ws.js',
  'utf8',
);
const b64 = Buffer.from(ws, 'utf8').toString('base64');
const mid = 6000;
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/exec-s1.js',
  'await figma.clientStorage.setAsync("wa", ' +
    JSON.stringify(b64.slice(0, mid)) +
    '); return { step: 1 };',
);
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/exec-s2.js',
  'await figma.clientStorage.setAsync("wb", ' +
    JSON.stringify(b64.slice(mid)) +
    '); return { step: 2 };',
);
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/exec-s3.js',
  'const b64=(await figma.clientStorage.getAsync("wa"))+(await figma.clientStorage.getAsync("wb")); ' +
    'const code=atob(b64); ' +
    'const fn=new (Object.getPrototypeOf(async function(){}).constructor)("figma", code); ' +
    'return await fn(figma);',
);
console.log(
  fs.statSync('C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/exec-s1.js').size,
  fs.statSync('C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/exec-s2.js').size,
  b64.length,
);
