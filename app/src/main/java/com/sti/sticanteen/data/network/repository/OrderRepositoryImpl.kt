package com.sti.sticanteen.data.network.repository

import androidx.datastore.core.DataStore
import com.sti.sticanteen.AppUserPreference
import com.sti.sticanteen.data.local.dao.OrderDao
import com.sti.sticanteen.data.mapper.mapToDomain
import com.sti.sticanteen.data.mapper.mapToEntity
import com.sti.sticanteen.data.network.api.CanteenApi
import com.sti.sticanteen.data.network.entity.Order
import com.sti.sticanteen.data.network.entity.OrderItem
import com.sti.sticanteen.domain.repository.OrderRepository
import com.sti.sticanteen.utils.Constants.networkError
import com.sti.sticanteen.utils.Constants.serverError
import com.sti.sticanteen.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject


class OrderRepositoryImpl @Inject constructor(
    private val canteenApi: CanteenApi,
    private val orderDao: OrderDao,
    private val appUserDataStore: DataStore<AppUserPreference>
) : OrderRepository {

    override suspend fun getOrders(): Flow<Resource<List<Order>>> = flow {
        emit(Resource.Loading())
        val ownerId = appUserDataStore.data.first().id.toLong()
        val fetchCachedOrders = orderDao.getOrders(ownerId)

        val transformToDomainOrders = fetchCachedOrders.map { orderWithItems ->
            orderWithItems.order.mapToDomain().copy(
                items = orderWithItems.orderItems.map { orderItemAndProduct ->
                    OrderItem(
                        id = orderItemAndProduct.orderItem.orderItemId,
                        product = orderItemAndProduct.product.mapToDomain(),
                        quantity = orderItemAndProduct.orderItem.quantity,
                        orderId = orderItemAndProduct.orderItem.orderId
                    )
                }
            )
        }

        if (fetchCachedOrders.isNotEmpty()) {
            emit(Resource.Loading(data = transformToDomainOrders))
        }

        try {
            val fetchNetworkOrders = canteenApi.getOrders()
            if (fetchNetworkOrders.isEmpty()) {
                orderDao.deleteOrders(ownerId = ownerId)
                return@flow emit(Resource.Success(data = emptyList()))
            }
            orderDao.insertOrder(fetchNetworkOrders.map { it.mapToEntity() })
            val orderItemsEntity = fetchNetworkOrders.map { order ->
                order.items.map { it.mapToEntity() }
            }
            orderItemsEntity.forEach { orderItems ->
                orderDao.insertOrderItems(orderItems)
            }
            emit(Resource.Success(data = fetchNetworkOrders))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    data = transformToDomainOrders,
                    message = e.message ?: networkError
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    data = transformToDomainOrders,
                    message = e.message ?: serverError
                )
            )
        }
    }
}