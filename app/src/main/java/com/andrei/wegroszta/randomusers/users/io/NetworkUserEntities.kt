package com.andrei.wegroszta.randomusers.users.io

import kotlinx.serialization.Serializable

@Serializable
data class NetworkUserResponseWrapper(
    val results: List<NetworkUser>
)

//todo decide if any of these is nullable
@Serializable
data class NetworkUser(
    val gender: String,
    val name: NetworkNameDetails,
    val location: NetworkLocationDetails,
    val email: String,
    val login: NetworkLoginDetails,
    val dob: NetworkDOB,
    val registered: NetworkRegistered,
    val phone: String,
    val cell: String,
    val id: NetworkId,
    val picture: NetworkPicture,
    val nat: String
)

@Serializable
data class NetworkNameDetails(
    val title: String,
    val first: String,
    val last: String
)

@Serializable
data class NetworkLocationDetails(
    val street: NetworkStreet,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    val coordinates: NetworkCoordinates,
    val timezone: NetworkTimezone
)

@Serializable
data class NetworkStreet(
    val number: Long,
    val name: String
)

@Serializable
data class NetworkCoordinates(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class NetworkTimezone(
    val offset: String,
    val description: String
)

@Serializable
data class NetworkLoginDetails(
    val uuid: String,
    val username: String,
    val password: String,
    val salt: String,
    val md5: String,
    val sha1: String,
    val sha256: String
)

@Serializable
data class NetworkDOB(
    //should be converted to timestamp
    val date: String,
    val age: Int
)

@Serializable
data class NetworkRegistered(
    //should be converted to timestamp
    val date: String,
    val age: Int
)

@Serializable
data class NetworkId(
    val name: String,
    val value: String?
)

@Serializable
data class NetworkPicture(
    val large: String,
    val medium: String,
    val thumbnail: String
)
