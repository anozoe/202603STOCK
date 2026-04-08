import { parseResponse } from "./apiClient";

const USER_BASE_URL = "http://localhost:8080/api/users";
const STOCK_BASE_URL = "http://localhost:8080/api/stocks";

export async function fetchMyInfo() {
  return parseResponse(await fetch(`${USER_BASE_URL}/me`));
}

export async function updateMyInfo(payload) {
  return parseResponse(
    await fetch(`${USER_BASE_URL}/me`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    })
  );
}

export async function fetchMyFavorites(page = 0, size = 20) {
  return parseResponse(
    await fetch(`${STOCK_BASE_URL}/favorites?page=${page}&size=${size}`)
  );
}

export async function removeFavorite(tickerCode) {
  return parseResponse(
    await fetch(`${STOCK_BASE_URL}/${tickerCode}/favorite`, {
      method: "DELETE",
    })
  );
}