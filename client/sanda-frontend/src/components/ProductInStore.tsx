import StoreModel from '../models/StoreModel';
import { getStoreInfo } from '../API';
import ProductModel from '../models/ProductModel';
import '../styles/Store.css';
import { useEffect } from 'react';

export const ProductInStore = (props: any) => {

    return (
        <div className = "productbox">
            <h3 className = "product">{props.product.productName}</h3>
            <h4 className = "product">${props.product.productPrice}</h4>
        </div>
    );
};

export default ProductInStore;