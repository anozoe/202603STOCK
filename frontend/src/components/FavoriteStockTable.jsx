import { useNavigate } from "react-router-dom";

function formatNumber(value) {
  if (value === null || value === undefined) return "-";
  return Number(value).toLocaleString();
}

function formatDollar(value) {
  if (value === null || value === undefined) return "-";
  return `$${Number(value).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
}

function formatPercent(value) {
  if (value === null || value === undefined) return "-";
  return `${Number(value).toFixed(2)}%`;
}

function marketLabel(code) {
  const map = {
    1: "NASDAQ",
    2: "NYSE",
    3: "AMEX",
  };
  return map[code] || code;
}

function FavoriteStockTable({ currentCount, maxCount, items, onRemoveFavorite }) {
  const navigate = useNavigate();

  return (
    <div className="favorite-section">
      <div className="favorite-title-row">
        <h2>お気に入り銘柄</h2>
        <div className="favorite-count">{currentCount}/{maxCount}</div>
      </div>

      <table className="favorite-table">
        <thead>
          <tr>
            <th>銘柄コード</th>
            <th>銘柄名</th>
            <th>市場</th>
            <th>現在値</th>
            <th>前日比</th>
            <th>騰落率</th>
            <th>時価総額</th>
            <th>お気に入り</th>
          </tr>
        </thead>
        <tbody>
          {items.length === 0 ? (
            <tr>
              <td colSpan="8" className="empty-cell">お気に入り銘柄はありません。</td>
            </tr>
          ) : (
            items.map((item) => (
              <tr key={item.tickerCode}>
                <td>{item.tickerCode}</td>
                <td>
                  <button
                    type="button"
                    className="text-link"
                    onClick={() => navigate(`/stocks/${item.tickerCode}`)}
                  >
                    {item.stockName}
                  </button>
                </td>
                <td>{marketLabel(item.market)}</td>
                <td>{formatDollar(item.currentPrice)}</td>
                <td>{formatDollar(item.priceChange)}</td>
                <td>{formatPercent(item.changeRate)}</td>
                <td>{item.marketCap == null ? "-" : `${formatNumber(item.marketCap)} 千ドル`}</td>
                <td>
                  <button
                    type="button"
                    className="favorite-star active"
                    onClick={() => onRemoveFavorite(item.tickerCode)}
                  >
                    ★
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default FavoriteStockTable;