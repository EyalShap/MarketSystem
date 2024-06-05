import StoreModel from '../models/StoreModel';
import { getStoreInfo, getStoreProducts } from '../API';
import ProductModel from '../models/ProductModel';
import '../styles/Store.css';
import { useEffect } from 'react';

export const ProductInStore = (props: any) => {

    return (
        <div className = "productbox">
            <h3 className = "product">{props.product.name}</h3>
            <h4 className = "product">${props.product.price}</h4>
        </div>
    );
};

export default ProductInStore;