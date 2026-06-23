#!/usr/bin/env python3
"""Genera documentos de muestra para testeo: Wallet (PDF) y Gastos (tickets escaneados JPG)."""
from __future__ import annotations

import random
from io import BytesIO
from pathlib import Path

try:
    import qrcode
    from PIL import Image, ImageDraw, ImageEnhance, ImageFilter, ImageFont
    from reportlab.lib import colors
    from reportlab.lib.pagesizes import A4
    from reportlab.lib.units import mm
    from reportlab.lib.utils import ImageReader
    from reportlab.pdfgen import canvas
except ImportError as exc:
    raise SystemExit(
        "Instala dependencias: pip install qrcode pillow reportlab"
    ) from exc

ROOT = Path(__file__).resolve().parents[1]
WALLET_OUT = ROOT / "docs" / "samples" / "wallet"
EXPENSES_OUT = ROOT / "docs" / "samples" / "expenses"
WALLET_OUT.mkdir(parents=True, exist_ok=True)
EXPENSES_OUT.mkdir(parents=True, exist_ok=True)

# Viaje demo «Barcelona fin de semana» — alineado con preview Home / Wallet.
# Referencia «hoy»: 23 jun 2026 → viaje vie 4 – dom 6 jul (ver TRIP_* abajo).
TRIP_ISO_START = "2026-07-04"      # vuelo, hotel check-in, coche pick-up
TRIP_ISO_MID = "2026-07-05"        # Sagrada, teatro, tickets gastos
TRIP_ISO_END = "2026-07-06"        # AVE vuelta, Prado, check-out hotel
TRIP_ISO_CAR_RETURN = "2026-07-07" # devolución coche

TRIP_ES_START = "4 jul 2026"
TRIP_ES_MID = "5 jul 2026"
TRIP_ES_END = "6 jul 2026"
TRIP_ES_CAR_RETURN = "7 jul 2026"

TRIP_SLASH_START = "04/07/2026"
TRIP_SLASH_MID = "05/07/2026"
TRIP_SLASH_END = "06/07/2026"

INK = colors.HexColor("#1A1C1E")
MUTED = colors.HexColor("#49454F")
ACCENT = colors.HexColor("#825513")
PAPER = colors.HexColor("#F7F2EA")
LINE = colors.HexColor("#E0D8CC")
DARK = colors.HexColor("#0D1B2A")
GOLD = colors.HexColor("#B8860B")

BOARDING_QR_PAYLOAD = (
    "M1DEMO/PAX EIB3254 MADBCNIB 3254 314Y014A0001 "
    "349>5180  5140BIB              2A825513825513 0000"
)


def qr_image(payload: str, box_size: int = 6) -> ImageReader:
    qr = qrcode.QRCode(version=None, box_size=box_size, border=2)
    qr.add_data(payload)
    qr.make(fit=True)
    img = qr.make_image(fill_color="black", back_color="white")
    buf = BytesIO()
    img.save(buf, format="PNG")
    buf.seek(0)
    return ImageReader(buf)


def _draw_label_value(c: canvas.Canvas, x: float, y: float, label: str, value: str) -> float:
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(x, y, label)
    c.setFont("Helvetica-Bold", 11)
    c.setFillColor(INK)
    c.drawString(x, y - 14, value)
    return y - 34


def _footer_sample(c: canvas.Canvas, width: float, margin: float) -> None:
    c.setFont("Helvetica", 8)
    c.setFillColor(MUTED)
    c.drawCentredString(width / 2, margin / 2, "Documento de muestra MyOwnTrip · no válido como billete real")


def write_boarding_pass(path: Path) -> None:
    c = canvas.Canvas(str(path), pagesize=A4, pageCompression=0)
    width, height = A4
    y = height - 72
    lines = [
        "BOARDING PASS",
        "IB 3254",
        "Madrid - Barcelona",
        "Flight departure",
        TRIP_ISO_START,
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
    c.drawImage(qr_image(BOARDING_QR_PAYLOAD), width / 2 - 90, 180, width=180, height=180, mask="auto")
    c.setFont("Helvetica", 10)
    c.drawCentredString(width / 2, 160, "Escanea en puerta de embarque")
    _footer_sample(c, width, 18 * mm)
    c.save()


def write_hotel(path: Path) -> None:
    c = canvas.Canvas(str(path), pagesize=A4, pageCompression=0)
    width, height = A4
    margin = 18 * mm
    content_w = width - 2 * margin

    c.setFillColor(PAPER)
    c.rect(0, 0, width, height, fill=1, stroke=0)
    y = height - margin

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
    y -= 102

    c.setFillColor(colors.white)
    c.roundRect(margin, y - 108, content_w, 108, 6, fill=1, stroke=1)
    stay_y = y - 16
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, stay_y, "DETALLES DE LA ESTANCIA")
    stay_y -= 22
    col1 = margin + 14
    col2 = margin + content_w / 2
    stay_y = _draw_label_value(c, col1, stay_y, "Check-in", f"{TRIP_ES_START} desde las 15:00")
    _draw_label_value(c, col2, stay_y + 34, "Check-out", f"{TRIP_ES_END} hasta las 11:00")
    stay_y = _draw_label_value(c, col1, stay_y, "Habitación", "Superior Double — cama doble")
    _draw_label_value(c, col2, stay_y + 34, "Noches", "2 noches · 2 adultos")
    y -= 122

    c.setFont("Helvetica", 10)
    c.setFillColor(MUTED)
    c.drawString(margin, y, "Hotel: Casa Bonay Barcelona · Booking confirmation · check-in 15:00")
    y -= 28

    c.setFillColor(colors.white)
    c.roundRect(margin, y - 56, content_w, 56, 6, fill=1, stroke=1)
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, y - 18, "IMPORTE")
    c.setFont("Helvetica-Bold", 14)
    c.setFillColor(INK)
    c.drawString(margin + 14, y - 36, "Total pagado: 486,00 EUR (impuestos incluidos)")

    _footer_sample(c, width, margin)
    c.save()


def write_theater_ticket(path: Path) -> None:
    """Entrada teatro — estilo Teatro Real."""
    c = canvas.Canvas(str(path), pagesize=A4, pageCompression=0)
    width, height = A4
    margin = 20 * mm
    ticket_w = width - 2 * margin
    ticket_h = 130 * mm
    ticket_y = height - margin - ticket_h

    c.setFillColor(PAPER)
    c.rect(0, 0, width, height, fill=1, stroke=0)

    c.setFillColor(DARK)
    c.roundRect(margin, ticket_y, ticket_w, ticket_h, 8, fill=1, stroke=0)
    c.setFillColor(GOLD)
    c.rect(margin, ticket_y + ticket_h - 28, ticket_w, 28, fill=1, stroke=0)
    c.setFillColor(colors.white)
    c.setFont("Helvetica-Bold", 14)
    c.drawString(margin + 16, ticket_y + ticket_h - 20, "TEATRO REAL · MADRID")

    c.setFillColor(colors.white)
    c.setFont("Helvetica-Bold", 22)
    c.drawString(margin + 16, ticket_y + ticket_h - 58, "Las bodas de Fígaro")
    c.setFont("Helvetica", 11)
    c.setFillColor(colors.HexColor("#C8D6E5"))
    c.drawString(margin + 16, ticket_y + ticket_h - 74, "Wolfgang Amadeus Mozart · Opera en tres actos")

    info_y = ticket_y + ticket_h - 100
    fields = [
        ("Fecha", TRIP_ES_MID),
        ("Hora", "20:00"),
        ("Sector", "Platea"),
        ("Fila / Asiento", "12 / 18"),
        ("Localizador", "TR-8847291"),
    ]
    for label, value in fields:
        c.setFont("Helvetica", 8)
        c.setFillColor(colors.HexColor("#8899AA"))
        c.drawString(margin + 16, info_y, label.upper())
        c.setFont("Helvetica-Bold", 12)
        c.setFillColor(colors.white)
        c.drawString(margin + 16, info_y - 14, value)
        info_y -= 36

    qr = qr_image(f"TEATRO-REAL|FIGARO|{TRIP_ISO_MID}|20:00|PLATEA-12-18", box_size=4)
    c.drawImage(qr, margin + ticket_w - 110, ticket_y + 18, width=92, height=92, mask="auto")

    c.setFillColor(INK)
    c.setFont("Helvetica", 10)
    c.drawString(margin, ticket_y - 24, "Ticket · event: Las bodas de Fígaro · show teatro · entrada válida")
    c.drawString(margin, ticket_y - 40, "Presenta este código QR en el acceso. Puerta 15 min antes del inicio.")

    _footer_sample(c, width, margin)
    c.save()


def write_concert_ticket(path: Path) -> None:
    """Entrada concierto — estilo festival / sala grande."""
    c = canvas.Canvas(str(path), pagesize=A4, pageCompression=0)
    width, height = A4
    margin = 18 * mm
    content_w = width - 2 * margin

    c.setFillColor(colors.HexColor("#0F0F12"))
    c.rect(0, 0, width, height, fill=1, stroke=0)

    y = height - margin
    c.setFillColor(colors.HexColor("#E63946"))
    c.rect(margin, y - 50, content_w, 50, fill=1, stroke=0)
    c.setFillColor(colors.white)
    c.setFont("Helvetica-Bold", 20)
    c.drawString(margin + 14, y - 32, "MAD COOL FESTIVAL 2026")

    y -= 68
    c.setFont("Helvetica-Bold", 28)
    c.drawString(margin + 14, y, "Arctic Monkeys")
    y -= 22
    c.setFont("Helvetica", 12)
    c.setFillColor(colors.HexColor("#A8B2C1"))
    c.drawString(margin + 14, y, "Viernes · Escenario principal · concierto")
    y -= 36

    c.setFillColor(colors.HexColor("#1A1D24"))
    c.roundRect(margin, y - 120, content_w, 120, 6, fill=1, stroke=0)
    inner_y = y - 22
    for label, value in [
        ("Evento", "Mad Cool Festival — Viernes"),
        ("Fecha", "12 jul 2026"),
        ("Hora acceso", "18:30"),
        ("Hora concierto", "21:45"),
        ("Tipo entrada", "General · Pie de pista"),
        ("Código", "MCF-26-VIE-447821"),
    ]:
        c.setFont("Helvetica", 8)
        c.setFillColor(colors.HexColor("#6B7280"))
        c.drawString(margin + 14, inner_y, label.upper())
        c.setFont("Helvetica-Bold", 11)
        c.setFillColor(colors.white)
        c.drawString(margin + 14, inner_y - 12, value)
        inner_y -= 28
    y -= 136

    qr = qr_image("MCF2026|FRI|ARCTIC|2026-07-12|1830|GEN")
    c.drawImage(qr, width / 2 - 70, y - 150, width=140, height=140, mask="auto")
    c.setFont("Helvetica", 9)
    c.setFillColor(colors.HexColor("#8899AA"))
    c.drawCentredString(width / 2, y - 162, "Ticket · entrada · evento")

    _footer_sample(c, width, margin)
    c.save()


def write_museum_ticket(path: Path) -> None:
    """Entrada museo — estilo Museo del Prado."""
    c = canvas.Canvas(str(path), pagesize=A4, pageCompression=0)
    width, height = A4
    margin = 18 * mm
    content_w = width - 2 * margin

    c.setFillColor(PAPER)
    c.rect(0, 0, width, height, fill=1, stroke=0)
    y = height - margin

    c.setFillColor(colors.HexColor("#2C1810"))
    c.roundRect(margin, y - 44, content_w, 44, 4, fill=1, stroke=0)
    c.setFillColor(colors.white)
    c.setFont("Helvetica-Bold", 16)
    c.drawString(margin + 14, y - 28, "MUSEO NACIONAL DEL PRADO")
    y -= 58

    c.setFillColor(colors.white)
    c.setStrokeColor(LINE)
    c.roundRect(margin, y - 200, content_w, 200, 6, fill=1, stroke=1)
    box_y = y - 20
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, box_y, "ENTRADA")
    box_y -= 18
    c.setFont("Helvetica-Bold", 18)
    c.setFillColor(INK)
    c.drawString(margin + 14, box_y, "Ticket general · museo")
    box_y -= 20
    c.setFont("Helvetica", 11)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, box_y, "Paseo del Prado, s/n · Madrid")
    box_y -= 28

    col1 = margin + 14
    col2 = margin + content_w / 2
    box_y = _draw_label_value(c, col1, box_y, "Fecha visita", TRIP_ES_END)
    _draw_label_value(c, col2, box_y + 34, "Franja horaria", "10:30")
    box_y = _draw_label_value(c, col1, box_y, "Titular", "María García López")
    _draw_label_value(c, col2, box_y + 34, "N.º entrada", "PRD-2026-9182734")
    y -= 216

    qr = qr_image(f"PRADO|GEN|{TRIP_ISO_END}|1030|9182734")
    c.drawImage(qr, margin + 14, y - 130, width=110, height=110, mask="auto")
    c.setFont("Helvetica", 10)
    c.setFillColor(MUTED)
    c.drawString(margin + 140, y - 40, "Event: Museo del Prado — entrada general")
    c.drawString(margin + 140, y - 56, "Válida solo para la fecha y franja indicadas.")
    c.drawString(margin + 140, y - 72, "Acceso por Puerta de Goya. tour / museum ticket.")

    _footer_sample(c, width, margin)
    c.save()


def write_sagrada_familia_ticket(path: Path) -> None:
    """Entrada monumento — Basílica Sagrada Família."""
    c = canvas.Canvas(str(path), pagesize=A4, pageCompression=0)
    width, height = A4
    margin = 18 * mm
    content_w = width - 2 * margin

    c.setFillColor(colors.HexColor("#F0EBE3"))
    c.rect(0, 0, width, height, fill=1, stroke=0)
    y = height - margin

    c.setFillColor(colors.HexColor("#4A6741"))
    c.roundRect(margin, y - 48, content_w, 48, 6, fill=1, stroke=0)
    c.setFillColor(colors.white)
    c.setFont("Helvetica-Bold", 15)
    c.drawString(margin + 14, y - 30, "BASÍLICA DE LA SAGRADA FAMÍLIA")
    y -= 62

    c.setFillColor(colors.white)
    c.setStrokeColor(LINE)
    c.roundRect(margin, y - 175, content_w, 175, 6, fill=1, stroke=1)
    inner = y - 18
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, inner, "TICKET · ENTRADA CON TORRE")
    inner -= 20
    c.setFont("Helvetica-Bold", 16)
    c.setFillColor(INK)
    c.drawString(margin + 14, inner, "Ticket event: Sagrada Familia")
    inner -= 22
    c.setFont("Helvetica", 10)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, inner, "Carrer de Mallorca, 401 · Barcelona")
    inner -= 30
    inner = _draw_label_value(c, margin + 14, inner, "Fecha", TRIP_ES_MID)
    inner = _draw_label_value(c, margin + 14, inner, "Hora", "11:00")
    inner = _draw_label_value(c, margin + 14, inner, "Torre", "Natividad · ascensor incluido")
    _draw_label_value(c, margin + 14, inner, "Localizador", "SF-BCN-7721045")
    y -= 190

    qr = qr_image(f"SAGRADA|NAT|{TRIP_ISO_MID}|1100|7721045")
    c.drawImage(qr, width - margin - 120, y - 115, width=100, height=100, mask="auto")
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(margin, y - 24, "excursion · show · entrada. Llega 15 min antes. Documento identidad obligatorio.")

    _footer_sample(c, width, margin)
    c.save()


def write_ave_ticket(path: Path) -> None:
    """Billete AVE Renfe."""
    c = canvas.Canvas(str(path), pagesize=A4, pageCompression=0)
    width, height = A4
    margin = 18 * mm
    content_w = width - 2 * margin

    c.setFillColor(colors.HexColor("#7B0B2E"))
    c.rect(0, 0, width, height, fill=1, stroke=0)
    y = height - margin

    c.setFillColor(colors.white)
    c.setFont("Helvetica-Bold", 22)
    c.drawString(margin, y, "renfe")
    c.setFont("Helvetica-Bold", 14)
    c.drawRightString(width - margin, y, "AVE 03142")
    y -= 28
    c.setFont("Helvetica", 11)
    c.drawString(margin, y, "Billete electrónico · tren alta velocidad")
    y -= 36

    c.setFillColor(colors.white)
    c.roundRect(margin, y - 155, content_w, 155, 6, fill=1, stroke=0)
    inner_y = y - 22
    c.setFillColor(INK)
    c.setFont("Helvetica-Bold", 20)
    c.drawString(margin + 16, inner_y, "Madrid-Puerta de Atocha")
    inner_y -= 24
    c.setFont("Helvetica", 10)
    c.setFillColor(MUTED)
    c.drawString(margin + 16, inner_y, "Salida")
    inner_y -= 18
    c.setFont("Helvetica-Bold", 14)
    c.setFillColor(INK)
    c.drawString(margin + 16, inner_y, f"{TRIP_SLASH_END} · 18:30")
    inner_y -= 28
    c.setFont("Helvetica-Bold", 20)
    c.drawString(margin + 16, inner_y, "Barcelona-Sants")
    inner_y -= 24
    c.setFont("Helvetica", 10)
    c.setFillColor(MUTED)
    c.drawString(margin + 16, inner_y, "Llegada estimada 21:12")
    inner_y -= 30
    c.setFont("Helvetica-Bold", 11)
    c.setFillColor(INK)
    c.drawString(margin + 16, inner_y, "Pasajero: María García · Coche 4 · Asiento 12C · Turista")

    y -= 170
    c.setFillColor(colors.HexColor("#F5E6EB"))
    c.roundRect(margin, y - 70, content_w, 70, 4, fill=1, stroke=0)
    c.setFillColor(INK)
    c.setFont("Helvetica", 10)
    c.drawString(margin + 14, y - 22, "Localizador: RENFE-AV-03142-88421")
    c.drawString(margin + 14, y - 38, "RENFE AVE 03142 Madrid - Barcelona · pickup tarjeta o QR")

    qr = qr_image(f"RENFE|AVE03142|MAD|BCN|{TRIP_ISO_END}|1830|12C")
    c.drawImage(qr, width - margin - 95, y - 62, width=78, height=78, mask="auto")

    _footer_sample(c, width, margin)
    c.save()


def write_car_rental(path: Path) -> None:
    """Confirmación alquiler coche — estilo operador."""
    c = canvas.Canvas(str(path), pagesize=A4, pageCompression=0)
    width, height = A4
    margin = 18 * mm
    content_w = width - 2 * margin

    c.setFillColor(PAPER)
    c.rect(0, 0, width, height, fill=1, stroke=0)
    y = height - margin

    c.setFillColor(colors.HexColor("#005EB8"))
    c.roundRect(margin, y - 40, content_w, 40, 4, fill=1, stroke=0)
    c.setFillColor(colors.white)
    c.setFont("Helvetica-Bold", 16)
    c.drawString(margin + 14, y - 26, "Europcar · Confirmación de alquiler")
    y -= 54

    c.setFillColor(colors.white)
    c.setStrokeColor(LINE)
    c.roundRect(margin, y - 200, content_w, 200, 6, fill=1, stroke=1)
    inner = y - 18
    c.setFont("Helvetica", 9)
    c.setFillColor(MUTED)
    c.drawString(margin + 14, inner, "RESERVA CONFIRMADA · RENTAL / CAR HIRE")
    inner -= 20
    c.setFont("Helvetica-Bold", 14)
    c.setFillColor(INK)
    c.drawString(margin + 14, inner, "Grupo B — Compacto (VW Golf o similar)")
    inner -= 28
    col1 = margin + 14
    col2 = margin + content_w / 2
    inner = _draw_label_value(c, col1, inner, "Pick-up", f"{TRIP_ES_START} · 10:00")
    _draw_label_value(c, col2, inner + 34, "Devolución", f"{TRIP_ES_CAR_RETURN} · 10:00")
    inner = _draw_label_value(c, col1, inner, "Oficina recogida", "Aeropuerto Madrid-Barajas T4")
    _draw_label_value(c, col2, inner + 34, "N.º reserva", "EC-7842910")
    inner = _draw_label_value(c, col1, inner, "Conductor", "María García López")
    _draw_label_value(c, col2, inner + 34, "Combustible", "Lleno / lleno")

    c.setFont("Helvetica", 10)
    c.setFillColor(MUTED)
    c.drawString(margin, y - 218, "alquiler coche · transfer aeropuerto. Presenta carnet y tarjeta en mostrador.")

    _footer_sample(c, width, margin)
    c.save()


def _load_mono_font(size: int) -> ImageFont.FreeTypeFont | ImageFont.ImageFont:
    candidates = [
        "/System/Library/Fonts/Supplemental/Courier New.ttf",
        "/System/Library/Fonts/Monaco.ttf",
        "/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf",
        "/usr/share/fonts/truetype/liberation/LiberationMono-Regular.ttf",
    ]
    for path in candidates:
        if Path(path).exists():
            return ImageFont.truetype(path, size)
    return ImageFont.load_default()


def _draw_receipt_lines(
    draw: ImageDraw.ImageDraw,
    font: ImageFont.ImageFont,
    font_bold: ImageFont.ImageFont,
    lines: list[tuple[str, str, bool]],
    x: int,
    y: int,
    width: int,
) -> int:
    for left, right, bold in lines:
        f = font_bold if bold else font
        draw.text((x, y), left, fill="#1A1A1A", font=f)
        if right:
            tw = draw.textlength(right, font=f)
            draw.text((x + width - tw, y), right, fill="#1A1A1A", font=f)
        y += 22 if bold else 20
    return y


def _apply_scan_effect(img: Image.Image, seed: int) -> Image.Image:
    rng = random.Random(seed)
    img = img.convert("RGB")

    # Ligera rotación y fondo crema
    angle = rng.uniform(-1.8, 1.2)
    bg = (245, 240, 232)
    img = img.rotate(angle, expand=True, fillcolor=bg)

    # Ruido sutil
    pixels = img.load()
    w, h = img.size
    for _ in range(int(w * h * 0.04)):
        px = rng.randint(0, w - 1)
        py = rng.randint(0, h - 1)
        base = pixels[px, py]
        delta = rng.randint(-18, 18)
        pixels[px, py] = tuple(max(0, min(255, c + delta)) for c in base)

    img = img.filter(ImageFilter.GaussianBlur(radius=0.35))
    img = ImageEnhance.Contrast(img).enhance(0.92)
    img = ImageEnhance.Brightness(img).enhance(0.97)

    # Sombra de borde (efecto foto sobre mesa)
    shadow = Image.new("RGB", (w + 40, h + 40), bg)
    shadow.paste(img, (20, 20))
    return shadow


def write_restaurant_receipt(path: Path) -> None:
    width, height = 420, 780
    img = Image.new("RGB", (width, height), "#FAFAF8")
    draw = ImageDraw.Draw(img)
    font = _load_mono_font(16)
    font_sm = _load_mono_font(14)
    font_bold = _load_mono_font(17)

    y = 28
    for line in [
        "LA TASQUITA DE ENRIQUE",
        "C/ Ballesta, 6 - MADRID",
        "Tlf: 915 32 45 67",
        "CIF: B86543210",
    ]:
        tw = draw.textlength(line, font=font_bold)
        draw.text(((width - tw) / 2, y), line, fill="#111", font=font_bold)
        y += 24
    y += 8
    draw.line((24, y, width - 24, y), fill="#333", width=1)
    y += 16

    y = _draw_receipt_lines(
        draw,
        font_sm,
        font_sm,
        [
            ("Mesa: 7", "Camarero: 03", False),
            (TRIP_SLASH_MID, "14:32", False),
        ],
        28,
        y,
        width - 56,
    )
    y += 4
    draw.line((24, y, width - 24, y), fill="#333", width=1)
    y += 14

    items = [
        ("2  Croqueta jamón ibérico", "14,00"),
        ("1  Ensaladilla rusa", "4,50"),
        ("1  Tortilla de patatas", "9,00"),
        ("2  Copa vino tinto", "8,00"),
        ("1  Agua 50cl", "2,50"),
        ("1  Café solo", "1,80"),
    ]
    for left, right in items:
        y = _draw_receipt_lines(draw, font_sm, font_sm, [(left, right, False)], 28, y, width - 56)

    y += 6
    draw.line((24, y, width - 24, y), fill="#333", width=1)
    y += 14
    y = _draw_receipt_lines(
        draw,
        font_sm,
        font_bold,
        [
            ("Base imponible", "32,95", False),
            ("IVA 10%", "3,85", False),
            ("TOTAL", "39,80", True),
        ],
        28,
        y,
        width - 56,
    )
    y += 8
    draw.line((24, y, width - 24, y), fill="#333", width=1)
    y += 16
    for line in ["IVA INCLUIDO", "TARJETA **** 4821", "Gracias por su visita"]:
        tw = draw.textlength(line, font=font_sm)
        draw.text(((width - tw) / 2, y), line, fill="#333", font=font_sm)
        y += 22

    scanned = _apply_scan_effect(img, seed=42)
    scanned.save(path, format="JPEG", quality=82, optimize=True)


def write_supermarket_receipt(path: Path) -> None:
    width, height = 400, 920
    img = Image.new("RGB", (width, height), "#FCFCFA")
    draw = ImageDraw.Draw(img)
    font = _load_mono_font(15)
    font_sm = _load_mono_font(13)
    font_bold = _load_mono_font(16)

    y = 24
    for line in ["MERCADONA S.A.", "Tienda 1847 - Barcelona", "Av. Diagonal, 234"]:
        tw = draw.textlength(line, font=font_bold if line.startswith("MERCADONA") else font)
        f = font_bold if line.startswith("MERCADONA") else font
        draw.text(((width - tw) / 2, y), line, fill="#0A5C36" if line.startswith("MERCADONA") else "#111", font=f)
        y += 22
    y += 6
    draw.line((20, y, width - 20, y), fill="#444", width=1)
    y += 12
    y = _draw_receipt_lines(
        draw,
        font_sm,
        font_sm,
        [("21/06/2026", "19:45", False), ("Op: 042", "Caja 03", False)],
        24,
        y,
        width - 48,
    )
    y += 4
    draw.line((20, y, width - 20, y), fill="#444", width=1)
    y += 12

    products = [
        ("LECHE DESNATADA 1L", "0,85"),
        ("PAN DE MOLDE 450G", "1,15"),
        ("TOMATE RAF 0,6 KG", "2,40"),
        ("ACEITE OLIVA 1L", "6,95"),
        ("AGUA MINERAL 6x1,5L", "2,58"),
        ("YOGUR NATURAL 4U", "1,89"),
        ("HUEVOS L 12U", "2,45"),
        ("QUESO MANCHEGO 250G", "4,20"),
        ("JAMÓN COCIDO 200G", "2,35"),
        ("DETERGENTE ROPA 1,5L", "3,10"),
        ("PAPEL HIGIÉNICO 12U", "4,25"),
        ("PLÁTANO CANARIO KG", "1,95"),
    ]
    for left, right in products:
        y = _draw_receipt_lines(draw, font_sm, font_sm, [(left, right, False)], 24, y, width - 48)

    y += 6
    draw.line((20, y, width - 20, y), fill="#444", width=1)
    y += 12
    y = _draw_receipt_lines(
        draw,
        font_sm,
        font_bold,
        [("TOTAL", "34,12", True), ("TARJETA", "**** 4821", False)],
        24,
        y,
        width - 48,
    )
    y += 10
    msg = "Gracias por comprar en Mercadona"
    tw = draw.textlength(msg, font=font_sm)
    draw.text(((width - tw) / 2, y), msg, fill="#333", font=font_sm)

    scanned = _apply_scan_effect(img, seed=77)
    scanned.save(path, format="JPEG", quality=80, optimize=True)


def write_cafe_receipt(path: Path) -> None:
    width, height = 360, 520
    img = Image.new("RGB", (width, height), "#FEFEFC")
    draw = ImageDraw.Draw(img)
    font = _load_mono_font(15)
    font_sm = _load_mono_font(13)
    font_bold = _load_mono_font(16)

    y = 30
    for line in ["CAFÉ COMERCIAL", "Pl. de la Catalunya, 2", "Barcelona"]:
        tw = draw.textlength(line, font=font_bold if "CAFÉ" in line else font)
        f = font_bold if "CAFÉ" in line else font
        draw.text(((width - tw) / 2, y), line, fill="#111", font=f)
        y += 22
    y += 8
    draw.line((22, y, width - 22, y), fill="#333", width=1)
    y += 14
    y = _draw_receipt_lines(
        draw,
        font_sm,
        font_sm,
        [("21/06/2026", "09:18", False), ("Ticket: 8847", "", False)],
        26,
        y,
        width - 52,
    )
    y += 4
    draw.line((22, y, width - 22, y), fill="#333", width=1)
    y += 12
    for left, right in [
        ("Café con leche", "2,20"),
        ("Croissant mantequilla", "2,80"),
        ("Zumo naranja", "3,50"),
    ]:
        y = _draw_receipt_lines(draw, font_sm, font_sm, [(left, right, False)], 26, y, width - 52)
    y += 6
    draw.line((22, y, width - 22, y), fill="#333", width=1)
    y += 12
    y = _draw_receipt_lines(draw, font_sm, font_bold, [("TOTAL", "8,50", True)], 26, y, width - 52)
    y += 8
    tw = draw.textlength("Gracias!", font=font_sm)
    draw.text(((width - tw) / 2, y), "Gracias!", fill="#333", font=font_sm)

    scanned = _apply_scan_effect(img, seed=13)
    scanned.save(path, format="JPEG", quality=83, optimize=True)


WALLET_SAMPLES = [
    ("boarding-pass-ib3254-madrid-barcelona.pdf", write_boarding_pass),
    ("hotel-casa-bonay-reserva.pdf", write_hotel),
    ("teatro-real-las-bodas-de-figaro.pdf", write_theater_ticket),
    ("concierto-mad-cool-arctic-monkeys.pdf", write_concert_ticket),
    ("museo-prado-entrada-general.pdf", write_museum_ticket),
    ("entrada-sagrada-familia-5jul.pdf", write_sagrada_familia_ticket),
    ("renfe-ave-03142-madrid-barcelona.pdf", write_ave_ticket),
    ("europcar-alquiler-coche-madrid.pdf", write_car_rental),
]

EXPENSE_SAMPLES = [
    ("ticket-restaurante-la-tasquita.jpg", write_restaurant_receipt),
    ("ticket-supermercado-mercadona.jpg", write_supermarket_receipt),
    ("ticket-cafeteria-tpv.jpg", write_cafe_receipt),
]


def main() -> None:
    for name, writer in WALLET_SAMPLES:
        writer(WALLET_OUT / name)
        print(f"  wallet/{name}")
    for name, writer in EXPENSE_SAMPLES:
        writer(EXPENSES_OUT / name)
        print(f"  expenses/{name}")
    print(f"\nGenerados {len(WALLET_SAMPLES)} PDFs en {WALLET_OUT}")
    print(f"Generados {len(EXPENSE_SAMPLES)} JPG en {EXPENSES_OUT}")

    sync = ROOT / "scripts" / "sync-samples-to-debug-assets.sh"
    if sync.exists():
        import subprocess
        subprocess.run(["bash", str(sync)], check=True)


if __name__ == "__main__":
    main()
