import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import SearchedProduct from './SearchedProduct';
import '../styles/searched-product.css';

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

function SearchResults() {
  const query = useQuery();
  const searchTerm = query.get('term');
  const searchCategory = query.get('category');
  const minPrice = query.get('minPrice');
  const maxPrice = query.get('maxPrice');
  //const [searchResults, setSearchResults] = useState([]);

//   useEffect(() => {
//     const fetchSearchResults = async () => {
//       try {
//         const response = await axios.get('YOUR_API_URL_HERE', {
//           params: {
//             term: searchTerm,
//             category: searchCategory,
//             minPrice: minPrice,
//             maxPrice: maxPrice
//           }
//         });
//         setSearchResults(response.data); // Assuming the API response contains an array of products
//       } catch (error) {
//         console.error('Error fetching search results:', error);
//       }
//     };

//     if (searchTerm && searchCategory && minPrice && maxPrice) {
//       fetchSearchResults();
//     }
//   }, [searchTerm, searchCategory, minPrice, maxPrice]);




const searchResults = [
  {
    id: '1',
    name: 'Product 1',
    description: 'Description of Product 1',
    rating: {
      stars: 4.5,
      count: 10
    },
    priceCents: 1500 // $15.00
  },
  {
    id: '2',
    name: 'Product 2',
    description: 'Description of Product 2',
    rating: {
      stars: 3.8,
      count: 8
    },
    priceCents: 2000 // $20.00
  },
  {
    id: '3',
    name: 'Product 3',
    description: 'Description of Product 3',
    rating: {
      stars: 2.2,
      count: 5
    },
    priceCents: 1000 // $10.00
  },
  {
    id: '4',
    name: 'Product 4',
    description: 'Description of Product 4',
    rating: {
      stars: 4.0,
      count: 15
    },
    priceCents: 2500 // $25.00
  },
  {
    id: '5',
    name: 'Product 5',
    description: 'Description of Product 5',
    rating: {
      stars: 3.5,
      count: 12
    },
    priceCents: 1800 // $18.00
  },
  // Add more sample products as needed
];



  return (
    <div>
      
      <div className="product-list">
        {searchResults.map(product => (
          <SearchedProduct key={product.id} product={product} />
        ))}
      </div>
    </div>
  );
}

export default SearchResults;
