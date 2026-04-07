const BASE_URL = "http://localhost:8080/api/users";

async function parseResponse(response) {
  const json = await response.json();
  if (!response.ok) {
    throw new Error(json.message || "エラーが発生しました。");
  }
  return json;
}

export async function fetchMyInfo() {
  return parseResponse(await fetch(`${BASE_URL}/me`));
}

export async function updateMyInfo(payload) {
  return parseResponse(
    await fetch(`${BASE_URL}/me`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    })
  );
}

export async function fetchMyFavorites(page = 0, size = 20) {
  return parseResponse(
    await fetch(`${BASE_URL}/me/favorites?page=${page}&size=${size}`)
  );
}

export async function removeFavorite(tickerCode) {
  return parseResponse(
    await fetch(`${BASE_URL}/me/favorites/${tickerCode}`, {
      method: "DELETE",
    })
  );
}