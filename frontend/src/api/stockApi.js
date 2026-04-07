const BASE_URL = "http://localhost:8080/api/stocks";

async function parseResponse(response) {
  const json = await response.json();
  if (!response.ok) {
    throw new Error(json.message || "エラーが発生しました。");
  }
  return json;
}

export async function fetchStocks(keyword = "", page = 0, size = 20) {
  return parseResponse(
    await fetch(
      `${BASE_URL}?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${size}`
    )
  );
}

export async function fetchStockDetail(tickerCode) {
  return parseResponse(await fetch(`${BASE_URL}/${tickerCode}`));
}

export async function toggleFavorite(tickerCode) {
  return parseResponse(
    await fetch(`${BASE_URL}/${tickerCode}/favorite`, {
      method: "POST",
    })
  );
}