"""Merge ops JSON files and apply via Figwright batch (chunked)."""
import json
import sys
from pathlib import Path

CHUNK = 40


def main():
    files = sys.argv[1:] if len(sys.argv) > 1 else []
    ops: list = []
    for f in files:
        ops.extend(json.loads(Path(f).read_text(encoding="utf-8")))
    # dedupe by stable key
    seen = set()
    deduped = []
    for op in ops:
        key = (op["tool"], json.dumps(op["params"], sort_keys=True))
        if key in seen:
            continue
        seen.add(key)
        deduped.append(op)
    out = Path(__file__).parent / "ops_merged.json"
    out.write_text(json.dumps(deduped, indent=2), encoding="utf-8")
    print("merged", len(deduped), "ops ->", out)
    print("chunks", (len(deduped) + CHUNK - 1) // CHUNK)


if __name__ == "__main__":
    main()