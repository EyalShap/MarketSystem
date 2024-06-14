import React, { useEffect, useState } from 'react';
import StoreModel from '../models/StoreModel';
import { getMangerPermissions, getStoreInfo, getStoreManagers, getStoreOwners, getStoreProducts, isManager, isOwner, searchAndFilterStoreProducts, updateManagerPermissions } from '../API';
import { useParams } from 'react-router-dom';
import '../styles/Staff.css';
import MemberModel from '../models/MemberModel';
import { IoPerson } from "react-icons/io5";
import { MdOutlineRemoveRedEye } from "react-icons/md";
import { Checkbox, FormControlLabel } from '@mui/material';
import Permission from '../models/Permission';

export const StaffRow = (props: any) => {
    let member: MemberModel = props.member;
    let isManager: boolean = props.isManager;
    const [showDetails,setShowDetails] = useState(false);
    const [changesMade,setChangesMade] = useState(false);
    const [permissions,setPermissions] = useState(getMangerPermissions(props.storeId,member.username));

    interface Dictionary<T> {
        [Key: number]: T;
    }

    let permissionToText: Dictionary<string> = {}
    permissionToText[Permission.ADD_PRODUCTS] = "Add Products"
    permissionToText[Permission.DELETE_PRODUCTS] = "Delete Products"
    permissionToText[Permission.UPDATE_PRODUCTS] = "Edit Products"
    permissionToText[Permission.ADD_BUY_POLICY] = "Edit Buy Policies"
    permissionToText[Permission.ADD_DISCOUNT_POLICY] = "Edit Discount Policies"

    const allPermissions: Permission[] = [Permission.ADD_PRODUCTS,Permission.DELETE_PRODUCTS,Permission.UPDATE_PRODUCTS,Permission.ADD_BUY_POLICY,Permission.ADD_DISCOUNT_POLICY]

    return (
        <div className={isManager ? 'bigStaffRowDivManager' : 'bigStaffRowDivOwner'}>
            <div className={showDetails ? 'staffRowOpen' : 'staffRow'}>
                <p className='staffText'><IoPerson />{"\t"}{member.username}</p>
                <button className='viewButton' onClick={() => setShowDetails(!showDetails)}><MdOutlineRemoveRedEye /></button>
            </div>
            {showDetails && 
            <div className = 'staffDetails'>
                <p>Full Name: {member.firstName} {member.lastName}</p>
                <p>Email: {member.email}</p>
                <p>Phone Number: {member.phoneNumber}</p>
                {allPermissions.map((permission: Permission) => {
                    return <FormControlLabel control={<Checkbox onChange = 
                        {
                            () => {
                                console.log(permissions)
                                if(permissions.includes(permission)){
                                    setPermissions(permissions.filter(perm => perm != permission))
                                }else{
                                    setPermissions(permissions.concat([permission]))
                                }
                                setChangesMade(true)
                            }
                        }
                    checked={permissions.includes(permission)}/> } label={permissionToText[permission]}/>
                })}
                <button className='updatePerms' onClick={() => updateManagerPermissions(props.storeId, member.username, permissions) ? alert("Permissions updated successfully") : alert("Error in changing permissions")} disabled={!changesMade}>Update Permissions</button>
            </div>
            }
        </div>
    );
};

export default StaffRow;