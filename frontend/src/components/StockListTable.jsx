import { useNavigate } from "react-router-dom";

function marketLabel(code) {
  const map = { 1: "NASDAQ", 2: "NYSE", 3: "AMEX" };
  return map[code] || code || "-";
}

function formatDollar(value) {
  if (value === null || value === undefined) return "-";
  const num = Number(value);
  return `$${num.toLocaleString(undefined, {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  })}`;
}

function formatPercent(value) {
  if (value === null || value === undefined) return "-";
  return `${Number(value).toFixed(2)}%`;
}

function valueClass(value) {
  const num = Number(value);
  if (value === null || value === undefined || Number.isNaN(num)) return "";
  if (num > 0) return "plus-value";
  if (num < 0) return "minus-value";
  return "";
}

function formatMarketCap(value) {
  if (value === null || value === undefined) return "-";
  return `$${Number(value).toLocaleString()}`;
}

function StockListTable({
  title,
  currentCount,
  maxCount,
  items,
  onToggleFavorite,
  fromPath = "/stocks",
}) {
  const navigate = useNavigate();

  return (
    <div className="stock-list-component">
      <div className="stock-list-component-header">
        <div className="stock-list-component-title">{title}</div>
        <div className="stock-list-component-favorite-count">
          お気に入り　{currentCount}/{maxCount}件
        </div>
      </div>

      <table className="stock-list-table">
        <thead>
          <tr>
            <th className="col-code">銘柄コード</th>
            <th className="col-name">銘柄名</th>
            <th className="col-market">市場</th>
            <th className="col-price">現在値</th>
            <th className="col-change">前日比</th>
            <th className="col-rate">騰落率</th>
            <th className="col-cap">時価総額</th>
            <th className="col-favorite">お気に入り</th>
          </tr>
        </thead>
        <tbody>
          {items.length === 0 ? (
            <tr>
              <td colSpan="8" className="empty-cell">
                該当する銘柄はありません。
              </td>
            </tr>
          ) : (
            items.map((item) => (
              <tr key={item.tickerCode}>
                <td>{item.tickerCode}</td>
                <td>
                  <button
                    type="button"
                    className="stock-name-link"
                    onClick={() =>
                      navigate(`/stocks/${item.tickerCode}`, {
                        state: { from: fromPath },
                      })
                    }
                  >
                    {item.stockName}
                  </button>
                </td>
                <td>{marketLabel(item.market)}</td>
                <td>{formatDollar(item.currentPrice)}</td>
                <td className={valueClass(item.priceChange)}>
                  {formatDollar(item.priceChange)}
                </td>
                <td className={valueClass(item.changeRate)}>
                  {formatPercent(item.changeRate)}
                </td>
                <td>{formatMarketCap(item.marketCap)}</td>
                <td className="favorite-cell">
                  <button
                    type="button"
                    className={`favorite-star ${item.favorite ? "active" : ""}`}
                    onClick={() => onToggleFavorite(item.tickerCode)}
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

export default StockListTable;