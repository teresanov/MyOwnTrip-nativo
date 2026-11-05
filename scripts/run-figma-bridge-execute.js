#!/usr/bin/env node
/**
 * Ejecuta un .js de plugin Figma vía Desktop Bridge (puerto 9223).
 * Uso: node scripts/run-figma-bridge-execute.js scripts/figma-design-file-wallet-flows-parity-part-a.js
 */
const fs = require('fs');
const path = require('path');
const WebSocket = require('ws');

const fileKey = 'Vf2tNMXyKAlJSV53A1v4Is';
const preferred = process.env.FIGMA_BRIDGE_PORT;
const ports = preferred
  ? [preferred]
  : ['9223', '9224', '9225', '9226', '9227', '9228', '9229', '9230', '9231', '9232'];
const scriptPath = process.argv[2];
if (!scriptPath) {
  console.error('Usage: node run-figma-bridge-execute.js <script.js>');
  process.exit(1);
}
const code = fs.readFileSync(path.resolve(scriptPath), 'utf8');
const requestId = 'req-' + Date.now();

function tryPort(portIndex) {
  if (portIndex >= ports.length) {
    console.error('No Figma Bridge port responded (tried ' + ports.join(', ') + ')');
    process.exit(1);
  }
  const port = ports[portIndex];
  const ws = new WebSocket(`ws://127.0.0.1:${port}`);

  ws.on('open', () => {
    ws.send(
      JSON.stringify({
        type: 'EXECUTE_CODE',
        requestId,
        fileKey,
        code,
        timeout: 120000,
      }),
    );
  });

  ws.on('message', (data) => {
    const msg = JSON.parse(data.toString());
    if (msg.requestId !== requestId) return;
    if (msg.type === 'EXECUTE_CODE_RESULT') {
      console.log(JSON.stringify(msg, null, 2));
      ws.close();
      process.exit(msg.success ? 0 : 1);
    }
  });

  ws.on('error', () => {
    ws.close();
    tryPort(portIndex + 1);
  });
}

tryPort(0);

setTimeout(() => {
  console.error('Timeout waiting for Figma Bridge');
  process.exit(1);
}, 130000);
