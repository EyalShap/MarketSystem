import React, { useState } from 'react';
import '../styles/memberNavbar.css'
import { Link } from 'react-router-dom';

import { Button, IconButton } from '@mui/material'
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import NotificationsIcon from '@mui/icons-material/Notifications';
import Badge from '@mui/material/Badge';
const MemberNavbar = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [notificationsOpen, setNotificationspen] = useState(false);
  const [notifications, setNotifications] = useState(["got one message","ask to"]);

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };
 

  const toggleNotifications = () => {
    setNotificationspen(!notificationsOpen);
  };

  return (
    <nav className="membernavbar">
    <Link to="/" className="navbar-logo" >
    Your Logo
    </Link>
    
      <div className="member-navbar-items">
        <button className="menu-toggle" onClick={toggleMenu}>
          ☰
        </button>
        {menuOpen && (
          <ul className="menu">
            <li className="menu-item">My orders</li>
            <li className="menu-item">My stores</li>
            <li className="menu-item">Profile</li>
          </ul>
        )}      
      </div>
      <IconButton size="small" color='inherit'>
            <ShoppingCartIcon />
        </IconButton>
        <div className="notifications-navbar-items">
        <IconButton size="small" color='inherit' onClick={toggleNotifications}>
        <Badge badgeContent={17} color="error">
                <NotificationsIcon />
              </Badge>
        </IconButton>
        {notificationsOpen && (
        <ul className="menu">
          {notifications.map((notification, index) => (<li className="notification-item">{notification}</li>))}
        </ul>
        )}
    </div>
    </nav>
  );
};

export default MemberNavbar;
