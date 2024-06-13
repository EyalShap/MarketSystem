import React from 'react';
import { useForm, SubmitHandler } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import '../styles/CreateStore.css';

interface IFormInput {
    storeName: string;
    address: string;
    email: string;
    phoneNumber: string;
}

const schema = yup.object().shape({
    storeName: yup.string().required('Store name is required'),
    address: yup.string().required('Address is required'),
    email: yup.string().email('Invalid email').required('Email is required'),
    phoneNumber: yup.string()
        .matches(/^[0-9]+$/, "Phone number must be only digits")
        .min(10, 'Phone number must be at least 10 digits')
        .max(15, 'Phone number must be at most 15 digits')
        .required('Phone number is required'),
});

const CreateStore: React.FC = () => {
    const { register, handleSubmit, formState: { errors, touchedFields }, trigger } = useForm<IFormInput>({
        resolver: yupResolver(schema),
    });

    const onSubmit: SubmitHandler<IFormInput> = data => {
        console.log(data);
        // You can replace the console.log with your API call to submit the data
    };

    return (
        <div className="create-store-container">
            <h2>Create New Store</h2>
            <form onSubmit={handleSubmit(onSubmit)} className="create-store-form">
                <div className="form-group">
                    <label htmlFor="storeName" className='label'>Store Name</label>
                    <input
                        id="storeName"
                        {...register('storeName', { onChange: () => trigger("storeName") })}
                        className={errors.storeName && touchedFields.storeName ? 'invalid' : ''}
                    />
                    {errors.storeName && <p className="error-message">{errors.storeName.message}</p>}
                </div>
                <div className="form-group">
                    <label htmlFor="address" className='label'>Address</label>
                    <input
                        id="address"
                        {...register('address', { onChange: () => trigger("address") })}
                        className={errors.address && touchedFields.address ? 'invalid' : ''}
                    />
                    {errors.address && <p className="error-message">{errors.address.message}</p>}
                </div>
                <div className="form-group">
                    <label htmlFor="email" className='label'>Email</label>
                    <input
                        id="email"
                        type="email"
                        {...register('email', { onChange: () => trigger("email") })}
                        className={errors.email && touchedFields.email ? 'invalid' : ''}
                    />
                    {errors.email && <p className="error-message">{errors.email.message}</p>}
                </div>
                <div className="form-group">
                    <label htmlFor="phoneNumber" className='label'>Phone Number</label>
                    <input
                        id="phoneNumber"
                        {...register('phoneNumber', { onChange: () => trigger("phoneNumber") })}
                        className={errors.phoneNumber && touchedFields.phoneNumber ? 'invalid' : ''}
                    />
                    {errors.phoneNumber && <p className="error-message">{errors.phoneNumber.message}</p>}
                </div>
                <button type="submit" className='button'>Create Store</button>
            </form>
        </div>
    );
};

export default CreateStore;
