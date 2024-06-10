import React, { useEffect, useState } from 'react';
import '../styles/cart.css';
// Assuming these interfaces and functions are imported correctly
import { viewCart, updateCart } from '../API'; // Add necessary API functions
import cartModel from '../models/CartModel';
import { useParams } from 'react-router-dom';

const Cart = () => {
    const { username } = useParams();
    const [cart, setCart] = useState({ products: [], totalPrice: 0, discountedPrice: 0 } as cartModel);

    useEffect(() => {
        const cartData = viewCart(username as string);
        setCart(cartData);
    }, [username]);

    const handleQuantityChange = (index: number, event: React.ChangeEvent<HTMLSelectElement>) => {
        const updatedCart = { ...cart };
        updatedCart.products[index].amount = parseInt(event.target.value, 10);
        updateCart(updatedCart); // Add API call to update the cart
        setCart(updatedCart);
    };

    const handleRemoveProduct = (index: number) => {
        const updatedCart = { ...cart };
        updatedCart.products.splice(index, 1);
        updateCart(updatedCart); // Add API call to update the cart
        setCart(updatedCart);
    };

    if (!cart) return <div>Loading...</div>;

    return (
        <div className="cart-container">
            <h2>Shopping Cart</h2>
            <div className="cart-items">
                {cart.products.map((product, index) => (
                    <div key={index} className="cart-item">
                        <div className="item-details">
                            <h3>{product.name}</h3>
                            <div className="item-quantity">
                                <label htmlFor={`quantity-${index}`}>Quantity: </label>
                                <select
                                    id={`quantity-${index}`}
                                    value={product.amount}
                                    onChange={(event) => handleQuantityChange(index, event)}
                                >
                                    {[...Array(10)].map((_, n) => (
                                        <option key={n + 1} value={n + 1}>
                                            {n + 1}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <p>Price: ${product.originalPrice}</p>
                            <p>Discounted Price: ${product.discountedPrice}</p>
                        </div>
                        <button className="button remove" onClick={() => handleRemoveProduct(index)}>X</button>
                    </div>
                ))}
            </div>
            <div className="cart-summary">
                <h3>Cart Summary</h3>
                <p>Total Price: ${cart.totalPrice}</p>
                <p>Discounted Price: ${cart.discountedPrice}</p>
            </div>
        </div>
    );
};

export default Cart;
