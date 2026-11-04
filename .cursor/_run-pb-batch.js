const fs = require('fs');
const path = require('path');
const WebSocket = require('ws');

const fileKey = 'Vf2tNMXyKAlJSV53A1v4Is';
const port = process.env.FIGMA_BRIDGE_PORT || '9223';
const dir = __dirname;

const steps = [
  ['exec-b64-pb0-a.js', 60000],
  ['exec-b64-pb0-b.js', 60000],
  ['exec-b64-pb0-merge.js', 60000],
  ['exec-b64-pb1-a.js', 60000],
  ['exec-b64-pb1-b.js', 60000],
  ['exec-b64-pb1-merge.js', 60000],
  ['exec-b64-pb2-a.js', 60000],
  ['exec-b64-pb2-b.js', 60000],
  ['exec-b64-pb2-merge.js', 60000],
  ['exec-b64-pb3.js', 60000],
  ['exec-b64-pb4.js', 60000],
  ['b64-run-part-b.js', 120000],
];

function executeStep(name, timeout) {
  const code = fs.readFileSync(path.join(dir, name), 'utf8');
  const requestId = `req-${Date.now()}-${name}`;
  return new Promise((resolve, reject) => {
    const ws = new WebSocket(`ws://127.0.0.1:${port}`);
    const timer = setTimeout(() => {
      ws.close();
      reject(new Error(`Timeout ${timeout}ms on ${name}`));
    }, timeout + 5000);

    ws.on('open', () => {
      ws.send(
        JSON.stringify({
          type: 'EXECUTE_CODE',
          requestId,
          fileKey,
          code,
          timeout,
        }),
      );
    });

    ws.on('message', (data) => {
      let msg;
      try {
        msg = JSON.parse(data.toString());
      } catch (e) {
        return;
      }
      if (msg.requestId !== requestId) return;
      if (msg.type === 'EXECUTE_CODE_RESULT') {
        clearTimeout(timer);
        ws.close();
        resolve({ name, msg });
      }
    });

    ws.on('error', (err) => {
      clearTimeout(timer);
      reject(new Error(`${name}: ${err.message}`));
    });
  });
}

(async () => {
  for (const [name, timeout] of steps) {
    const { msg } = await executeStep(name, timeout);
    const out = {
      step: name,
      success: msg.success,
      result: msg.result,
      error: msg.error,
    };
    console.log(JSON.stringify(out));
    if (!msg.success) {
      process.exit(1);
    }
  }
})().catch((e) => {
  console.error(JSON.stringify({ fatal: e.message }));
  process.exit(1);
});
