"""Generate Figwright batch ops for chip disabled state layers."""
import json
import sys
from pathlib import Path

VAR_SEC_CONT_12 = "VariableID:61552:10194"
VAR_WARNING_12 = "VariableID:61554:15545"
VAR_DISABLED_CONTENT = "VariableID:61552:10195"
LEGACY = {"VariableID:55001:413", "VariableID:55001:407", "VariableID:54778:454"}


def walk(node, findings=None, comp_variant=""):
    if findings is None:
        findings = []
    name = node.get("name", "")
    if node.get("type") == "COMPONENT":
        comp_variant = name
    if node.get("type") == "COMPONENT" and "State=Disabled" in comp_variant:
        fills = node.get("fills") or []
        var = None
        has_solid = bool(
            fills and fills[0].get("type") == "SOLID" and fills[0].get("visible", True)
        )
        if has_solid:
            var = (fills[0].get("boundVariables") or {}).get("color")
        sl_id = None
        sl_op = None
        for child in node.get("children") or []:
            if child.get("name") in ("State layer", "State-layer"):
                sl_id = child["id"]
                sl_op = (child.get("boundVariables") or {}).get("opacity")
                break
        findings.append(
            {
                "comp_id": node["id"],
                "state_layer_id": sl_id,
                "variant": comp_variant,
                "fillVar": var,
                "has_solid": has_solid,
                "opacityVar": sl_op,
                "set_name": "",
            }
        )
    for child in node.get("children") or []:
        walk(child, findings, comp_variant)
    return findings


def target_var(set_name: str, variant: str, current: str | None) -> str | None:
    sn = set_name.lower()
    if current not in LEGACY and current != "VariableID:61552:10194":
        return None
    if "assistive" in sn and "Style=Warning" in variant:
        return VAR_WARNING_12
    if ("filter" in sn or "suggestion" in sn) and "Selected=True" in variant:
        return VAR_SEC_CONT_12
    return None


def main():
    paths = sys.argv[1:]
    ops = []
    for path in paths:
        data = json.loads(Path(path).read_text(encoding="utf-8"))
        for root in data["nodes"]:
            for row in walk(root):
                row["set_name"] = root["name"]
                target = target_var(root["name"], row["variant"], row["fillVar"])
                if target and row["has_solid"] and row["fillVar"] != target:
                    ops.append(
                        {
                            "tool": "bind_variable_to_paint",
                            "params": {
                                "nodeId": row["comp_id"],
                                "variableId": target,
                            },
                        }
                    )
                if row["state_layer_id"] and not row["opacityVar"]:
                    ops.append(
                        {
                            "tool": "bind_variable_to_node",
                            "params": {
                                "nodeId": row["state_layer_id"],
                                "field": "opacity",
                                "variableId": VAR_DISABLED_CONTENT,
                            },
                        }
                    )
    out = Path(__file__).parent / "ops_chips.json"
    out.write_text(json.dumps(ops, indent=2), encoding="utf-8")
    print(len(ops), "ops ->", out)


if __name__ == "__main__":
    main()