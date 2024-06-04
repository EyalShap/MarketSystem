import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/navbar.css';
import logo from '../images/la_sadna.png';
import SearchBar from './Search';
import '../styles/search.css';

export const Navbar = () => {
    return (
        <nav>
            <div className="navbar-container">
                <Link to="/" className="navbar-logo">
                    <img src={logo} alt="Logo"></img>
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
                        <Link to="/account" className="navbar-link">
                            Account
                        </Link>
                    </li>
                </ul>
            </div>
        </nav>
    );
};

export default Navbar;