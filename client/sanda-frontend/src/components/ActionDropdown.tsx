import { ReactElement, useState } from "react";
import { IoIosArrowDown  } from "react-icons/io";
import { IoBagAddOutline, IoBagAdd, IoPerson  } from "react-icons/io5";
import { getPermissions, hasPermission, isOwner, storeActive } from "../API";
import { FaSkull } from "react-icons/fa6";
import { FaDoorOpen } from "react-icons/fa";
import Permission from "../models/Permission";
import { FaPlusCircle, FaTrashAlt, FaEdit,FaCreditCard  } from "react-icons/fa";
import '../styles/ActionDropdown.css';
import { IconType } from "react-icons";
import { useNavigate } from "react-router-dom";
 

export const ActionDropdown = (props: any) => {
    const [showMenu, setShowMenu] = useState(false);
    const navigate = useNavigate()

    const toggleShowMenu = () => {
        setShowMenu(!showMenu)
    }

    interface Dictionary<T> {
        [Key: number]: T;
    }

    let permissionToText: Dictionary<string> = {}
    permissionToText[Permission.ADD_PRODUCTS] = "Add Product"
    permissionToText[Permission.CLOSE_STORE] = "Close Store"
    permissionToText[Permission.REOPEN_STORE] = "Reopen Store"
    permissionToText[Permission.ADD_BUY_POLICY] = "Add Buy Policy"
    permissionToText[Permission.ADD_DISCOUNT_POLICY] = "Add Discount Policy"

    let permissionToIcon: Dictionary<ReactElement> = {}
    permissionToIcon[Permission.ADD_PRODUCTS] = <FaPlusCircle/>
    permissionToIcon[Permission.ADD_BUY_POLICY] = <IoBagAddOutline/>
    permissionToIcon[Permission.ADD_DISCOUNT_POLICY] = <IoBagAdd />

    var permissions: Permission[] = getPermissions(props.storeId)

    return (
        <div className = "dropdown">
            <button onClick={toggleShowMenu} className="dropdownButton">Actions <IoIosArrowDown className = {showMenu ? "arrowrotate" : "arrowdefault"}/></button>
            {showMenu && 
            <div className="options">
                {permissions.map(permission => (permission in permissionToIcon) && <button className="optionButton">{permissionToIcon[permission]} {permissionToText[permission]}</button>)}
                {isOwner(props.storeId) &&
                <button className="optionButton" onClick={() => navigate("./staff")}><IoPerson /> View Staff</button>
                }
                {isOwner(props.storeId) &&
                <button className="optionButton" ><FaCreditCard />  Update Bank Account</button>
                }
                {isOwner(props.storeId) && (storeActive(props.storeId) ? <button className="optionButton closeStore"><FaSkull /> Close Store</button> : <button className="optionButton reopenStore"><FaDoorOpen /> Reopen Store</button>)}
            </div>
            }
        </div>
    );
};

export default ActionDropdown;