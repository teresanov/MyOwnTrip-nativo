const fs = require('fs');
const b64 = fs.readFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/wallet-parity.min.b64',
  'utf8',
);
const mid = Math.floor(b64.length / 2);
const p1 = b64.slice(0, mid);
const p2 = b64.slice(mid);
const c1 =
  'await figma.clientStorage.setAsync("w_p1", ' + JSON.stringify(p1) + '); return { step: 1 };';
const c2 =
  'await figma.clientStorage.setAsync("w_p2", ' + JSON.stringify(p2) + '); return { step: 2 };';
const c3 =
  'const b64=(await figma.clientStorage.getAsync("w_p1"))+(await figma.clientStorage.getAsync("w_p2")); ' +
  'const fn=new (Object.getPrototypeOf(async function(){}).constructor)("figma", atob(b64)); ' +
  'return await fn(figma);';
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/figma-exec-step1.js',
  c1,
);
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/figma-exec-step2.js',
  c2,
);
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/figma-exec-step3.js',
  c3,
);
console.log(c1.length, c2.length, c3.length);
