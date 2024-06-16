import React, { useContext, useEffect, useState } from 'react';
import '../styles/cart.css';
// Assuming these interfaces and functions are imported correctly
import { viewMemberCart, viewGuestCart, changeProductAmountInCart, changeProductAmountInCartGuest, removeProductFromCart, removeProductFromCartGuest, checkCart, checkCartGuest, loginUsingJwt } from '../API'; // Add necessary API functions
import cartModel from '../models/CartModel';
import { useNavigate, useParams } from 'react-router-dom';
import { AppContext } from '../App';
import CustomizedDialogs from './CartError';

const Cart = () => {
    const {isloggedin , setIsloggedin } = useContext(AppContext);
    const { username } = useParams<{ username: string }>();
    const [cart, setCart] = useState({ productsData: [], oldPrice: 0, newPrice: 0 } as cartModel);
    const navigate = useNavigate();
    const [dialogOpen, setDialogOpen] = useState(false);
    const [error, setError] = useState("");
    useEffect(() => {
        const checkLogin = async () => {
            if(!isloggedin&&!localStorage.getItem("guestId")){
              const resp=await loginUsingJwt(localStorage.getItem("username") as string, localStorage.getItem("token") as string);
              console.log(resp);
                  if(!resp.error){
                   setIsloggedin(true);
                   await fetchCart(true);
                await checkCart(true);
                  }
                  else{
                    localStorage.clear();
                    alert("Session over please login again");
                    navigate('/');
                  }
            }
            else{
                await fetchCart(isloggedin);
                await checkCart(isloggedin);
            }        
          };   
    const checkCart=async(isloggedin: boolean)=>{
        await validate(isloggedin);
    }
    checkLogin()
    }, []);
    const fetchCart = async (isloggedin: boolean ) =>{ 
        try{
        let response;
        console.log(isloggedin);
        if(isloggedin){
            response = await viewMemberCart(username as string);
        }
        else{
            response = await viewGuestCart(Number.parseInt(username as string)); 
        }
        console.log(response);
        const res=JSON.parse(response.dataJson);
        if(res.error){
            alert(res.errorString);
        }
        setCart(res as cartModel);
    }catch(e: any){
        alert("Error occoured please try again later");
        navigate('/');
    }
    }
    const handleQuantityChange = async(index: number, event: React.ChangeEvent<HTMLSelectElement>) => {
        try{
        const updatedCart = { ...cart };
        updatedCart.productsData[index].amount = parseInt(event.target.value, 10);
        let res;
        if(isloggedin)
            res=await changeProductAmountInCart(updatedCart.productsData[index].id,updatedCart.productsData[index].storeId, updatedCart.productsData[index].amount)
        else{
            res=await changeProductAmountInCartGuest(updatedCart.productsData[index].id,updatedCart.productsData[index].storeId, updatedCart.productsData[index].amount)
        }
        validate(isloggedin)
        if (res.error){
            alert(res.errorString);
        }
        else{
            await fetchCart(isloggedin)
        }
    }catch(e: any){
        console.log(e);
        alert("Error occoured please try again later");
    }
    };

    const handleRemoveProduct = async(index: number) => {
        try{
        const updatedCart = { ...cart };
        let res;
        if(isloggedin)
            res=await removeProductFromCart(updatedCart.productsData[index].id,updatedCart.productsData[index].storeId);
        else
            res=await removeProductFromCartGuest(updatedCart.productsData[index].id,updatedCart.productsData[index].storeId);
            if (res.error){
                alert(res.errorString);
            }
            else{
                await fetchCart(isloggedin)
            }
        }
    catch(e: any){
        console.log(e);
        alert("Error occoured please try again later");
    }
    };
    const validate=async(isloggedin: boolean )=>{
        try{
            let response;
            if(isloggedin){
                response = await checkCart(username as string);
            }
            else{
                response = await checkCartGuest(Number.parseInt(username as string));  
        }
        if(response.error){
            setError(response.errorString);
    }
}
        catch(e: any){
            console.log(e);
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
                {cart.productsData.map((product, index) => (
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
                            <p>Price: ${product.oldPrice}</p>
                            <p>Discounted Price: ${product.newPrice}</p>
                        </div>
                        <button className="button remove" onClick={() => handleRemoveProduct(index)}>X</button>
                    </div>
                ))}
            </div>
            <div className="cart-summary">
                <h3>Cart Summary</h3>
                <p>Total Price: ${cart.oldPrice}</p>
                <p>Discounted Price: ${cart.newPrice}</p>
                <button className="purchase-button" disabled={!error &&cart.productsData.length>0} >Purchase</button>
            </div>
            <CustomizedDialogs open={dialogOpen} onClose={handleDialogClose} text={error} /> 
        </div>
    );
};

export default Cart;
