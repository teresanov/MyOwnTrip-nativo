const fs = require('fs');
let c = fs.readFileSync(
  'C:/Users/Usuario/AndroidProjects/MOT_nativo/scripts/figma-design-file-wallet-flows-parity.js',
  'utf8',
);
c = c.replace(/\/\*[\s\S]*?\*\//g, '');
c = c.replace(/\/\/.*$/gm, '');
c = c.replace(/\s+/g, ' ');
fs.writeFileSync('C:/Users/Usuario/AndroidProjects/MOT_nativo/.cursor/wallet-parity.min.js', c);
console.log(c.length);
