/**
 * TripHeroCard — component set en librería DS (Cards).
 * Ejecutar vía figma_execute (Desktop Bridge) en zrGAL4v6MEMc9hzZemU432.
 *
 * Patrón: portada de viaje destacado (Home). No confundir con Stacked · Media only.
 */
if (figma.fileKey !== 'zrGAL4v6MEMc9hzZemU432') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_nativo — Design System');
}

const CARDS_SECTION_ID = '55412:13979';
const ELEVATED_BB = '52350:27634';
const OUTLINED_BB = '52347:27854';
const ASSIST_CHIP_KEY = '492812df895914324d86de773a97e152e19d8e33';
const BUTTON_KEY = '3bf314ceb2123b2088dc56d86557062902d828c4';

const W = 360;
const H = 280;

await figma.loadFontAsync({ family: 'Inter', style: 'Regular' });
await figma.loadFontAsync({ family: 'Inter', style: 'Medium' });
await figma.loadFontAsync({ family: 'Inter', style: 'Semi Bold' });
await figma.loadFontAsync({ family: 'Fraunces', style: 'SemiBold' });

const section = await figma.getNodeByIdAsync(CARDS_SECTION_ID);
if (!section || section.type !== 'SECTION') {
  throw new Error('Cards section not found: ' + CARDS_SECTION_ID);
}

const existing = section.findOne(
  (n) => n.type === 'COMPONENT_SET' && n.name === 'TripHeroCard',
);
if (existing) existing.remove();

const elevatedMc = await figma.getNodeByIdAsync(ELEVATED_BB);
const outlinedMc = await figma.getNodeByIdAsync(OUTLINED_BB);
const chipMc = await figma.importComponentByKeyAsync(ASSIST_CHIP_KEY);
const buttonMc = await figma.importComponentByKeyAsync(BUTTON_KEY);

async function buildVariant(styleName, bgMc) {
  const root = figma.createFrame();
  root.name = `Style=${styleName}`;
  root.resize(W, H);
  root.layoutMode = 'NONE';
  root.clipsContent = true;
  root.cornerRadius = 12;

  const bg = bgMc.createInstance();
  bg.name = 'Background';
  bg.resize(W, H);
  bg.x = 0;
  bg.y = 0;
  root.appendChild(bg);

  const media = figma.createRectangle();
  media.name = 'Cover image';
  media.resize(W, H);
  media.x = 0;
  media.y = 0;
  media.fills = [
    {
      type: 'GRADIENT_LINEAR',
      gradientTransform: [
        [1, 0, 0],
        [0, 1, 0],
      ],
      gradientStops: [
        { position: 0, color: { r: 0.45, g: 0.35, b: 0.22, a: 1 } },
        { position: 1, color: { r: 0.15, g: 0.12, b: 0.1, a: 1 } },
      ],
    },
  ];
  root.appendChild(media);

  const scrim = figma.createRectangle();
  scrim.name = 'Scrim';
  scrim.resize(W, H);
  scrim.x = 0;
  scrim.y = 0;
  scrim.fills = [
    {
      type: 'GRADIENT_LINEAR',
      gradientTransform: [
        [0, 1, 0],
        [-1, 0, 1],
      ],
      gradientStops: [
        { position: 0, color: { r: 0, g: 0, b: 0, a: 0.08 } },
        { position: 0.45, color: { r: 0, g: 0, b: 0, a: 0.02 } },
        { position: 0.7, color: { r: 0, g: 0, b: 0, a: 0.55 } },
        { position: 1, color: { r: 0, g: 0, b: 0, a: 0.82 } },
      ],
    },
  ];
  root.appendChild(scrim);

  const chip = chipMc.createInstance();
  chip.name = 'Eyebrow chip';
  chip.x = 16;
  chip.y = 16;
  root.appendChild(chip);
  const chipLabel = chip.findOne((n) => n.type === 'TEXT');
  if (chipLabel) {
    await figma.loadFontAsync(chipLabel.fontName);
    chipLabel.characters = 'Próximo viaje';
  }

  const countdown = figma.createText();
  countdown.name = 'Countdown';
  countdown.fontName = { family: 'Inter', style: 'Medium' };
  countdown.characters = 'Sale en 3 días';
  countdown.fontSize = 16;
  countdown.fills = [{ type: 'SOLID', color: { r: 0.91, g: 0.78, b: 0.55 } }];
  countdown.x = 16;
  countdown.y = 148;
  countdown.resize(W - 32, 22);
  root.appendChild(countdown);

  const title = figma.createText();
  title.name = 'Title';
  title.fontName = { family: 'Fraunces', style: 'SemiBold' };
  title.characters = 'Barcelona fin de semana';
  title.fontSize = 24;
  title.lineHeight = { unit: 'PIXELS', value: 30 };
  title.fills = [{ type: 'SOLID', color: { r: 1, g: 1, b: 1 } }];
  title.x = 16;
  title.y = 174;
  title.resize(W - 32, 60);
  root.appendChild(title);

  const meta = figma.createText();
  meta.name = 'Meta';
  meta.fontName = { family: 'Inter', style: 'Regular' };
  meta.characters = '20 jun 2026 – 22 jun 2026 · 3 días';
  meta.fontSize = 14;
  meta.fills = [{ type: 'SOLID', color: { r: 1, g: 1, b: 1, a: 0.88 } }];
  meta.x = 16;
  meta.y = 210;
  meta.resize(W - 32, 40);
  root.appendChild(meta);

  const cta = buttonMc.createInstance();
  cta.name = 'Primary CTA';
  cta.x = 16;
  cta.y = 232;
  cta.resize(W - 32, 40);
  root.appendChild(cta);
  const ctaLabel = cta.findOne((n) => n.name === 'Label' && n.type === 'TEXT');
  if (ctaLabel) {
    await figma.loadFontAsync(ctaLabel.fontName);
    ctaLabel.characters = 'Abrir cuaderno';
  }

  const component = figma.createComponentFromNode(root);
  return component;
}

const elevatedVariant = await buildVariant('Elevated', elevatedMc);
const outlinedVariant = await buildVariant('Outlined', outlinedMc);

const set = figma.combineAsVariants([elevatedVariant, outlinedVariant], section);
set.name = 'TripHeroCard';
set.description =
  'Portada del viaje destacado en Home. Imagen full-bleed + scrim + eyebrow + countdown + título + meta + CTA. Ver showcase TripHeroCard. Chip y CTA: editar en instancia anidada.';

set.addComponentProperty('Countdown text', 'TEXT', 'Sale en 3 días');
set.addComponentProperty('Show countdown', 'BOOLEAN', true);
set.addComponentProperty('Title text', 'TEXT', 'Barcelona fin de semana');
set.addComponentProperty('Meta text', 'TEXT', '20 jun 2026 – 22 jun 2026 · 3 días');

function bindTextProps(variant) {
  const defs = set.componentPropertyDefinitions;
  const key = (prefix) =>
    Object.keys(defs).find((k) => k.startsWith(prefix)) || null;

  const bind = (node, prefix) => {
    const propKey = key(prefix);
    if (node && propKey) {
      node.componentPropertyReferences = { characters: propKey };
    }
  };

  const eyebrowChip = variant.findOne((n) => n.name === 'Eyebrow chip');
  // Chip label: no bind (instance sublayer — edit nested Assist chip)
  const countdownNode = variant.findOne((n) => n.name === 'Countdown');
  bind(countdownNode, 'Countdown text');
  const showKey = key('Show countdown');
  if (countdownNode && showKey) {
    countdownNode.componentPropertyReferences = {
      ...(countdownNode.componentPropertyReferences || {}),
      visible: showKey,
    };
  }
  bind(variant.findOne((n) => n.name === 'Title'), 'Title text');
  bind(variant.findOne((n) => n.name === 'Meta'), 'Meta text');
  // CTA label: no bind (instance sublayer — edit nested Button)
}

for (const child of set.children) {
  bindTextProps(child);
}

outlinedVariant.x = elevatedVariant.x + W + 48;
outlinedVariant.y = elevatedVariant.y;

const maxY = Math.max(...section.children.map((c) => c.y + c.height));
set.x = 90;
set.y = maxY + 80;

figma.viewport.scrollAndZoomIntoView([set]);

return {
  setId: set.id,
  setKey: set.key,
  variants: set.children.map((c) => ({ id: c.id, name: c.name, key: c.key })),
  figmaUrl: `https://www.figma.com/design/zrGAL4v6MEMc9hzZemU432/MyOwnTrip_nativo---Design-System?node-id=${set.id.replace(':', '-')}`,
};
