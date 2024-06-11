import './App.css';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from './components/Login';
import Register from './components/Register';
import Home from './components/Home';
import Navbar from './components/Navbar';
import MemberNavbar from './components/MemberNavbar';
import { useState, createContext } from 'react';
import Profile from './components/Profile';
import SearchResults from './components/SearchResults';
import Store from './components/Store';
import Staff from './components/Staff';
import Cart from './components/Cart';
import Orders from './components/Orders';

interface AppContextProps {
  isloggedin: boolean;
  setIsloggedin: React.Dispatch<React.SetStateAction<boolean>>;
}

export const AppContext = createContext<AppContextProps>({
  isloggedin: false,
  setIsloggedin: () => {},
});


function App() {
  const [isloggedin, setIsloggedin] = useState(false);
  
  return (
    <div className="App">
      <AppContext.Provider value={{ isloggedin, setIsloggedin }}>
        <Router>
          {isloggedin ? <MemberNavbar /> : <Navbar />}
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/profile/:username" element={<Profile />} />
            <Route path="/orders/:username" element={<Orders />} />
            <Route path="/cart/:username" element={<Cart />} />
            <Route path="/search-results" element={<SearchResults />} />
            <Route path="/store/:storeId" element={<Store />} />
            <Route path="/store/:storeId/staff" element={<Staff />} />
            <Route path="/*" element={<h1>PAGE NOT FOUND!</h1>} />
          </Routes>
        </Router>
      </AppContext.Provider>
    </div>
  );
}

export default App;
