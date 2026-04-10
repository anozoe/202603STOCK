const BASE_URL = "http://localhost:8080/api/admin";

async function fetchJson(url, options = {}) {
  const response = await fetch(url, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
    ...options,
  });

  const data = await response.json();

  if (!response.ok) {
    throw data;
  }

  return data;
}

export async function fetchAdminStocks(page = 0, size = 20) {
  return fetchJson(`${BASE_URL}/stocks?page=${page}&size=${size}`);
}

export async function createAdminStock(payload) {
  return fetchJson(`${BASE_URL}/stocks`, {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function updateAdminStock(id, payload) {
  return fetchJson(`${BASE_URL}/stocks/${id}`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
}

export async function deleteAdminStock(id) {
  return fetchJson(`${BASE_URL}/stocks/${id}`, {
    method: "DELETE",
  });
}

export async function reorderAdminStocks(stockIds) {
  return fetchJson(`${BASE_URL}/stocks/reorder`, {
    method: "PUT",
    body: JSON.stringify({ stockIds }),
  });
}

export async function fetchAdminUsers(page = 0, size = 20) {
  return fetchJson(`${BASE_URL}/users?page=${page}&size=${size}`);
}

export async function deleteAdminUser(id) {
  return fetchJson(`${BASE_URL}/users/${id}`, {
    method: "DELETE",
  });
}