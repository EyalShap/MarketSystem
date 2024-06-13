import React, { useContext, useState } from 'react';
import '../styles/memberNavbar.css';
import { Link } from 'react-router-dom';
import { IconButton, Badge } from '@mui/material';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import NotificationsIcon from '@mui/icons-material/Notifications';


const MemberNavbar = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  const [notifications, setNotifications] = useState([
    "got one message",
    "ask to ffffffffff",
    "ffff",
  ]);

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

  const toggleNotifications = () => {
    setNotificationsOpen(!notificationsOpen);
  };
  const handleLogout = () => {

  }
  const getProfileUrl = ():string => {
    return `/profile/${localStorage.getItem('username')}`;
  }
  const getStoresUrl = ():string => {
    return `/memberStores/${localStorage.getItem('username')}`;
  }
  return (
    <nav className="membernavbar" onClick={()=>{menuOpen&&toggleMenu();notificationsOpen&&toggleNotifications();}}>
      <Link to="/" className="navbar-logo">
        Your Logo
      </Link>
      <div className="navbar-right">
        <div className="notifications-navbar-items">
          <IconButton size="small" color="inherit" onClick={toggleNotifications}>
            <Badge badgeContent={notifications.length} color="error">
              <NotificationsIcon />
            </Badge>
          </IconButton>
          {notificationsOpen && (
            <ul className="menu">
              {notifications.map((notification, index) => (
                <li key={index} className="notification-item">
                  {notification}
                  <button className="">accept </button>
                  <button className="">reject </button>
                </li>
              ))}
            </ul>
          )}
        </div>
        <div className="member-navbar-items">
          <button className="menu-toggle" onClick={toggleMenu}>
            ☰
          </button>
          {menuOpen && (
            <ul className="options-menu">
              <li className="menu-item">My orders</li>
              <li className="menu-item"><Link to={getStoresUrl()} className="navbar-link">My stores</Link></li>
              <li className="menu-item"><Link to={getProfileUrl()} className="navbar-link">Profile</Link></li>
              <li className="menu-item"onClick={handleLogout}>logout</li>
            </ul>
          )}
        </div>
        <IconButton size="small" color="inherit">
          <ShoppingCartIcon />
        </IconButton>
      </div>
    </nav>
  );
};

export default MemberNavbar;
