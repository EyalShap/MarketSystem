import React from 'react';
import '../styles/CowComponent.css';
import cow from '../images/cow.png';

const CowComponent = () => {
  return (
    <div className="cow-container">
      <div className="cow-animation">
        <img src={cow} alt="Cow" width="100" height="100" />
      </div>
      <div className="message">
        We are having connection problems, please come back later...
      </div>
    </div>
  );
};

export default CowComponent;
