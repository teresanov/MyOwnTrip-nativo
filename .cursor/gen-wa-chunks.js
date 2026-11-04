const fs = require('fs');
const ws = fs.readFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/part-a.ws.js',
  'utf8',
);
const b64 = Buffer.from(ws, 'utf8').toString('base64');
const wa = b64.slice(0, 6000);
const mid = 3000;
const a = wa.slice(0, mid);
const b = wa.slice(mid);
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/wa-chunk-a.js',
  'await figma.clientStorage.setAsync("wa", ' +
    JSON.stringify(a) +
    '); return { chunk: "wa-a", len: ' +
    a.length +
    ' };',
);
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/wa-chunk-b.js',
  'const prev=await figma.clientStorage.getAsync("wa"); await figma.clientStorage.setAsync("wa", prev+' +
    JSON.stringify(b) +
    '); return { chunk: "wa-b", len: (await figma.clientStorage.getAsync("wa")).length };',
);
console.log('wa', wa.length);
