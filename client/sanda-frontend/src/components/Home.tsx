import React, { useContext, useEffect, useRef, useState } from 'react';
import ProductsBar from './ProductsBar';
import CategoriesBar from './CategoriesBar';
import { enterAsGuest, loginUsingJwt } from '../API';
import { AppContext } from '../App';
import { set } from 'date-fns';

const products = [
    { id: 1, name: 'Product 1', price: '10.00' },
    { id: 2, name: 'Product 2', price: '20.00' },
    { id: 3, name: 'Product 3', price: '30.00' },
    { id: 4, name: 'Product 4', price: '40.00' },
    { id: 5, name: 'Product 5', price: '50.00' },
    { id: 6, name: 'Product 1', price: '10.00' },
    { id: 7, name: 'Product 2', price: '20.00' },
    { id: 8, name: 'Product 3', price: '30.00' },
    { id: 9, name: 'Product 4', price: '40.00' },
    { id: 10, name: 'Product 5', price: '50.00' },
  ];

const Home = () => {
  const {isloggedin , setIsloggedin } = useContext(AppContext);
  const effectRan = useRef(false);;
    useEffect(() => {
        const fetchData = async () => {
          if (effectRan.current) return;          
          if(localStorage.getItem("guestId")||isloggedin) return;
          if(localStorage.getItem("token") !== null&& localStorage.getItem("username") !== "null"){
            const resp=await loginUsingJwt(localStorage.getItem("username") as string, localStorage.getItem("token") as string);
            if(!resp.error){
             setIsloggedin(true);
             return;
            }
            else{
              localStorage.clear();
              alert("Session over please login again");
            }
          }
          try{
          const guestId = await enterAsGuest();
          localStorage.setItem("guestId", `${guestId}`);
          }catch(e){
            alert("Error occoured please try again later");
          }
        };
        fetchData();
        effectRan.current = true;
      }, [isloggedin, setIsloggedin]);
    return (
        <div>
            <h1>Our top products</h1>
            <ProductsBar products={products} />
            <h3>Our Top Categories</h3>
            <CategoriesBar />     
        </div>
    );
};

export default Home;