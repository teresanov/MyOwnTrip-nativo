#!/usr/bin/env python3
"""Genera PDFs de muestra para Wallet (boarding con QR escaneable, reserva hotel realista)."""
from __future__ import annotations

from io import BytesIO
from pathlib import Path

try:
    import qrcode
    from reportlab.lib import colors
    from reportlab.lib.pagesizes import A4
    from reportlab.lib.units import mm
    from reportlab.lib.utils import ImageReader
    from reportlab.pdfgen import canvas
except ImportError as exc:
    raise SystemExit(
        "Instala dependencias: pip install qrcode pillow reportlab"
    ) from exc

OUT = Path(__file__).resolve().parents[1] / "docs" / "samples" / "wallet"
OUT.mkdir(parents=True, exist_ok=True)

# Payload estilo BCBP (demo, no válido en aeropuerto real)
BOARDING_QR_PAYLOAD = (
    "M1DEMO/PAX EIB3254 MADBCNIB 3254 314Y014A0001 "
    "349>5180  5140BIB              2A825513825513 0000"
)

INK = colors.HexColor("#1A1C1E")
MUTED = colors.HexColor("#49454F")
ACCENT = colors.HexColor("#825513")
PAPER = colors.HexColor("#F7F2EA")
LINE = colors.HexColor("#E0D8CC")


def qr_image(payload: str) -> ImageReader:
    qr = qrcode.QRCode(version=None, box_size=6, border=2)
    qr.add_data(payload)
    qr.make(fit=True)
    img = qr.make_image(fill_color="black", back_color="white")
    buf = BytesIO()
    img.save(buf, format="PNG")
    buf.seek(0)
    return ImageReader(buf)


def write_boarding_pass(path: Path) -> None:
    c = canvas.Canvas(str(path), pagesize=A4, pageCompression=0)
    width, height = A4
    y = height - 72
    lines = [
        "BOARDING PASS",
        "IB 3254",
        "Madrid - Barcelona",
        "Flight departure",
        "2026-06-14",
        "Departure 09:15",
        "Gate B12",
        "Seat 14A",
    ]
    c.setFont("Helvetica-Bold", 16)
    c.drawString(72, y, lines[0])
    y -= 28
    c.setFont("Helvetica", 12)
    for line in lines[1:]:
        c.drawString(72, y, line)
        y -= 18
    qr = qr_image(BOARDING_QR_PAYLOAD)
    c.drawImage(qr, width / 2 - 90, 180, width=180, height=180, mask="auto")
    c.setFont("Helvetica", 10)
    c.drawCentredString(width / 2, 160, "Escanea en puerta de embarque")
    c.save()


def _draw_label_value(c: canvas.Canvas, x: float, y: float, label: str, value: str) -> float:
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(x, y, label)
    c.setFont("Helvetica-Bold", 11)
    c.setFillColor(INK)
    c.drawString(x, y - 14, value)
    return y - 34


def write_hotel(path: Path) -> None:
    """Confirmación de reserva estilo operador / hotel boutique (texto parseable por Wallet)."""
    c = canvas.Canvas(str(path), pagesize=A4, pageCompression=0)
    width, height = A4
    margin = 18 * mm
    content_w = width - 2 * margin

    # Fondo papel
    c.setFillColor(PAPER)
    c.rect(0, 0, width, height, fill=1, stroke=0)

    y = height - margin

    # Cabecera
    c.setFillColor(ACCENT)
    c.rect(margin, y - 42, content_w, 42, fill=1, stroke=0)
    c.setFillColor(colors.white)
    c.setFont("Helvetica-Bold", 18)
    c.drawString(margin + 14, y - 28, "Confirmación de reserva")
    c.setFont("Helvetica", 10)
    c.drawRightString(width - margin - 14, y - 20, "Estado: Confirmada")
    c.drawRightString(width - margin - 14, y - 32, "Reserva n.º BCN-88421")
    y -= 58

    c.setFillColor(INK)
    c.setFont("Helvetica", 10)
    c.drawString(margin, y, "Gracias por tu reserva. Presenta este documento en recepción.")
    y -= 22

    # Bloque hotel
    c.setStrokeColor(LINE)
    c.setFillColor(colors.white)
    c.roundRect(margin, y - 118, content_w, 118, 6, fill=1, stroke=1)
    box_y = y - 18
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, box_y, "ALOJAMIENTO")
    box_y -= 18
    c.setFont("Helvetica-Bold", 16)
    c.setFillColor(INK)
    c.drawString(margin + 14, box_y, "Hotel Casa Bonay")
    box_y -= 16
    c.setFont("Helvetica", 11)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, box_y, "Gran Via de les Corts Catalanes, 700")
    box_y -= 14
    c.drawString(margin + 14, box_y, "08010 Barcelona, España")
    box_y -= 14
    c.drawString(margin + 14, box_y, "Tel. +34 933 18 34 50 · recepcion@casabonay.com")
    y -= 132

    # Dos columnas: huésped / referencia
    col_w = (content_w - 12) / 2
    c.setFillColor(colors.white)
    c.roundRect(margin, y - 88, col_w, 88, 6, fill=1, stroke=1)
    c.roundRect(margin + col_w + 12, y - 88, col_w, 88, 6, fill=1, stroke=1)

    left_y = y - 16
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, left_y, "HUÉSPED PRINCIPAL")
    left_y -= 16
    c.setFont("Helvetica-Bold", 12)
    c.setFillColor(INK)
    c.drawString(margin + 14, left_y, "María García López")
    left_y -= 16
    c.setFont("Helvetica", 10)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, left_y, "maria.garcia@email.com")
    left_y -= 14
    c.drawString(margin + 14, left_y, "+34 612 345 678")

    right_x = margin + col_w + 12
    right_y = y - 16
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(right_x + 14, right_y, "REFERENCIA")
    right_y -= 16
    c.setFont("Helvetica-Bold", 11)
    c.setFillColor(INK)
    c.drawString(right_x + 14, right_y, "Confirmation number: 4829174630")
    right_y -= 14
    c.setFont("Helvetica", 10)
    c.drawString(right_x + 14, right_y, "PIN: 4829")
    right_y -= 14
    c.drawString(right_x + 14, right_y, "Reservado: 3 may 2026")
    y -= 102

    # Estancia — campos clave para el parser
    c.setFillColor(colors.white)
    c.roundRect(margin, y - 108, content_w, 108, 6, fill=1, stroke=1)
    stay_y = y - 16
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, stay_y, "DETALLES DE LA ESTANCIA")
    stay_y -= 22

    col1 = margin + 14
    col2 = margin + content_w / 2
    stay_y = _draw_label_value(c, col1, stay_y, "Check-in", "14 jun 2026 desde las 15:00")
    _draw_label_value(c, col2, stay_y + 34, "Check-out", "16 jun 2026 hasta las 11:00")
    stay_y = _draw_label_value(c, col1, stay_y, "Habitación", "Superior Double — cama doble")
    _draw_label_value(c, col2, stay_y + 34, "Noches", "2 noches · 2 adultos")
    y -= 122

    # Línea parseable explícita (backup para heurísticas)
    c.setFont("Helvetica", 10)
    c.setFillColor(MUTED)
    c.drawString(
        margin,
        y,
        "Hotel: Casa Bonay Barcelona · Booking confirmation · check-in 15:00",
    )
    y -= 28

    # Importe
    c.setFillColor(colors.white)
    c.roundRect(margin, y - 56, content_w, 56, 6, fill=1, stroke=1)
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, y - 18, "IMPORTE")
    c.setFont("Helvetica-Bold", 14)
    c.setFillColor(INK)
    c.drawString(margin + 14, y - 36, "Total pagado: 486,00 EUR (impuestos incluidos)")
    y -= 72

    # Política
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    policy = (
        "Cancelación gratuita hasta el 12 jun 2026, 18:00. "
        "Después de esa hora se cobrará la primera noche. "
        "Documento de identidad obligatorio en check-in."
    )
    text_obj = c.beginText(margin, y)
    text_obj.setFont("Helvetica", 9)
    text_obj.setFillColor(MUTED)
    for chunk in [policy[i : i + 92] for i in range(0, len(policy), 92)]:
        text_obj.textLine(chunk)
    c.drawText(text_obj)

    c.setFont("Helvetica", 8)
    c.drawCentredString(
        width / 2,
        margin / 2,
        "Documento de muestra MyOwnTrip · no válido como reserva real",
    )
    c.save()


if __name__ == "__main__":
    write_boarding_pass(OUT / "boarding-pass-ib3254-madrid-barcelona.pdf")
    write_hotel(OUT / "hotel-casa-bonay-reserva.pdf")
    print(f"Generados en {OUT}")
