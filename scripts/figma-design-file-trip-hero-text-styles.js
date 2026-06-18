/**
 * TripHeroCard instancias — importar text styles de librería DS y enlazar.
 * Ejecutar vía figma_execute en design-file Vf2tNMXyKAlJSV53A1v4Is.
 *
 * El design-file no tiene estilos locales; importStyleByKeyAsync crea el enlace remoto.
 * No sustituye publicar/actualizar el componente — solo fija overrides de tipografía.
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_design-file');
}

const STYLE_KEYS = {
  title: 'fc94ca472140b82d5c3a240cf4ffa2101db6a640', // M3/headline/small-emphasized
  countdown: '71e05c366cc02f5a25576b262dcf2e665a4c7885', // M3/title/medium
  meta: 'af5862182f0ac9e027bace471b833dc4fe1cc23c', // M3 / body / medium
  eyebrow: 'e109920fc5c52a8fff4ed98f16182172c8749303', // M3 / label / medium
  cta: 'be251ea271cd60f11997737b47ac040f09c862ee', // M3/label/large · Inter
};

const imported = {};
for (const [role, key] of Object.entries(STYLE_KEYS)) {
  imported[role] = await figma.importStyleByKeyAsync(key);
  await figma.loadFontAsync(imported[role].fontName);
}

async function bindTripHeroInstance(inst) {
  const binds = [
    ['Countdown', 'countdown'],
    ['Title', 'title'],
    ['Meta', 'meta'],
    ['Label text', 'eyebrow'],
  ];
  for (const [nodeName, role] of binds) {
    const text = inst.findOne((n) => n.name === nodeName && n.type === 'TEXT');
    if (text) await text.setTextStyleIdAsync(imported[role].id);
  }
  const cta = inst
    .findOne((n) => n.name === 'Button - tonal')
    ?.findOne((n) => n.name === 'Label' && n.type === 'TEXT');
  if (cta) await cta.setTextStyleIdAsync(imported.cta.id);

  return inst.findAll((n) => n.type === 'TEXT' && !n.textStyleId).length;
}

const fixed = [];
for (const page of figma.root.children) {
  await figma.setCurrentPageAsync(page);
  for (const inst of page.findAll((n) => n.type === 'INSTANCE' && n.name === 'TripHeroCard')) {
    const unlinked = await bindTripHeroInstance(inst);
    fixed.push({ id: inst.id, page: page.name, unlinked });
  }
}

return { status: 'trip-hero text-styles bound in design-file', instances: fixed.length, fixed };
