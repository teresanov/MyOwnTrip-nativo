/**
 * TripHeroCard + Eyebrow label — enlazar text styles M3 obligatorios.
 * Ejecutar vía figma_execute en librería DS zrGAL4v6MEMc9hzZemU432.
 *
 * Color sigue en variables de nodo; estilos sin color (limitación API).
 */
if (figma.fileKey !== 'zrGAL4v6MEMc9hzZemU432') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_nativo — Design System');
}

const TRIP_HERO_SET_ID = '61199:7862';
const EYEBROW_SET_ID = '61202:16834';

const allStyles = await figma.getLocalTextStylesAsync();

function pickStyle(name, family) {
  const matches = allStyles.filter((s) => s.name === name);
  if (family) {
    const hit = matches.find((s) => s.fontName.family === family);
    if (hit) return hit;
  }
  return matches.find((s) => s.fontName.family !== 'Roboto') || matches[0];
}

const STYLES = {
  title: pickStyle('M3/headline/small-emphasized', 'Fraunces'),
  countdown: pickStyle('M3/title/medium', 'Inter'),
  meta: pickStyle('M3 / body / medium', 'Inter'),
  eyebrowSmall: pickStyle('M3 / label / medium', 'Inter'),
  eyebrowMedium: pickStyle('M3 / label / large-emphasized', 'Inter'),
  cta: pickStyle('M3/label/large', 'Inter'),
};

for (const [key, style] of Object.entries(STYLES)) {
  if (!style) throw new Error('Missing text style: ' + key);
  await figma.loadFontAsync(style.fontName);
}

async function applyTextStyle(node, textStyle) {
  if (!node || node.type !== 'TEXT' || !textStyle) return;
  await node.setTextStyleIdAsync(textStyle.id);
}

const report = { tripHero: [], eyebrow: [] };

const tripSet = await figma.getNodeByIdAsync(TRIP_HERO_SET_ID);
if (!tripSet) throw new Error('TripHeroCard not found');

for (const variant of tripSet.children) {
  if (variant.type !== 'COMPONENT') continue;

  for (const [nodeName, styleKey] of [
    ['Countdown', 'countdown'],
    ['Title', 'title'],
    ['Meta', 'meta'],
  ]) {
    const node = variant.findOne((n) => n.name === nodeName && n.type === 'TEXT');
    if (node) {
      await applyTextStyle(node, STYLES[styleKey]);
      report.tripHero.push({ variant: variant.name, name: nodeName, style: STYLES[styleKey].name });
    }
  }

  const cta = variant
    .findOne((n) => n.name === 'Button - tonal')
    ?.findOne((n) => n.name === 'Label' && n.type === 'TEXT');
  if (cta) {
    await applyTextStyle(cta, STYLES.cta);
    report.tripHero.push({ variant: variant.name, name: 'CTA Label', style: STYLES.cta.name });
  }
}

const eyebrowSet = await figma.getNodeByIdAsync(EYEBROW_SET_ID);
if (eyebrowSet?.type === 'COMPONENT_SET') {
  for (const variant of eyebrowSet.children) {
    if (variant.type !== 'COMPONENT') continue;
    const label = variant.findOne((n) => n.type === 'TEXT' && /label/i.test(n.name));
    if (!label) continue;
    const ts = /Size=Small/i.test(variant.name) ? STYLES.eyebrowSmall : STYLES.eyebrowMedium;
    await applyTextStyle(label, ts);
    report.eyebrow.push({ variant: variant.name, style: ts.name });
  }
}

return { status: 'trip-hero text-styles bound', report };
