import React, { useEffect, useState } from 'react';
import StoreModel from '../models/StoreModel';
import { getStoreInfo, getStoreProducts, isManager, isOwner, searchAndFilterStoreProducts } from '../API';
import { useNavigate, useParams } from 'react-router-dom';
import ProductModel from '../models/ProductModel';
import { Rating } from 'react-simple-star-rating'
import '../styles/Store.css';
import ProductInStore from './ProductInStore';
import ActionDropdown from './ActionDropdown';
import RestResponse from '../models/RestResponse';

export const Store = () => {
    const {storeId} = useParams();
    const [store, setStore] = useState<StoreModel>({storeName: "LOADING", storeId: -1, rank: -1, address: "LOADING", email: "LOADING", phoneNumber: "LOADING", founderUsername: "LOADING"});
    const [searchTerm, setSearchTerm] = useState('');
    const navigate = useNavigate();
    const [searchCategory, setSearchCategory] = useState('all');
    const [minPrice, setMinPrice] = useState(0); // Default price
  const [maxPrice, setMaxPrice] = useState(100); // Default price
  const [products, setProducts] = useState<ProductModel[]>(getStoreProducts(storeId!))

  useEffect(()=>{
    loadStore();
  }, [])
  useEffect(() => {
    setProducts(searchAndFilterStoreProducts(storeId!, searchCategory, searchTerm, minPrice, maxPrice))
  },[searchTerm,searchCategory,minPrice,maxPrice])

  const loadStore = async () => {
    var storeResponse: RestResponse = await getStoreInfo(storeId!);
    console.log(storeResponse);
    if(!storeResponse.error){
      setStore(JSON.parse(storeResponse.dataJson))
    }else{
      navigate('/permission-error', {state: storeResponse.errorString})
    }
  }
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
            <h1 className = "description">Welcome to {store!.storeName}</h1>
            <Rating initialValue={store!.rank} readonly={true} size={20}/>
            <div className="details">
                <p className = "details">Founded by {store!.founderUsername}</p>
                <p className='dot'>·</p>
                <p className = "details">{store!.address}</p>
                <p className='dot'>·</p>
                <p className = "details">{store!.email}</p>
            </div>
            <div className='dropdownDiv'>
            {(isOwner(storeId!) || isManager(storeId!)) &&
                <ActionDropdown storeId = {storeId}/>
            }
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