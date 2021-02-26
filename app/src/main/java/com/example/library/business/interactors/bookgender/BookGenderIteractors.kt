package com.example.library.business.interactors.bookgender

class BookGenderInteractors(
        val getBookGendersFromServer: GetBookGendersFromServer,
        val getBookGendersFromCache: GetBookGendersFromCache,
        val saveNewBookGender: SaveNewBookGender,
        val updateGender: UpdateGender,
        val synchronizeRemoteAndLocalGenders: SynchronizeRemoteAndLocalGenders,
        val removeGender: RemoveGender
)