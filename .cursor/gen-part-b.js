const fs = require('fs');
const path = require('path');
const root = path.join(__dirname, '..');
let c = fs.readFileSync(
  path.join(root, 'scripts/figma-design-file-wallet-flows-parity.js'),
  'utf8',
);
c = c.replace(/^\/\*\*[\s\S]*?\*\/\s*/, '');
const tail = `
await figma.loadAllPagesAsync();
const page = figma.root.children.find((p) => p.id === '491:14473');
if (!page) throw new Error('04 · Wallet page missing');
await figma.setCurrentPageAsync(page);
const flow = page.findOne((n) => /Wallet.*flujo de app/.test(n.name));
if (!flow) throw new Error('Wallet flow missing');
const screens = flow.findOne((n) => n.name === 'Screens');
if (!screens) throw new Error('Screens missing');
const cap10b = ensureCap(
  screens,
  'cap 10b — detalle hotel',
  'cap 10b · detalle hotel\\npreview: previewWalletEntries HOTEL + previewHotelImportState · hotel-casa-bonay-reserva.pdf',
);
const phone10b = cap10b.findOne((n) => n.name === 'phone');
await rebuildDetailPhone(phone10b.id, SCENARIOS.hotelDetail);
const cap10c = ensureCap(
  screens,
  'cap 10c — detalle actividad · drift',
  'cap 10c · Teatro Nacional D. Maria II\\npreview: DebugPastTripsSeeder · plan chip drift · teatro-real-las-bodas-de-figaro.pdf',
);
const phone10c = cap10c.findOne((n) => n.name === 'phone');
await rebuildDetailPhone(phone10c.id, SCENARIOS.teatroDetail);
const cap13 = ensureCap(
  screens,
  'cap 13 — visor PDF',
  'cap 13 · visor PDF\\npreview: DocumentViewerScreen · billete-barcelona.pdf · route: document viewer',
);
const phone13 = cap13.findOne((n) => n.name === 'phone');
await rebuildPdfViewerPhone(phone13.id);
figma.viewport.scrollAndZoomIntoView([cap10b, cap10c, cap13]);
figma.notify('Wallet parity part B OK');
return { part: 'B', updated: [phone10b.id, phone10c.id, phone13.id] };
`;
const idx = c.indexOf('await figma.loadAllPagesAsync');
const body = c.slice(0, idx) + tail;
const outPath = path.join(root, 'scripts/figma-design-file-wallet-flows-parity-part-b.js');
fs.writeFileSync(
  outPath,
  '/**\n * Wallet design-file — Part B: caps 10b, 10c, 13.\n */\n' + body,
);
const wsPath = path.join(__dirname, 'part-b.ws.js');
fs.writeFileSync(wsPath, body);
console.log('bytes', body.length, '->', outPath);
