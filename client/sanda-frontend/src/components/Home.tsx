import React from 'react';
import SearchBar from './Search';
import ProductsBar from './ProductsBar';
import { useState, createContext, useContext } from "react";
import { Category } from '@mui/icons-material';
import CategoriesBar from './CategoriesBar';

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