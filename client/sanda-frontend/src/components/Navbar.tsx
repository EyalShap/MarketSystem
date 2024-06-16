import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/navbar.css';
import logo from '../images/la_sadna.png';
import SearchBar from './Search';
import '../styles/search.css';
import { IconButton } from '@mui/material';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import { AppContext } from '../App';

export const Navbar = () => {
    const {isloggedin , setIsloggedin } = useContext(AppContext);
    const navigate = useNavigate();
    const handleCartClick = () => {
        if(isloggedin){
          navigate(`/cart/${localStorage.getItem('username')}`);
        }else{
          navigate(`/cart/${localStorage.getItem('guestId')}`);
        }
      }
    return (
        <nav>
            <div className="navbar-container">
                <Link to="/" className="navbar-logo">
                    <img src={logo} width={397} height={250} alt="Logo"></img>
                </Link>
                <SearchBar />
                <ul className="navbar-menu">
                    <li className="navbar-item">
                        <Link to="/login" className="navbar-link">
                            login
                        </Link>
                    </li>
                    <li className="navbar-item">
                        <Link to="/register" className="navbar-link">
                            register
                        </Link>
                    </li>
                    <li className="navbar-item">
                        <IconButton size="small" color="inherit" onClick={handleCartClick}>
                        <ShoppingCartIcon />
                        </IconButton>
                    </li>
                </ul>
            </div>
        </nav>
    );
};

export default Navbar;