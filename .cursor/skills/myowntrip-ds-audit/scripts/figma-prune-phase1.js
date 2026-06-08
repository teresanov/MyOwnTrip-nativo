/**
 * Fase 1 — poda páginas y sets huérfanos (MVP+1).
 * Ejecutar SOLO con snapshot pre-prune y Desktop Bridge conectado.
 * Lotes ≤20 operaciones por invocación figma_execute.
 */

const DELETE_PAGES = [
  "Getting started",
  "Table of contents",
  "Avatars",
  "Examples",
  "Shape",
  "Utilities",
  "---",
  "Carousel",
  "Toolbars",
  "Tooltips",
];

const PRUNE_SET_NAME_PATTERNS = [
  /navigation\s*rail/i,
  /side\s*sheet/i,
  /time\s*picker/i,
  /^rail\b/i,
];

async function ensureArchivePage() {
  let archive = figma.root.children.find((p) => p.name === "_archive");
  if (!archive) {
    archive = figma.createPage();
    archive.name = "_archive";
  }
  return { archiveId: archive.id, created: !archive };
}

async function deletePagesBatch(pageNames) {
  const removed = [];
  const skipped = [];
  for (const name of pageNames) {
    const page = figma.root.children.find((p) => p.name === name);
    if (!page) {
      skipped.push({ name, reason: "not found" });
      continue;
    }
    if (page.name === "_archive" || page.name === "Styles" || page.name === "Icons") {
      skipped.push({ name, reason: "protected" });
      continue;
    }
    page.remove();
    removed.push(name);
  }
  return { removed, skipped, remainingPages: figma.root.children.map((p) => p.name) };
}

async function countInstancesOnPage(page) {
  let count = 0;
  function walk(n) {
    if (n.type === "INSTANCE") count += 1;
    if ("children" in n) for (const c of n.children) walk(c);
  }
  for (const c of page.children) walk(c);
  return count;
}

async function pruneSetsOnPage(pageName, dryRun = true) {
  const page = figma.root.children.find((p) => p.name === pageName);
  if (!page) return { error: "page not found" };

  const candidates = [];
  function walk(node) {
    if (node.type === "COMPONENT_SET") {
      const match = PRUNE_SET_NAME_PATTERNS.some((re) => re.test(node.name));
      if (match) {
        let inst = 0;
        function countInst(n) {
          if (n.type === "INSTANCE") inst += 1;
          if ("children" in n) for (const c of n.children) countInst(c);
        }
        for (const c of page.children) countInst(c);
        candidates.push({ id: node.id, name: node.name, instances: inst });
      }
    }
    if ("children" in node) for (const c of node.children) walk(c);
  }
  for (const child of page.children) walk(child);

  const removed = [];
  if (!dryRun) {
    for (const c of candidates) {
      if (c.instances > 0) continue;
      const node = await figma.getNodeByIdAsync(c.id);
      if (node) {
        node.remove();
        removed.push(c.name);
      }
    }
  }

  return { pageName, dryRun, candidates, removed };
}

// Export helpers for inline figma_execute wrappers:
// ensureArchivePage(), deletePagesBatch(DELETE_PAGES.slice(0,5)), pruneSetsOnPage('Navigation', false)
