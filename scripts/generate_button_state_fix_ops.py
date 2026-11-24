"""Emit Figwright batch ops for button disabled + pressed fixes from get_nodes_info JSON."""
import json
import sys
from pathlib import Path

VAR_ON_SURFACE_12 = "VariableID:61552:10171"
VAR_PRIMARY_12 = "VariableID:61552:10196"
VAR_SEC_CONT_12 = "VariableID:61552:10194"
VAR_SECONDARY_12 = "VariableID:61552:10743"
VAR_DISABLED_CONTENT = "VariableID:61552:10195"
VAR_ON_SC_10 = "VariableID:55001:344"  # pressed tonal
VAR_ON_SURFACE_10 = "VariableID:55001:407"

# Elevated enabled uses Surface Container Low (check id in variables)
VAR_SURFACE_CONTAINER_LOW = "VariableID:54778:452"  # may need Surface Container Low/Opacity-12

ROLE_12_BY_ENABLED = {
    "VariableID:54778:407": VAR_PRIMARY_12,
    "VariableID:54778:414": VAR_SEC_CONT_12,
    "VariableID:54778:412": VAR_SECONDARY_12,
    "VariableID:54778:452": None,  # resolve below
}


def walk(node, variant="", ctx=None):
    if ctx is None:
        ctx = {
            "set_name": "",
            "content_pairs": [],  # (content_id, state_layer_id, variant, fill_var)
            "pressed_sl": [],
            "enabled_by_variant": {},
        }
    name = node.get("name", "")
    if name.startswith("Type="):
        variant = name
    if name == "Content" and "State=Enabled" in variant:
        fills = node.get("fills") or []
        if fills and fills[0].get("type") == "SOLID":
            var = (fills[0].get("boundVariables") or {}).get("color")
            if var:
                ctx["enabled_by_variant"][variant] = var
    if name == "Content" and "State=Disabled" in variant:
        fills = node.get("fills") or []
        fill_var = None
        has_solid = bool(
            fills and fills[0].get("type") == "SOLID" and fills[0].get("visible", True)
        )
        if has_solid:
            fill_var = (fills[0].get("boundVariables") or {}).get("color")
        sl_id = None
        for child in node.get("children") or []:
            if child.get("name") == "State-layer":
                sl_id = child["id"]
                break
        ctx["content_pairs"].append(
            {
                "content_id": node["id"],
                "state_layer_id": sl_id,
                "variant": variant,
                "fill_var": fill_var,
                "has_solid": has_solid,
            }
        )
    if name == "State-layer" and "State=Pressed" in variant:
        fills = node.get("fills") or []
        fill_var = None
        if fills:
            fill_var = (fills[0].get("boundVariables") or {}).get("color")
        ctx["pressed_sl"].append(
            {"id": node["id"], "variant": variant, "fill_var": fill_var}
        )
    for child in node.get("children") or []:
        walk(child, variant, ctx)
    return ctx


def disabled_target(set_name: str, variant: str, fill_var: str | None, enabled_by_variant: dict):
    sn = set_name.lower()
    if "toggle" in sn and "tonal" in sn:
        if "Selected=True" in variant:
            return VAR_SECONDARY_12
        return VAR_SEC_CONT_12
    if "toggle" in sn and "outline" in sn:
        return VAR_ON_SURFACE_12
    if "toggle" in sn and "elevated" in sn:
        return None  # often no fill; handle separately
    if sn == "toggle button":
        if "Selected=True" in variant:
            return VAR_PRIMARY_12
        return VAR_ON_SURFACE_12
    if sn == "button":
        return VAR_PRIMARY_12
    if "tonal" in sn and sn.startswith("button"):
        return VAR_SEC_CONT_12
    if "outline" in sn or "text" in sn:
        return VAR_ON_SURFACE_12
    if "elevated" in sn:
        return "VariableID:61554:10842"
    if "togglable" in sn:
        if "tonal" in sn:
            if "Selected=True" in variant:
                return VAR_SECONDARY_12
            return VAR_SEC_CONT_12
        if "outline" in sn or "standard" in sn:
            if "Selected=True" in variant:
                return VAR_ON_SURFACE_12
            return VAR_ON_SURFACE_12
        if "Selected=True" in variant:
            return VAR_PRIMARY_12
        return VAR_ON_SURFACE_12
    if "icon" in sn:
        if "tonal" in sn:
            return VAR_SEC_CONT_12
        if "outline" in sn or "standard" in sn:
            return VAR_ON_SURFACE_12
        return VAR_PRIMARY_12
    return None


def main():
    path = Path(sys.argv[1])
    data = json.loads(path.read_text(encoding="utf-8"))
    ops = []
    for root in data["nodes"]:
        ctx = walk(root)
        ctx["set_name"] = root["name"]
        for row in ctx["content_pairs"]:
            target = disabled_target(
                root["name"], row["variant"], row["fill_var"], ctx["enabled_by_variant"]
            )
            if target and row["fill_var"] != target:
                if row["has_solid"]:
                    if row["fill_var"] in {VAR_ON_SURFACE_10, None} or target:
                        ops.append(
                            {
                                "tool": "bind_variable_to_paint",
                                "params": {
                                    "nodeId": row["content_id"],
                                    "variableId": target,
                                },
                            }
                        )
                elif "elevated" in root["name"].lower():
                    ops.append(
                        {
                            "tool": "set_fills",
                            "params": {
                                "nodeId": row["content_id"],
                                "fills": [
                                    {
                                        "type": "SOLID",
                                        "visible": True,
                                        "boundVariables": {"color": target},
                                    }
                                ],
                            },
                        }
                    )
            if row["state_layer_id"]:
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
        for row in ctx["pressed_sl"]:
            if row["fill_var"] == "VariableID:55001:343":
                ops.append(
                    {
                        "tool": "bind_variable_to_paint",
                        "params": {
                            "nodeId": row["id"],
                            "variableId": VAR_ON_SC_10,
                        },
                    }
                )
    out = Path(sys.argv[2]) if len(sys.argv) > 2 else Path("button_fix_ops.json")
    out.write_text(json.dumps(ops, indent=2), encoding="utf-8")
    print(f"{path.name}: {len(ops)} ops -> {out}")


if __name__ == "__main__":
    main()