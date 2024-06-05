import React, { useState } from 'react';
import '../styles/searched-product.css';
import '../styles/general.css';

interface Product {
  id: string;
  name: string;
  description: string;
  rating: {
    stars: number;
    count: number;
  };
  priceCents: number;
}

interface SearchedProductProps {
  product: Product;
}

const SearchedProduct: React.FC<SearchedProductProps> = ({ product }) => {
  const [addedToCart, setAddedToCart] = useState(false);

  function handleAddToCart(event: React.MouseEvent<HTMLButtonElement, MouseEvent>): void {
    // Implement the logic to add the product to the cart here
    console.log('Product added to cart:', product);
    // Set addedToCart to true when the button is clicked
    setAddedToCart(true);
  }

  return (
    <div className="product-container">
      <div className="product-name limit-text-to-2-lines">
        {product.name}
      </div>

      <div className="product-description">
        {product.description}
      </div>

      <div className="product-rating-container">
        <img className="product-rating-stars" src={require(`../images/ratings/rating-${Math.round(product.rating.stars * 2) * 5}.png`)} alt={"../images/ratings/rating-0.png"} />
        <div className="product-rating-count link-primary">
          {product.rating.count}
        </div>
      </div>

      <div className="product-price">
        ${(product.priceCents / 100).toFixed(2)}
      </div>

      <div className="product-quantity-container">
        <select>
          {[...Array(10)].map((_, index) => (
            <option key={index} value={index + 1}>{index + 1}</option>
          ))}
        </select>
      </div>

      <div className={addedToCart ? "added-to-cart-visible" : "added-to-cart"}>
        <img src={require("../images/ratings/checkmark.png")} alt={""} />
        Added
      </div>

      <button className="add-to-cart-button button-primary" data-product-id={product.id} onClick={handleAddToCart}>
        Add to Cart
      </button>
    </div>
  );
};

export default SearchedProduct;
