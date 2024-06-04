import './App.css';
import {BrowserRouter as Router,Routes,Route} from "react-router-dom";
import Login from './components/Login';
import Register from './components/Register';
import Home from './components/Home';
import Navbar from './components/Navbar';
import MemberNavbar from './components/MemberNavbar';
import { useState } from 'react';
import Profile from './components/Profile';

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
          <Route path="/profile" element={<Profile/>}/>
          <Route path="/*" element={<h1>PAGE NOT FOUND!</h1>}/>
        </Routes> 
      </Router>
    </div>
  );
}

export default App;
