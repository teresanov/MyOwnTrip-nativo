"""Audit Disabled variants in a Figwright get_nodes_info JSON dump."""
import json
import sys
from collections import Counter

BAD = {"VariableID:55001:407"}  # legacy kit disabled container @ 10 %


def walk(node, variant="", findings=None):
    if findings is None:
        findings = {"content_disabled": [], "state_layer_disabled": [], "pressed_sl": []}
    name = node.get("name", "")
    if name.startswith("Type=") or name.startswith("State="):
        variant = name
    if name == "Content" and "State=Disabled" in variant:
        fills = node.get("fills") or []
        var = None
        has_solid = False
        if fills:
            has_solid = fills[0].get("type") == "SOLID" and fills[0].get("visible", True)
            if has_solid:
                var = (fills[0].get("boundVariables") or {}).get("color")
        findings["content_disabled"].append(
            {
                "id": node["id"],
                "variant": variant,
                "fillVar": var,
                "hasSolid": has_solid,
                "bad": var in BAD if var else False,
            }
        )
    if name == "State-layer" and "State=Disabled" in variant:
        findings["state_layer_disabled"].append(
            {
                "id": node["id"],
                "variant": variant,
                "opacityVar": (node.get("boundVariables") or {}).get("opacity"),
                "opacity": node.get("opacity"),
            }
        )
    if name == "State-layer" and "State=Pressed" in variant:
        fills = node.get("fills") or []
        var = None
        if fills:
            var = (fills[0].get("boundVariables") or {}).get("color")
        findings["pressed_sl"].append(
            {"id": node["id"], "variant": variant, "fillVar": var}
        )
    for child in node.get("children") or []:
        walk(child, variant, findings)
    return findings


def main():
    data = json.load(open(sys.argv[1], encoding="utf-8"))
    root = data["nodes"][0]
    f = walk(root)
    print("ROOT", root["id"], root["name"])
    bad_c = [x for x in f["content_disabled"] if x["bad"]]
    no_var_solid = [
        x
        for x in f["content_disabled"]
        if x["hasSolid"] and not x["fillVar"]
    ]
    sl_manual = [
        x for x in f["state_layer_disabled"] if not x["opacityVar"]
    ]
    pressed_08 = [
        x
        for x in f["pressed_sl"]
        if x["fillVar"] and "343" in (x["fillVar"] or "")
    ]
    print(
        "disabled Content:",
        len(f["content_disabled"]),
        "bad:",
        len(bad_c),
        "solid no var:",
        len(no_var_solid),
    )
    print("disabled State-layer no opacity var:", len(sl_manual))
    print("pressed State-layer Opacity-08:", len(pressed_08))
    if bad_c:
        for x in bad_c[:8]:
            print(" BAD", x)
    print(
        "content vars:",
        Counter(x["fillVar"] for x in f["content_disabled"] if x["fillVar"]),
    )


if __name__ == "__main__":
    main()