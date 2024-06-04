import './App.css';
import {BrowserRouter as Router,Routes,Route} from "react-router-dom";
import Login from './components/Login';
import Register from './components/Register';
import Home from './components/Home';
import Navbar from './components/Navbar';
import SearchBar from './components/Search';

function App() {
  return (
    <div className="App">
      <Router>   
      <Navbar/>
        <Routes>
          <Route path="/" element={<Home/>}/>
          <Route path="/login" element={<Login/>}/>
          <Route path="/register" element={<Register/>}/>
          <Route path="/*" element={<h1>PAGE NOT FOUND!</h1>}/>
        </Routes>
       
      </Router>
    </div>
  );
}

export default App;
