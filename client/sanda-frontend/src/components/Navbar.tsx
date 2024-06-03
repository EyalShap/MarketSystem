import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/navbar.css';

export const Navbar = () => {
    return (
        <nav>
            <div className="navbar-container">
                <Link to="/" className="navbar-logo">
                    Your Logo
                </Link>
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