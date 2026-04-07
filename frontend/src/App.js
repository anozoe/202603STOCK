import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import MyPage from "./pages/MyPage";
import StockListPage from "./pages/StockListPage";
import StockDetailPage from "./pages/StockDetailPage";
import "./styles/common.css";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/stocks" replace />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/stocks" element={<StockListPage />} />
        <Route path="/stocks/:tickerCode" element={<StockDetailPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;