import React, { useEffect } from 'react';
import '../styles/CowComponent.css';
import cow from '../images/cow.png';
import { checkAlive, setFalse } from '../API';
import { useDispatch, useSelector } from 'react-redux';

const CowComponent = () => {
  const conerror = useSelector((state:any) => state.conerror.value)
  const dispatch = useDispatch()
  const checkIfServerBack = async () => {
    let alive = await checkAlive();
    if(alive){
      dispatch(setFalse())
      window.location.href = window.location.origin+"/";
    }else{
      console.log("server still dead")
    }
  }
  useEffect(() => {
    const interval = setInterval(() => checkIfServerBack(), 30000);
    return () => {
      clearInterval(interval);
    };
  }, []);

  return (
    <div className="cow-container">
      <div className="cow-animation">
        <img src={"https://media1.tenor.com/m/5jpxCVc4hHgAAAAC/cow-dancing.gif"} alt="Cow" width="100" />
      </div>
      <div className="message">
        We are having connection problems, please come back later...
      </div>
    </div>
  );
};

export default CowComponent;
