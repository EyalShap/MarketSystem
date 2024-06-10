import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import SearchedProduct from './SearchedProduct';
import '../styles/searched-product.css';
import ProductModel from '../models/ProductModel';
import { searchProducts } from '../API'; // Adjust the import path based on your file structure

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

function SearchResults() {
  const query = useQuery();
  const searchTerm = query.get('term') || '';
  const searchCategory = query.get('category') || 'all';
  const minPrice = parseFloat(query.get('minPrice') || '0');
  const maxPrice = parseFloat(query.get('maxPrice') || '100');
  const [productsResults, setProductsResults] = useState<ProductModel[]>([]);

  useEffect(() => {
    const results = searchProducts(searchTerm, searchCategory, minPrice, maxPrice);
    setProductsResults(results);
  }, [searchTerm, searchCategory, minPrice, maxPrice]);

  return (
    <div>
      <div className="product-list">
        {productsResults.map((product, index) => (
          <SearchedProduct key={index} product={product} />
        ))}
      </div>
    </div>
  );
}

export default SearchResults;
