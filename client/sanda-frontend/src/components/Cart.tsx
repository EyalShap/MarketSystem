import React, { useContext, useEffect, useState } from 'react';
import '../styles/cart.css';
// Assuming these interfaces and functions are imported correctly
import { viewMemberCart, viewGuestCart, changeProductAmountInCart, changeProductAmountInCartGuest, removeProductFromCart, removeProductFromCartGuest, checkCart, checkCartGuest } from '../API'; // Add necessary API functions
import cartModel from '../models/CartModel';
import { useNavigate, useParams } from 'react-router-dom';
import { number } from 'yup';
import { AppContext } from '../App';
import CustomizedDialogs from './CartError';

const Cart = () => {
    const {isloggedin , setIsloggedin } = useContext(AppContext);
    const { user } = useParams();
    const [cart, setCart] = useState({ products: [], totalPrice: 0, discountedPrice: 0 } as cartModel);
    const navigate = useNavigate();
    const [dialogOpen, setDialogOpen] = useState(false);
    const [error, setError] = useState("");
    useEffect(() => {
        const fetchCart = async () =>{ 
        try{
        let response;
        if(isloggedin){
            response = await viewMemberCart(user as string);
        }
        else{
            response = await viewGuestCart(Number.parseInt(user as string)); 
        }
        const res=JSON.parse(response);
        if(res.isError){
            alert(res.message);
        }
        setCart(res.dataJson as cartModel);
    }catch{
        alert("Error occoured please try again later");
        navigate('/');
    }
    }
    const checkCart=async()=>{
        await validate();
    }
    fetchCart();
    checkCart();
    }, [user]);

    const handleQuantityChange = async(index: number, event: React.ChangeEvent<HTMLSelectElement>) => {
        try{
        const updatedCart = { ...cart };
        updatedCart.products[index].amount = parseInt(event.target.value, 10);
        let res;
        if(isloggedin)
            res=await changeProductAmountInCart(updatedCart.products[index].id,updatedCart.products[index].storeId, updatedCart.products[index].amount)
        else{
            res=await changeProductAmountInCartGuest(updatedCart.products[index].id,updatedCart.products[index].storeId, updatedCart.products[index].amount)
        }
        if (res.isError){
            alert(res.message);
        }
        else{
        setCart(updatedCart);
        }
    }catch{
        alert("Error occoured please try again later");
    }
    };

    const handleRemoveProduct = async(index: number) => {
        try{
        const updatedCart = { ...cart };
        let res;
        if(isloggedin)
            res=await removeProductFromCart(updatedCart.products[index].id,updatedCart.products[index].storeId);
        else
            res=await removeProductFromCartGuest(updatedCart.products[index].id,updatedCart.products[index].storeId);
            if (res.isError){
                alert(res.message);
            }
            else{
            setCart(updatedCart);
            }
        }
    catch{
        alert("Error occoured please try again later");
    }
    };
    const validate=async()=>{
        try{
            let response;
            if(isloggedin){
                response = await checkCart(user as string);
            }
            else{
                response = await checkCartGuest(Number.parseInt(user as string));  
        }
        if(response.isError){
            setError(response.message);
    }
}
        catch{
            alert("Error occoured please try again later");
        
        }
    }
    const handleDialogClose = () => {
        setDialogOpen(false);
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
            <CustomizedDialogs open={dialogOpen} onClose={handleDialogClose} text={error} /> 
        </div>
    );
};

export default Cart;
