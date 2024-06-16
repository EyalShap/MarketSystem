import React, { useContext, useEffect, useRef, useState } from 'react';
import '../styles/memberNavbar.css';
import { Link, useNavigate } from 'react-router-dom';
import { IconButton, Badge } from '@mui/material';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import NotificationsIcon from '@mui/icons-material/Notifications';
import { acceptRequest, enterAsGuest, fetchNotifications, logout, okNotification, rejectRequest } from '../API';
import { AppContext } from '../App';
import { NotificationModel, RequestModel } from '../models/NotificationModel';
import { useSubscription } from 'react-stomp-hooks';
import SearchBar from './Search';


const MemberNavbar = () => {
  const {isloggedin , setIsloggedin } = useContext(AppContext);
  const [menuOpen, setMenuOpen] = useState(false);
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  const [notifications, setNotifications] = useState<NotificationModel[]>([]);
  
  const navigate = useNavigate();

  useSubscription(`/topic/notifications/${localStorage.getItem('username')}`, (message) => {
    let notif: NotificationModel = JSON.parse(message.body);
    alert(`New Notification: ${notif.message}`);
    console.log(notif);
    reloadNotifs();
  })

  useEffect(() => {
    reloadNotifs()
  }, [])


  const reloadNotifs = async () => {
    setNotifications(await fetchNotifications())
    {console.log(notifications)}
  }

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

  const toggleNotifications = () => {
    setNotificationsOpen(!notificationsOpen);
  };
  const handleLogout = async () => {
    try{
    const response=await logout(localStorage.getItem('username') as string);
    console.log(response);
    localStorage.clear();
    localStorage.setItem("guestId", `${response}`);
    }catch(e){
      alert("Error occoured");
    }
    setIsloggedin(false);
    navigate('/');
  }
  const getProfileUrl = ():string => {
    return `/profile/${localStorage.getItem('username')}`;
  }
  const getStoresUrl = ():string => {
    return `/memberStores/${localStorage.getItem('username')}`;
  }
  const getOrdersUrl = ():string => {
    return `/orders/${localStorage.getItem('username')}`};
  const handleCartClick = () => {
    if(isloggedin){
      navigate(`/cart/${localStorage.getItem('username')}`);
    }else{
      navigate(`/cart/${localStorage.getItem('guestId')}`);
    }
  }
  
  return (
    <nav className="membernavbar" onClick={()=>{menuOpen&&toggleMenu();notificationsOpen&&toggleNotifications();}}>
      <Link to="/" className="navbar-logo">
        Your Logo
      </Link>
      <SearchBar />
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
                  <p>{notification.message}</p>
                  {!('storeId' in notification) && <button onClick={() => {
                    okNotification(notification.id)
                    setNotifications(notifications.filter(notif => notif.id != notification.id))
                  }} className="">OK </button>}
                  {('storeId' in notification) && <button onClick={() => {
                    acceptRequest(notification.id)
                    setNotifications(notifications.filter(notif => notif.id != notification.id))
                    }} className="">accept </button>}
                  {('storeId' in notification) && <button onClick={() => {
                    rejectRequest(notification.id)
                    setNotifications(notifications.filter(notif => notif.id != notification.id))
                  }} className="">reject </button>}
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
              <li className="menu-item"><Link to={getOrdersUrl()} className="navbar-link">My Orders</Link></li>
              <li className="menu-item"><Link to={getStoresUrl()} className="navbar-link">My Stores</Link></li>
              <li className="menu-item"><Link to={getProfileUrl()} className="navbar-link">Profile</Link></li>
              <li className="menu-item"onClick={handleLogout}>logout</li>
            </ul>
          )}
        </div>
        <IconButton size="small" color="inherit" onClick={handleCartClick}>
          <ShoppingCartIcon />
        </IconButton>
      </div>
    </nav>
  );
};

export default MemberNavbar;
