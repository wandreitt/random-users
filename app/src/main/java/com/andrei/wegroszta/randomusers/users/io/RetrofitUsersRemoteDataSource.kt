package com.andrei.wegroszta.randomusers.users.io

import com.andrei.wegroszta.randomusers.users.data.*
import com.andrei.wegroszta.randomusers.users.data.UsersRepository.UsersRemoteDataSource
import javax.inject.Inject
import javax.inject.Named

class RetrofitUsersRemoteDataSource @Inject constructor(
    private val service: UsersAPIService,
    @Named("resultsPerPage")
    private val resultsPerPage: Int,
    @Named("seed")
    private val seed: String
) : UsersRemoteDataSource {

    override suspend fun loadUsers(page: Int): List<User> {
        val networkResults = service.getUsers(page, resultsPerPage, seed).results
        return networkResults.map { it.toUser() }
    }

    private fun NetworkUser.toUser() = User(
        gender = this.gender,
        name = this.name.toNameDetails(),
        location = this.location.toLocationDetails(),
        email = this.email,
        login = this.login.toLoginDetails(),
        dob = this.dob.toDateOfBirth(),
        registered = this.registered.toRegisterDetails(),
        phone = this.phone,
        cell = this.cell,
        id = this.id.toUserIdentifier(),
        picture = this.picture.toUserPicture(),
        nat = this.nat
    )

    private fun NetworkNameDetails.toNameDetails() = NameDetails(
        title = this.title,
        firstName = this.first,
        lastName = this.last
    )

    private fun NetworkLocationDetails.toLocationDetails() = LocationDetails(
        street = this.street.toStreet(),
        city = this.city,
        state = this.state,
        country = this.country,
        postcode = this.postcode,
        coordinates = this.coordinates.toCoordinates(),
        timezone = this.timezone.toTimezoneDetails()
    )

    private fun NetworkStreet.toStreet() = Street(
        number = this.number,
        name = this.name
    )

    private fun NetworkCoordinates.toCoordinates() = Coordinates(
        latitude = this.latitude,
        longitude = this.longitude
    )

    private fun NetworkTimezone.toTimezoneDetails() = TimezoneDetails(
        offset = this.offset,
        description = this.description
    )

    private fun NetworkLoginDetails.toLoginDetails() = LoginDetails(
        uuid = this.uuid,
        username = this.username,
        password = this.password,
        salt = this.salt,
        md5 = this.md5,
        sha1 = this.sha1,
        sha256 = this.sha256
    )

    private fun NetworkDOB.toDateOfBirth() = DateOfBirth(
        date = this.date,
        age = this.age
    )

    private fun NetworkRegistered.toRegisterDetails() = RegisterDetails(
        date = this.date,
        age = this.age
    )

    private fun NetworkId.toUserIdentifier() = UserIdentifier(
        name = this.name,
        value = this.value
    )

    private fun NetworkPicture.toUserPicture() = UserPicture(
        large = this.large,
        medium = this.medium,
        thumbnail = this.thumbnail
    )
}
