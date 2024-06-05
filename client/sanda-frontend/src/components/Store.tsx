import React, { useEffect, useState } from 'react';
import StoreModel from '../models/StoreModel';
import { getStoreInfo, getStoreProducts, searchAndFilterStoreProducts } from '../API';
import { useParams } from 'react-router-dom';
import ProductModel from '../models/ProductModel';
import { Rating } from 'react-simple-star-rating'
import '../styles/Store.css';
import ProductInStore from './ProductInStore';

export const Store = () => {
    const {storeId} = useParams();
    var store: StoreModel = getStoreInfo(storeId!);
    const [searchTerm, setSearchTerm] = useState('');
    const [searchCategory, setSearchCategory] = useState('all');
    const [minPrice, setMinPrice] = useState(0); // Default price
  const [maxPrice, setMaxPrice] = useState(100); // Default price
  const [products, setProducts] = useState<ProductModel[]>(getStoreProducts(storeId!))

  useEffect(() => {
    setProducts(searchAndFilterStoreProducts(storeId!, searchCategory, searchTerm, minPrice, maxPrice))
  },[searchTerm,searchCategory,minPrice,maxPrice])

  const handleInputChange = (event:any) => {
    setSearchTerm(event.target.value);
  };

  const handleCategoryChange = (event:any) => {
    setSearchCategory(event.target.value);
  };

    const handleMinPriceChange = (event:any) =>{
       setMinPrice(event.target.value);
    };
    const handleMaxPriceChange = (event:any) =>{
        setMaxPrice(event.target.value);
     };

    return (
        <div>
            <div className = "description">
            <h1 className = "description">Welcome to {store.storeName}</h1>
            <Rating initialValue={store.rank} readonly={true} size={20}/>
            <div className="details">
                <p className = "details">Founded by {store.founderUsername}</p>
                <p className='dot'>·</p>
                <p className = "details">{store.address}</p>
                <p className='dot'>·</p>
                <p className = "details">{store.email}</p>
            </div>
            </div>
            <div className="products">
                <h1 className="products">Products:</h1>
                <nav className="search">
        <select className="category"  // Add this line
            value={searchCategory} onChange={handleCategoryChange}>
            <option value="all">All</option>
            <option value="category1">Category 1</option>
            <option value="category2">Category 2</option>
            <option value="category3">Category 3</option>
          </select>
        <input className='search-inputs'
          type="text"
          placeholder="Search..."
          value={searchTerm}
          onChange ={handleInputChange}
        />
        
          <div className="price-range">
            <label>
              Min Price: ${minPrice}
              <input
                type="range"
                min="0"
                max={maxPrice}
                value={minPrice}
                onChange={handleMinPriceChange}
              />
            </label>
            <label>
              Max Price: ${maxPrice}
              <input
                type="range"
                min={minPrice}
                max="100"
                value={maxPrice}
                onChange={handleMaxPriceChange}
              />
            </label>
          </div>        
    </nav>
                <div className='productsgrid'>
                    {products.map(product => 
                        <ProductInStore product = {product}/>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Store;