/**
 * Inventario por página — ejecutar vía figma_execute (Desktop Bridge).
 * NO usar loadAllPagesAsync global; una página por invocación.
 *
 * Uso MCP: pasar pageName como variable en el wrapper, o iterar externamente.
 */
// eslint-disable-next-line no-unused-vars
async function inventoryPage(pageName) {
  const page = figma.root.children.find((p) => p.name === pageName);
  if (!page) return { error: "page not found", pageName };

  const sets = [];
  const instances = { total: 0, bySet: {} };

  function walk(node) {
    if (node.type === "COMPONENT_SET") {
      const variants = node.children.filter((c) => c.type === "COMPONENT");
      const props = node.componentPropertyDefinitions || {};
      sets.push({
        id: node.id,
        name: node.name,
        variantCount: variants.length,
        propertyKeys: Object.keys(props),
        variantNames: variants.map((v) => v.name),
      });
    }
    if (node.type === "INSTANCE" && node.mainComponent) {
      instances.total += 1;
      const parentSet =
        node.mainComponent.parent?.type === "COMPONENT_SET"
          ? node.mainComponent.parent.name
          : node.mainComponent.name;
      instances.bySet[parentSet] = (instances.bySet[parentSet] || 0) + 1;
    }
    if ("children" in node) for (const c of node.children) walk(c);
  }

  for (const child of page.children) walk(child);

  return {
    pageName,
    pageId: page.id,
    setCount: sets.length,
    sets,
    instances,
  };
}
