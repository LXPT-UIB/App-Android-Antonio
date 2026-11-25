package com.amari.mesalistapruebas.data.model

import com.amari.mesalistapruebas.data.repository.ReservationClient


class ReservationsResponse(
    val reservations: List<ReservationClient>
)