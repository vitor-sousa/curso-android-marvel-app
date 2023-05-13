package com.example.core.data.mapper

import com.example.core.data.StorageConstants
import com.example.core.domain.model.SortingType.ORDER_ASCENDING
import com.example.core.domain.model.SortingType.ORDER_DESCENDING
import com.example.core.domain.model.SortingType.ORDER_BY_NAME
import com.example.core.domain.model.SortingType.ORDER_BY_MODIFIED
import javax.inject.Inject

class SortingMapper @Inject constructor() {

    fun mapToPair(sorting: String): Pair<String, String> {
        val nameAscending = ORDER_BY_NAME.value to ORDER_ASCENDING.value
        return when (sorting) {
            StorageConstants.ORDER_BY_NAME_ASCENDING -> nameAscending
            StorageConstants.ORDER_BY_NAME_DESCENDING -> ORDER_BY_NAME.value to ORDER_DESCENDING.value
            StorageConstants.ORDER_BY_MODIFIED_ASCENDING -> ORDER_BY_MODIFIED.value to ORDER_ASCENDING.value
            StorageConstants.ORDER_BY_MODIFIED_DESCENDING -> ORDER_BY_MODIFIED.value to ORDER_DESCENDING.value
            else -> nameAscending
        }
    }

    fun mapFromPair(sortingPair: Pair<String, String>): String {
        val orderBy = sortingPair.first
        val order = sortingPair.second

        return when (orderBy) {
            ORDER_BY_NAME.value -> when (order) {
                ORDER_ASCENDING.value -> StorageConstants.ORDER_BY_NAME_ASCENDING
                ORDER_DESCENDING.value -> StorageConstants.ORDER_BY_NAME_DESCENDING
                else -> StorageConstants.ORDER_BY_NAME_ASCENDING
            }
            ORDER_BY_MODIFIED.value -> when (order) {
                ORDER_ASCENDING.value -> StorageConstants.ORDER_BY_MODIFIED_ASCENDING
                ORDER_DESCENDING.value -> StorageConstants.ORDER_BY_MODIFIED_DESCENDING
                else -> StorageConstants.ORDER_BY_MODIFIED_ASCENDING
            }
            else -> StorageConstants.ORDER_BY_NAME_ASCENDING
        }
    }
}