/**
 * Restaurar subset MyOwnTrip desde huérfanos (mismos IDs que CS).
 * Ejecutar en archivo principal con Desktop Bridge; lotes ≤25 sets.
 */

const SKIP_IDS = new Set(["442911:208", "442911:218"]); // placeholders interim Cards

const V11_REMOVE = [/Search full-screen/i, /Search docked layout/i, /Side [Ss]heet/i];

const BUTTON_XLARGE = [
  /^Button$/i,
  /^Button -/i,
  /^Toggle button/i,
  /^Icon button/i,
];

async function findOrCreateParent(page, item) {
  if (item.parentId) {
    try {
      const p = await figma.getNodeByIdAsync(item.parentId);
      if (p && p.type !== "DOCUMENT") return p;
    } catch (_) {}
  }
  if (item.parentName) {
    let p = page.findOne(
      (n) => (n.type === "SECTION" || n.type === "FRAME") && n.name === item.parentName,
    );
    if (p) return p;
    if (item.parentType === "SECTION") {
      p = figma.createSection();
      p.name = item.parentName;
      page.appendChild(p);
      p.x = 0;
      p.y = 0;
      return p;
    }
    if (item.parentType === "FRAME") {
      p = figma.createFrame();
      p.name = item.parentName;
      page.appendChild(p);
      return p;
    }
  }
  const section = page.findOne((n) => n.type === "SECTION");
  return section || page;
}

async function restoreCatalogBatch(catalog, dryRun = false) {
  await figma.loadAllPagesAsync();
  const restored = [];
  const skipped = [];
  const errors = [];

  for (const item of catalog) {
    if (SKIP_IDS.has(item.id)) {
      skipped.push({ name: item.name, reason: "skip placeholder" });
      continue;
    }

    const page = figma.root.children.find((p) => p.name === item.page);
    if (!page) {
      errors.push({ name: item.name, err: `page missing: ${item.page}` });
      continue;
    }

    const already = page.findAll(
      (n) => n.type === "COMPONENT_SET" && n.name === item.name,
    );
    if (already.length > 0) {
      const node = already[0];
      node.x = item.x;
      node.y = item.y;
      skipped.push({ name: item.name, action: "repositioned", id: node.id });
      continue;
    }

    const src = await figma.getNodeByIdAsync(item.id);
    if (!src || src.type !== "COMPONENT_SET") {
      errors.push({ name: item.name, err: "orphan not found" });
      continue;
    }

    if (dryRun) {
      restored.push({ name: item.name, action: "would restore" });
      continue;
    }

    const parent = await findOrCreateParent(page, item);
    const clone = src.clone();
    parent.appendChild(clone);
    clone.x = item.x;
    clone.y = item.y;
    restored.push({ name: clone.name, id: clone.id, variants: clone.children.length });
  }

  return { restored, skipped, errors };
}

async function ensureSlidersPage() {
  await figma.loadAllPagesAsync();
  let page = figma.root.children.find((p) => p.name === "Sliders");
  if (!page) {
    page = figma.createPage();
    page.name = "Sliders";
  }
  return page.id;
}

async function pruneButtonXLarge() {
  const patterns = [/Size=XLarge/];
  const removed = [];
  await figma.loadAllPagesAsync();
  for (const page of figma.root.children) {
    if (page.name === "_archive") continue;
    for (const set of page.findAll((n) => n.type === "COMPONENT_SET")) {
      const isButtonLike = BUTTON_XLARGE.some((re) => re.test(set.name));
      if (!isButtonLike) continue;
      let count = 0;
      for (const v of [...set.children]) {
        if (patterns.some((re) => re.test(v.name)) && set.children.length > 1) {
          v.remove();
          count++;
        }
      }
      if (count) removed.push({ name: set.name, removed: count, remaining: set.children.length });
    }
  }
  return removed;
}

async function removeV11Sets() {
  const removed = [];
  await figma.loadAllPagesAsync();
  for (const page of figma.root.children) {
    for (const set of page.findAll((n) => n.type === "COMPONENT_SET")) {
      if (V11_REMOVE.some((re) => re.test(set.name))) {
        set.remove();
        removed.push({ name: set.name, page: page.name });
      }
    }
  }
  return removed;
}

// Inline en figma_execute: restoreCatalogBatch(CATALOG_BATCH), ensureSlidersPage(), pruneButtonXLarge(), removeV11Sets()
