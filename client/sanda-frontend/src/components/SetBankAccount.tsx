import React, { useContext, useEffect } from 'react';
import { useForm, SubmitHandler } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import '../styles/CreateStore.css';
import { addProduct, createNewStore, hasPermission, isOwner, loginUsingJwt, setStoreBankAccount } from '../API';
import { useNavigate, useParams } from 'react-router-dom';
import { AppContext } from '../App';
import Permission from '../models/Permission';

// Define the schema for validation
const schema = yup.object().shape({
  bankCode: yup.string().required("Bank Code is required"),
  bankBranchCode: yup.string().required("Bank Branch Code is required"),
  accountCode: yup.string().required("Bank Account code is required"),
  ownerId: yup.string().length(9, "An israeli ID consists of 9 digits").required("Owner ID is required"),
});

const SetBankAccount = () => {
  const { register, handleSubmit, formState: { errors, touchedFields }, trigger } = useForm({
    resolver: yupResolver(schema)
  });

  const {storeId} = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    const checkAllowed = async ()=> {
        let canAccess: boolean = await isOwner(storeId!);
        if(!canAccess){
            navigate('/permission-error', {state: "You do not have permission to set bank account in the given store"})
        }
    }
    checkAllowed();
}, [])

  const onSubmit: SubmitHandler<any> = async (data) => {
    const response = await setStoreBankAccount(parseInt(storeId!), data);
    if (response.error) {
      alert("Error: " + response.errorString);
    } else {
      alert("Bank account changed successfully!");
      navigate(`/store/${storeId}`);
    }
  };

  return (
    <div className="create-store-container">
      <h2>Add New Product</h2>
      <form onSubmit={handleSubmit(onSubmit)} className="create-store-form">
        <div className="form-group">
          <label htmlFor="productName" className='label'>Product Name</label>
          <input
            id="productName"
            placeholder='Enter product name'
            {...register('productName', { onChange: () => trigger("productName") })}
            className={errors.productName && touchedFields.productName ? 'invalid' : ''}
          />
          {errors.productName && <p className="error-message">{errors.productName.message}</p>}
        </div>
        <div className="form-group">
          <label htmlFor="productPrice" className='label'>Product Price</label>
          <input
            id="productPrice"
            placeholder='Enter product price'
            {...register('productPrice', { onChange: () => trigger("productPrice") })}
            className={errors.productPrice && touchedFields.productPrice ? 'invalid' : ''}
          />
          {errors.productPrice && <p className="error-message">{errors.productPrice.message}</p>}
        </div>
        <div className="form-group">
          <label htmlFor="category" className='label'>Product Category</label>
          <input
            id="category"
            placeholder='Enter product category'
            {...register('category', { onChange: () => trigger("category") })}
            className={errors.category && touchedFields.category ? 'invalid' : ''}
          />
          {errors.category && <p className="error-message">{errors.category.message}</p>}
        </div>
        <div className="form-group">
          <label htmlFor="productWeight" className='label'>Product Weight</label>
          <input
            id="productWeight"
            placeholder='Enter product weight'
            {...register('productWeight', { onChange: () => trigger("productWeight") })}
            className={errors.productWeight && touchedFields.productWeight ? 'invalid' : ''}
          />
          {errors.productWeight && <p className="error-message">{errors.productWeight.message}</p>}
        </div>
        <div className="form-group">
          <label htmlFor="productQuantity" className='label'>Initial Amount</label>
          <input
            id="productQuantity"
            placeholder='Enter initial amount'
            {...register('productQuantity', { onChange: () => trigger("productQuantity") })}
            className={errors.productQuantity && touchedFields.productQuantity ? 'invalid' : ''}
          />
          {errors.productQuantity && <p className="error-message">{errors.productQuantity.message}</p>}
        </div>
        <button type="submit" className='button'>Change Bank Account</button>
      </form>
    </div>
  );
};

export default SetBankAccount;
