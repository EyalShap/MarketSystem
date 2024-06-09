import React, { useEffect, useState } from 'react';
import StoreModel from '../models/StoreModel';
import { getStoreInfo, getStoreManagers, getStoreOwners, getStoreProducts, isManager, isOwner, searchAndFilterStoreProducts } from '../API';
import { useParams } from 'react-router-dom';
import '../styles/Staff.css';
import MemberModel from '../models/MemberModel';
import { IoPerson } from "react-icons/io5";
import { MdOutlineRemoveRedEye } from "react-icons/md";

export const StaffRow = (props: any) => {
    let member: MemberModel = props.member;
    let isManager: boolean = props.isManager;
    return (
        <div className='staffRow'>
            <p className='staffText'><IoPerson />{"\t"}{member.username}</p>
            <button className='viewButton'><MdOutlineRemoveRedEye /></button>
        </div>
    );
};

export default StaffRow;