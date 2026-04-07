import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import Header from "../components/Header";
import { fetchStockDetail } from "../api/stockApi";
import "../styles/StockDetailPage.css";

function marketLabel(code) {
  const map = { 1: "NASDAQ", 2: "NYSE", 3: "AMEX" };
  return map[code] || code || "-";
}

function formatDollar(value) {
  if (value === null || value === undefined) return "-";
  return `$${Number(value).toLocaleString(undefined, {
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

function StockDetailPage() {
  const { tickerCode } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  const [data, setData] = useState(null);
  const [message, setMessage] = useState("");
  const [activeTab, setActiveTab] = useState("overview");
  const [chartType, setChartType] = useState("candle");
  const [period, setPeriod] = useState("week");
  const [showMa, setShowMa] = useState(true);

  useEffect(() => {
    loadDetail();
  }, [tickerCode]);

  async function loadDetail() {
    try {
      const res = await fetchStockDetail(tickerCode);
      setData(res.data);
    } catch (error) {
      setMessage(error.message);
    }
  }

  function handleBack() {
    if (location.state?.from) {
      navigate(location.state.from);
      return;
    }
    navigate(-1);
  }

  if (!data) return <div className="loading-screen">読み込み中...</div>;

  const chartPoints = period === "week" ? data.weekChart : data.monthChart;

  return (
    <div className="stock-detail-screen">
      <Header title="銘柄詳細画面" userName="User Name" />

      <div className="stock-detail-page-body">
        {message && <div className="page-message">{message}</div>}

        <button type="button" className="back-button" onClick={handleBack}>
          ↵戻る
        </button>

        <div className="stock-detail-summary">
          <div>銘柄コード：{data.tickerCode}</div>
          <div>銘柄名：{data.stockName}</div>
          <div>市場：{marketLabel(data.market)}</div>
          <div>現在値：{formatDollar(data.currentPrice)}</div>
          <div className={valueClass(data.priceChange)}>
            前日比：{formatDollar(data.priceChange)}
          </div>
          <div>データ取得日：{data.fetchedAt}</div>
        </div>

        <div className="detail-tab-row">
          <button
            type="button"
            className={activeTab === "overview" ? "active" : ""}
            onClick={() => setActiveTab("overview")}
          >
            概要
          </button>
          <button
            type="button"
            className={activeTab === "chart" ? "active" : ""}
            onClick={() => setActiveTab("chart")}
          >
            チャート
          </button>
        </div>

        {activeTab === "overview" ? (
          <div className="overview-tab">
            <div className="overview-card">
              <div className="overview-card-title">四本値</div>
              <div>始値：{formatDollar(data.overview.openPrice)}</div>
              <div>高値：{formatDollar(data.overview.highPrice)}</div>
              <div>安値：{formatDollar(data.overview.lowPrice)}</div>
              <div>終値：{formatDollar(data.overview.closePrice)}</div>
            </div>

            <div className="overview-card">
              <div className="overview-card-title">指標</div>
              <div>PER：{data.overview.per}</div>
              <div>PBR：{data.overview.pbr}</div>
              <div>ROE：{formatPercent(data.overview.roe)}</div>
              <div>配当利回り：{formatPercent(data.overview.dividendYield)}</div>
            </div>
          </div>
        ) : (
          <div className="chart-tab">
            <div className="chart-toolbar">
              <button
                type="button"
                className={chartType === "candle" ? "active" : ""}
                onClick={() => setChartType("candle")}
              >
                ローソク
              </button>
              <button
                type="button"
                className={chartType === "line" ? "active" : ""}
                onClick={() => setChartType("line")}
              >
                線
              </button>
              <button
                type="button"
                className={period === "week" ? "active" : ""}
                onClick={() => setPeriod("week")}
              >
                1週間
              </button>
              <button
                type="button"
                className={period === "month" ? "active" : ""}
                onClick={() => setPeriod("month")}
              >
                1か月
              </button>
              <label className="chart-ma-check">
                <input
                  type="checkbox"
                  checked={showMa}
                  onChange={(e) => setShowMa(e.target.checked)}
                />
                移動平均（5日）
              </label>
            </div>

            <div className="chart-placeholder">
              <div className="chart-placeholder-title">
                {chartType === "candle" ? "ローソクチャート" : "線チャート"}
              </div>
              <div className="chart-placeholder-list">
                {chartPoints.map((point) => (
                  <div key={point.date}>
                    {point.date} / O:{formatDollar(point.openPrice)} H:{formatDollar(point.highPrice)} L:{formatDollar(point.lowPrice)} C:{formatDollar(point.closePrice)}
                    {showMa ? ` / MA5:${formatDollar(point.movingAverage5)}` : ""}
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default StockDetailPage;