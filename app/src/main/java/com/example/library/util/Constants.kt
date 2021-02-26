package com.example.library.util

object Constants {

    const val COLLECTION_BOOKS = "BOOKS"
    const val COLLECTION_BOOKS_GENDER = "BOOKS_GENDER"
    const val COLLECTION_OWNER = "COLLECTION_OWNER"

    const val DB_NAME = "BOOKS_DB"

    //region touchListener threshold
    const val SWIPE_THRESHOLD = 100
    const val SWIPE_VELOCITY_THRESHOLD = 100
    //endregion

    //region successful CRUD operation
    const val UPDATE_PERFORMED_SUCCESSFUL = "Update performed successful"
    const val INSERT_PERFORMED_SUCCESSFUL = "Insert performed successful"
    const val DELETE_PERFORMED_SUCCESSFUL = "Delete performed sucessful"

    //region error on CRUD operation
    const val ERROR_TRYING_TO_PERFORM_UPDATE = "Error trying to update"
    const val ERROR_TRYING_TO_PERFORM_INSERT = "Error trying to insert"
    const val ERROR_TRYING_TO_GET_DATA = "Error trying to get data"
    const val ERROR_TRYING_TO_SYNCHRONIZE_SERVER_AND_CACHE = "Error trying to synchronize server and cache"

    //endregion

    //region network error
    const val ERROR_TRYING_TO_GET_REMOTE_GENDER = "Error trying to get remote gender"
    //endregion



}