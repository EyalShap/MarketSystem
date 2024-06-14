import './App.css';
import {BrowserRouter as Router,Routes,Route} from "react-router-dom";
import Login from './components/Login';
import Register from './components/Register';
import Home from './components/Home';
import Navbar from './components/Navbar';
import MemberNavbar from './components/MemberNavbar';
import { useState } from 'react';
import Profile from './components/Profile';
import SearchResults from './components/SearchResults';
import SearchBar from './components/Search';
import Store from './components/Store';
import Staff from './components/Staff';
import DiscountWizard from './components/DiscountWizard';

function App() {
  const [isloggedin,setIsloggedin] = useState(true);
  return (
    <div className="App">
      <Router>
      {isloggedin?<MemberNavbar/>: <Navbar/>}
        <Routes>
          <Route path="/" element={<Home/>}/>
          <Route path="/login" element={<Login/>}/>
          <Route path="/register" element={<Register/>}/>
          <Route path="/profile/:username" element={<Profile/>}/>
          <Route path="/search-results" element={<SearchResults />} />
          <Route path="/store/:storeId" element={<Store/>}/>
          <Route path="/store/:storeId/staff" element={<Staff/>}/>
          <Route path="/store/:storeId/discount" element={<DiscountWizard/>}/>
          <Route path="/*" element={<h1>PAGE NOT FOUND!</h1>}/>
        </Routes>
      
      
      
      
      </Router>
    



    </div>
  );
}

export default App;
