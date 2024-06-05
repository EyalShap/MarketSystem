import React, { useEffect, useState } from 'react';
import StoreModel from '../models/StoreModel';
import { getStoreInfo, getStoreManagers, getStoreOwners, getStoreProducts, isManager, isOwner, searchAndFilterStoreProducts } from '../API';
import { useParams } from 'react-router-dom';
import ProductModel from '../models/ProductModel';
import { Rating } from 'react-simple-star-rating'
import '../styles/Staff.css';
import ProductInStore from './ProductInStore';
import ActionDropdown from './ActionDropdown';
import MemberModel from '../models/MemberModel';

export const Staff = () => {
    const {storeId} = useParams();
    const managers: MemberModel[] = getStoreManagers(storeId!);
    const owners: MemberModel[] = getStoreOwners(storeId!);
    return (
        <div className='walled'>
            <div className='managers'>
                <h1>Managers:</h1>
                {managers.map(manager => <p>Username: {manager.username}</p>)}
            </div>
            <div className='divider'/>
            <div className='owners'>
                <h1>Owners:</h1>
                {owners.map(owner => <p>Username: {owner.username}</p>)}
            </div>
        </div>
    );
};

export default Staff;