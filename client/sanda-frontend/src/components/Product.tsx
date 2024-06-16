import React, { useContext, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import RestResponse from '../models/RestResponse';
import { addProductToCartGuest, addProductToCartMember, getProductDetails } from '../API';
import ProductModel from '../models/ProductModel';
import { TextField } from '@mui/material';
import { AppContext } from '../App';

export const Product = () => {
    const {productId} = useParams();
    const [product, setProduct] = useState<ProductModel>({productID: 0, productPrice: 0, productName: "LOADING"});
    const [amount, setAmount] = useState("0");
    useEffect(()=>{
        loadProduct();
      }, [])
      const navigate = useNavigate()
      const { isloggedin, setIsloggedin } = useContext(AppContext);
      const loadProduct = async () => {
        var productResponse: RestResponse = await getProductDetails(parseInt(productId!));
        console.log(productResponse);
        if(!productResponse.error){
            setProduct(JSON.parse(productResponse.dataJson))
        }else{
          navigate('/permission-error', {state: productResponse.errorString})
        }
      }

      const addToCart = async () => {
        let response: RestResponse;
        if(isloggedin){
            response = await addProductToCartMember(parseInt(productId!), product.storeId!, parseInt(amount));
        }else{
            response = await addProductToCartGuest(parseInt(productId!), product.storeId!, parseInt(amount));
        }
        if(response.error){
            alert(`Add to cart failed: ${response.errorString}`)
        }else{
            alert("Product added to cart!")
        }
      }

    return <div>
        <h1>This is a very crude product view page that only shows</h1>
        <h2>basic information like the product {product.productName} name or</h2>
        <h3>the product {product.productPrice} price and a </h3>
        <button onClick={() => navigate(`/store/${product.storeId}`)}>button that sends you to store</button>
        <TextField type='number' size='small' id="outlined-basic" label="And a text field for the amount" variant="outlined" value={amount} onChange={(event: React.ChangeEvent<HTMLInputElement>) => { setAmount(event.target.value); }} />
        <button onClick={addToCart}>And a button that adds said product to cart</button>
    </div>
}