"""Audit chip disabled/pressed bindings in Figwright get_nodes_info JSON."""
import json
import sys
from collections import Counter

BAD_DISABLED = {
    "VariableID:55001:407",
    "VariableID:55001:413",
    "VariableID:55001:412",
    "VariableID:55001:410",
}


def walk(node, findings=None, comp_variant=""):
    if findings is None:
        findings = {"disabled": [], "pressed": []}
    name = node.get("name", "")
    if node.get("type") == "COMPONENT":
        comp_variant = name
    if node.get("type") == "COMPONENT" and "State=Disabled" in comp_variant:
        fills = node.get("fills") or []
        var = None
        if fills and fills[0].get("type") == "SOLID" and fills[0].get("visible", True):
            var = (fills[0].get("boundVariables") or {}).get("color")
        sl_id = None
        sl_op = None
        for child in node.get("children") or []:
            if child.get("name") in ("State layer", "State-layer"):
                sl_id = child["id"]
                sl_op = (child.get("boundVariables") or {}).get("opacity")
                break
        findings["disabled"].append(
            {
                "comp_id": node["id"],
                "state_layer_id": sl_id,
                "variant": comp_variant,
                "fillVar": var,
                "opacityVar": sl_op,
            }
        )
    if name in ("State layer", "State-layer") and "State=Pressed" in comp_variant:
        fills = node.get("fills") or []
        var = (fills[0].get("boundVariables") or {}).get("color") if fills else None
        findings["pressed"].append({"id": node["id"], "fillVar": var, "variant": comp_variant})
    for child in node.get("children") or []:
        walk(child, findings, comp_variant)
    return findings


def main():
    data = json.load(open(sys.argv[1], encoding="utf-8"))
    for root in data["nodes"]:
        f = walk(root)
        print("ROOT", root["id"], root["name"])
        bad = [x for x in f["disabled"] if x["fillVar"] in BAD_DISABLED]
        no_op = [x for x in f["disabled"] if x["state_layer_id"] and not x["opacityVar"]]
        print("disabled:", len(f["disabled"]), "bad fill:", len(bad), "no opacity var:", len(no_op))
        print("fills:", Counter(x["fillVar"] for x in f["disabled"]))
        p08 = [p for p in f["pressed"] if p["fillVar"] and "343" in (p["fillVar"] or "")]
        print("pressed 08:", len(p08))


if __name__ == "__main__":
    main()