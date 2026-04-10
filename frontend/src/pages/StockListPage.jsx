import { useEffect, useState } from "react";
import Header from "../components/Header";
import StockListTable from "../components/StockListTable";
import Pagination from "../components/Pagination";
import { fetchStocks, addFavorite, removeFavorite } from "../api/stockApi";
import "../styles/StockListPage.css";

const PAGE_SIZE = 20;

function StockListPage() {
  const [keyword, setKeyword] = useState("");
  const [searchKeyword, setSearchKeyword] = useState("");
  const [message, setMessage] = useState("");
  const [currentPage, setCurrentPage] = useState(0);

  const [stockData, setStockData] = useState({
    totalCount: 0,
    page: 0,
    size: PAGE_SIZE,
    totalPages: 0,
    currentFavoriteCount: 0,
    maxFavoriteCount: 20,
    items: [],
  });

  useEffect(() => {
    loadStocks(0, "");
  }, []);

  useEffect(() => {
    loadStocks(currentPage, searchKeyword);
  }, [currentPage, searchKeyword]);

  async function loadStocks(page, keywordValue) {
    try {
      const res = await fetchStocks(page, PAGE_SIZE, keywordValue);
      setStockData(res.data);
      setMessage("");
    } catch (error) {
      console.error("fetchStocks error:", error);
      setMessage(error.message || "処理に失敗しました。");
      setStockData({
        totalCount: 0,
        page: 0,
        size: PAGE_SIZE,
        totalPages: 0,
        currentFavoriteCount: 0,
        maxFavoriteCount: 20,
        items: [],
      });
    }
  }

  function handleSearch() {
    setCurrentPage(0);
    setSearchKeyword(keyword.trim());
  }

  async function handleToggleFavorite(tickerCode, isFavorite) {
    try {
      if (isFavorite) {
        await removeFavorite(tickerCode);
      } else {
        await addFavorite(tickerCode);
      }

      await loadStocks(currentPage, searchKeyword);
    } catch (error) {
      console.error("favorite error:", error);
      setMessage(error.message || "処理に失敗しました。");
    }
  }

  return (
    <div className="stock-list-screen">
      <Header />

      <div className="stock-list-page">
        {message && <div className="page-message">{message}</div>}

        <div className="stock-search-area">
          <input
            type="text"
            className="stock-search-input"
            placeholder="銘柄コードまたは銘柄名"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
          <button
            type="button"
            className="stock-search-button"
            onClick={handleSearch}
          >
            検索
          </button>
        </div>

        <StockListTable
          title="銘柄一覧"
          currentCount={stockData.currentFavoriteCount}
          maxCount={stockData.maxFavoriteCount}
          items={stockData.items}
          onToggleFavorite={handleToggleFavorite}
          fromPath="/stocks"
        />

        <Pagination
          currentPage={currentPage}
          totalCount={stockData.totalCount}
          pageSize={PAGE_SIZE}
          onPageChange={setCurrentPage}
        />
      </div>
    </div>
  );
}

export default StockListPage;