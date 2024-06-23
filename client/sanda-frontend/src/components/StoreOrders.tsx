import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getOrders, getStoreOrders } from '../API'; 
import { OrderModel } from '../models/OrderModel'; 
import '../styles/storeOrders.css'; 

export const StoreOrders = () => {
    const { username } = useParams<{ username: string }>() 
    const [orders, setOrders] = useState<OrderModel[]>([]);
    const [storeName, setStoreName] = useState<string>('');

    const handleStoreNameChange = async(event: React.ChangeEvent<HTMLInputElement>) => {
        setStoreName(event.target.value);
        try{
            const fetchedOrders = await getStoreOrders(Number.parseInt(storeName),username!);
            setOrders(fetchedOrders);
            }catch(e){
                alert("Error occoured please try again later");
            }
    };

    const handleSearchSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        // Add logic to fetch and filter orders by store name if needed
    };

    return (
        <div className="main">
            <div className="page-title">Store Orders</div>
            <form className="search-form" onSubmit={handleSearchSubmit}>
                <input 
                    type="text" 
                    placeholder="Enter store id..." 
                    value={storeName} 
                    onChange={handleStoreNameChange} 
                    className="search-input"
                />
            </form>
            <div className="orders-grid">
                {orders.map((order) => (
                    <div className="order-container" key={order.id}>
                        <div className="order-header">
                            <div className="order-header-left-section">
                                <div className="order-date">
                                    <div className="order-header-label">Order Placed:</div>
                                    <div>{order.date}</div>
                                </div>
                                <div className="order-total">
                                    <div className="order-header-label">Total:</div>
                                    <div>${order.total}</div>
                                </div>
                            </div>
                            <div className="order-header-right-section">
                                <div className="order-header-label">Order ID:</div>
                                <div>{order.id}</div>
                            </div>
                        </div>
                        <div className="order-details-grid">
                            {order.products.map((product) => (
                                <React.Fragment key={product.id}>
                                    <div className="product-details">
                                        <div className="product-name">{product.name}</div>
                                        <div className="store-name">Store: {product.storeId}</div>
                                        <div className="product-quantity">Quantity: {product.quantity}</div>
                                    </div>
                                    <div className="product-actions">
                                        <div className="product-price">Price Before: ${product.oldPrice}</div>
                                        <div className="product-price">Price After: ${product.newPrice}</div>
                                    </div>
                                </React.Fragment>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default StoreOrders;
