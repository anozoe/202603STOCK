import logo from './logo.svg';
import './App.css';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage'
import { Routes, Route } from 'react-router-dom';
import Header from './components/Header';

function App() {
  return (
    <Routes>
      <Route path="/" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path='/header' element={<Header />} />
    </Routes>
  );
}

export default App;
