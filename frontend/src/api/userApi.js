import { getAuthHeaders } from "../utils/authHeader";

async function fetchJson(url, options = {}) {
  const response = await fetch(url, {
    ...options,
    headers: getAuthHeaders(options.headers || {}),
  });

  const text = await response.text();
  let data = null;

  try {
    data = text ? JSON.parse(text) : null;
  } catch (e) {
    data = { message: text || "サーバーエラー" };
  }

  if (!response.ok) {
    throw data || { message: "通信に失敗しました。" };
  }

  return data;
}

export async function fetchMyInfo() {
  return fetchJson("http://localhost:8080/api/users/me");
}

export async function updateMyInfo(payload) {
  return fetchJson("http://localhost:8080/api/users/me", {
    method: "PUT",
    body: JSON.stringify(payload),
  });
}

export async function fetchMyFavorites(page = 0, size = 20) {
  return fetchJson(`http://localhost:8080/api/stocks/favorites?page=${page}&size=${size}`);
}

export async function addFavorite(tickerCode) {
  return fetchJson(`http://localhost:8080/api/stocks/${tickerCode}/favorite`, {
    method: "POST",
  });
}

export async function removeFavorite(tickerCode) {
  return fetchJson(`http://localhost:8080/api/stocks/${tickerCode}/favorite`, {
    method: "DELETE",
  });
}