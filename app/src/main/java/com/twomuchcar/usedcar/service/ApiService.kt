package com.twomuchcar.usedcar.firebase

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @PUT("/user/fcmToken")
    suspend fun updateFcmToken(@Body request: TokenRequest): Response<Unit>
}