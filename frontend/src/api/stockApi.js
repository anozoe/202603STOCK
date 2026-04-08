import { parseResponse } from "./apiClient";

const BASE_URL = "http://localhost:8080/api/stocks";

export async function fetchStocks(keyword = "", page = 0, size = 20) {
  return parseResponse(
    await fetch(
      `${BASE_URL}?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${size}`
    )
  );
}

export async function fetchFavoriteStocks(page = 0, size = 20) {
  return parseResponse(
    await fetch(`${BASE_URL}/favorites?page=${page}&size=${size}`)
  );
}

export async function fetchStockDetail(tickerCode) {
  return parseResponse(await fetch(`${BASE_URL}/${tickerCode}`));
}

export async function addFavorite(tickerCode) {
  return parseResponse(
    await fetch(`${BASE_URL}/${tickerCode}/favorite`, {
      method: "POST",
    })
  );
}

export async function removeFavorite(tickerCode) {
  return parseResponse(
    await fetch(`${BASE_URL}/${tickerCode}/favorite`, {
      method: "DELETE",
    })
  );
}