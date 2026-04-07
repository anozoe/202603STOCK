const BASE_URL = "http://localhost:8080/api/admin";

async function parseResponse(response) {
  const json = await response.json();
  if (!response.ok) {
    throw new Error(json.message || "エラーが発生しました。");
  }
  return json;
}

export async function fetchAdminStocks(page = 0, size = 20) {
  return parseResponse(await fetch(`${BASE_URL}/stocks?page=${page}&size=${size}`));
}

export async function createAdminStock(payload) {
  return parseResponse(
    await fetch(`${BASE_URL}/stocks`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    })
  );
}

export async function updateAdminStock(id, payload) {
  return parseResponse(
    await fetch(`${BASE_URL}/stocks/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    })
  );
}

export async function deleteAdminStock(id) {
  return parseResponse(
    await fetch(`${BASE_URL}/stocks/${id}`, {
      method: "DELETE",
    })
  );
}

export async function reorderAdminStocks(stockIds) {
  return parseResponse(
    await fetch(`${BASE_URL}/stocks/reorder`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ stockIds }),
    })
  );
}

export async function fetchAdminUsers(page = 0, size = 20) {
  return parseResponse(await fetch(`${BASE_URL}/users?page=${page}&size=${size}`));
}

export async function deleteAdminUser(id) {
  return parseResponse(
    await fetch(`${BASE_URL}/users/${id}`, {
      method: "DELETE",
    })
  );
}