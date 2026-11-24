"""Print each chunk file path for external Figwright batch apply."""
from pathlib import Path

chunks = sorted(Path(__file__).parent.joinpath("chunks").glob("chunk_*.json"))
for p in chunks:
    print(p)