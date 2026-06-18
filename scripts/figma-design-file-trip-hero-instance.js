/**
 * Sustituye el frame compuesto "Hero trip" por instancia TripHeroCard en design-file.
 * Prerrequisito: publicar TripHeroCard desde scripts/figma-trip-hero-card.js en la librería DS.
 * Ejecutar vía figma_execute en Vf2tNMXyKAlJSV53A1v4Is.
 */
if (figma.fileKey !== 'Vf2tNMXyKAlJSV53A1v4Is') {
  throw new Error('ABORT: ejecutar en MyOwnTrip_design-file');
}

// Clave de variante Elevated — actualizar tras publicar librería si cambia
const TRIP_HERO_ELEVATED_KEY = '7ada0034947b879a0b8b0ee34c24dc325e24118a';

const body = await figma.getNodeByIdAsync('205:1021');
if (!body) throw new Error('Home body not found');

const heroFrame = body.findOne((n) => n.name === 'Hero trip' && n.type === 'FRAME');
if (!heroFrame) throw new Error('Hero trip frame not found');

const idx = body.children.indexOf(heroFrame);
const comp = await figma.importComponentByKeyAsync(TRIP_HERO_ELEVATED_KEY);
const inst = comp.createInstance();
inst.name = 'TripHeroCard';
inst.layoutSizingHorizontal = 'FILL';

const props = {};
for (const key of Object.keys(inst.componentProperties)) {
  if (key.startsWith('Eyebrow text')) props[key] = 'Próximo viaje';
  if (key.startsWith('Countdown text')) props[key] = 'Sale en 3 días';
  if (key.startsWith('Show countdown')) props[key] = true;
  if (key.startsWith('Title text')) props[key] = 'Barcelona fin de semana';
  if (key.startsWith('Meta text')) props[key] = '20 jun 2026 – 22 jun 2026 · 3 días';
  if (key.startsWith('CTA label')) props[key] = 'Abrir cuaderno';
}
inst.setProperties(props);

body.insertChild(idx, inst);
heroFrame.remove();

return { instanceId: inst.id, status: 'Hero trip replaced with TripHeroCard instance' };
