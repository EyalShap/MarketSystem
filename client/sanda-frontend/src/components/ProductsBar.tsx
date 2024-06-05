import React, { useRef, useState } from 'react';
import '../styles/ProductCarousel.css';

type Product= {
  id: number;
  name: string;
  price: string;
}

type ProductCarouselProps= {
  products: Product[];
}

const ProductsBar = ( products:ProductCarouselProps) => {
    const [carouselRef, setcarouselRef]= useState(0);
    const [currentProduct, setProducts]= useState(products.products.slice(5));

    const scrollLeft = () => {
        if(carouselRef>0){
            setcarouselRef(carouselRef-1);
        setProducts(products.products.slice(carouselRef-1,carouselRef+4));
        }
    };
  
    const scrollRight = () => {
        if(carouselRef<products.products.length-5){
            setcarouselRef(carouselRef+1);
            setProducts(products.products.slice(carouselRef+1,carouselRef+6));
        }
    };
    const handleClick = (product: Product) => {
        console.log(product.id);
    }
    return (
      <div className="product-carousel">
        <button className="arrow left" onClick={scrollLeft}>&lt;</button>
        <button className="arrow right" onClick={scrollRight}>&gt;</button>
        <div className="product-list"  >
          {currentProduct.map((product, index) => (
            <div className="product-item" key={index} onClick={() => handleClick(product)}>
              <h3>{product.name}</h3>
              <p>${product.price}</p>
              
            </div>
          ))}
        </div>
        
      </div>
    );
  };
  
export default ProductsBar;


