import { initShowcasePage, setupToc } from "../lib/bootstrap.js";
import { mountFoundation } from "../lib/foundation-page.js";
import data from "../data/foundations/spacing.json";

initShowcasePage("spacing");
mountFoundation(data);
setupToc();
