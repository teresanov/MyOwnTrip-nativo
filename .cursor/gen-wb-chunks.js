const fs = require('fs');
const ws = fs.readFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/part-a.ws.js',
  'utf8',
);
const b64 = Buffer.from(ws, 'utf8').toString('base64');
const wb = b64.slice(6000);
const mid = Math.ceil(wb.length / 2);
const a = wb.slice(0, mid);
const b = wb.slice(mid);
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/wb-chunk-a.js',
  'await figma.clientStorage.setAsync("wb", ' +
    JSON.stringify(a) +
    '); return { chunk: "a", len: ' +
    a.length +
    ' };',
);
fs.writeFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/wb-chunk-b.js',
  'const prev=await figma.clientStorage.getAsync("wb"); await figma.clientStorage.setAsync("wb", prev+' +
    JSON.stringify(b) +
    '); return { chunk: "b", len: (await figma.clientStorage.getAsync("wb")).length };',
);
console.log('wb', wb.length, 'a', a.length, 'b', b.length);
