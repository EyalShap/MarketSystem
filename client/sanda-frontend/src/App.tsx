import './App.css';
import {BrowserRouter as Router,Routes,Route} from "react-router-dom";
import Login from './components/Login';
import Register from './components/Register';
import Home from './components/Home';
import Navbar from './components/Navbar';
import SearchBar from './components/Search';
import Store from './components/Store';
import Staff from './components/Staff';

function App() {
  return (
    <div className="App">
      <Router>   
      <Navbar/>
        <Routes>
          <Route path="/" element={<Home/>}/>
          <Route path="/login" element={<Login/>}/>
          <Route path="/register" element={<Register/>}/>
          <Route path="/store/:storeId" element={<Store/>}/>
          <Route path="/store/:storeId/staff" element={<Staff/>}/>
          <Route path="/*" element={<h1>PAGE NOT FOUND!</h1>}/>
        </Routes>
       
      </Router>
    </div>
  );
}

export default App;
