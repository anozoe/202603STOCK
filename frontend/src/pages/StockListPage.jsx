import { useEffect, useState } from "react";
import Header from "../components/Header";
import StockListTable from "../components/StockListTable";
import Pagination from "../components/Pagination";
import { addFavorite, fetchStocks, removeFavorite } from "../api/stockApi";
import "../styles/StockListPage.css";

const PAGE_SIZE = 20;

function StockListPage() {
  const [keyword, setKeyword] = useState("");
  const [searchKeyword, setSearchKeyword] = useState("");
  const [page, setPage] = useState(0);
  const [message, setMessage] = useState("");
  const [data, setData] = useState({
    totalCount: 0,
    currentFavoriteCount: 0,
    maxFavoriteCount: 20,
    items: [],
  });

  useEffect(() => {
    loadStocks(searchKeyword, page);
  }, [searchKeyword, page]);

  async function loadStocks(nextKeyword, nextPage) {
    try {
      const res = await fetchStocks(nextKeyword, nextPage, PAGE_SIZE);
      setData(res.data);
    } catch (error) {
      setMessage(error.message);
    }
  }

  async function handleToggleFavorite(tickerCode) {
    try {
      const item = data.items.find((s) => s.tickerCode === tickerCode);
      if (item?.favorite) {
        await removeFavorite(tickerCode);
      } else {
        await addFavorite(tickerCode);
      }
      await loadStocks(searchKeyword, page);
    } catch (error) {
      setMessage(error.message);
    }
  }

  return (
    <div className="stock-list-screen">
      <Header title="銘柄一覧画面" userName="User Name" />

      <div className="stock-list-page-body">
        {message && <div className="page-message">{message}</div>}

        <div className="stock-search-box">
          <input
            type="text"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            placeholder="銘柄コードまたは銘柄名"
            className="stock-search-input"
          />
          <button
            type="button"
            className="stock-search-button"
            onClick={() => {
              setPage(0);
              setSearchKeyword(keyword);
            }}
          >
            検索
          </button>
        </div>

        <StockListTable
          title="銘柄一覧"
          currentCount={data.currentFavoriteCount}
          maxCount={data.maxFavoriteCount}
          items={data.items}
          onToggleFavorite={handleToggleFavorite}
          fromPath="/stocks"
        />

        <Pagination
          currentPage={page}
          totalCount={data.totalCount}
          pageSize={PAGE_SIZE}
          onPageChange={setPage}
        />
      </div>
    </div>
  );
}

export default StockListPage;