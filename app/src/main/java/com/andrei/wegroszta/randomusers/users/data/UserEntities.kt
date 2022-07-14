package com.andrei.wegroszta.randomusers.users.data

//probably we don't need all this data
data class User(
    val gender: String,
    val name: NameDetails,
    val location: LocationDetails,
    val email: String,
    val login: LoginDetails,
    val dob: DateOfBirth,
    val registered: RegisterDetails,
    val phone: String,
    val cell: String,
    val id: UserIdentifier,
    val picture: UserPicture,
    val nat: String
)

data class NameDetails(
    val title: String,
    val firstName: String,
    val lastName: String
)

data class LocationDetails(
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    val coordinates: Coordinates,
    val timezone: TimezoneDetails
)

data class Street(
    val number: Long,
    val name: String
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

data class TimezoneDetails(
    val offset: String,
    val description: String
)

data class LoginDetails(
    val uuid: String,
    val username: String,
    val password: String,
    val salt: String,
    val md5: String,
    val sha1: String,
    val sha256: String
)

data class DateOfBirth(
    //should be converted to timestamp
    val date: String,
    val age: Int
)

data class RegisterDetails(
    //should be converted to timestamp
    val date: String,
    val age: Int
)

data class UserIdentifier(
    val name: String,
    val value: String?
)

data class UserPicture(
    val large: String,
    val medium: String,
    val thumbnail: String
)
