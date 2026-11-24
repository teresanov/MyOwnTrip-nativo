import json
import subprocess
from pathlib import Path

DUMP = Path(
    r"C:\Users\Usuario\.cursor\projects\C-Users-Usuario-AndroidProjects-MOT-nativo\agent-tools\4f7b7b31-1a71-41ff-b56a-2193a1673a18.txt"
)
TMP = Path(__file__).parent / "_tmp_audit.json"
AUDIT = Path(__file__).parent / "audit_button_disabled_figma.py"
GEN = Path(__file__).parent / "generate_button_state_fix_ops.py"

data = json.loads(DUMP.read_text(encoding="utf-8"))
all_ops = []
for n in data["nodes"]:
    TMP.write_text(json.dumps({"nodes": [n]}), encoding="utf-8")
    print("===", n["name"])
    subprocess.run(["python", str(AUDIT), str(TMP)], check=False)
    out = Path(__file__).parent / "ops_togglable_tmp.json"
    subprocess.run(["python", str(GEN), str(TMP), str(out)], check=False)
    if out.exists():
        all_ops.extend(json.loads(out.read_text(encoding="utf-8")))

Path(__file__).parent.joinpath("ops_batch4.json").write_text(
    json.dumps(all_ops, indent=2), encoding="utf-8"
)
print("total togglable ops", len(all_ops))